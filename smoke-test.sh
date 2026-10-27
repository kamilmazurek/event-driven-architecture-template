#!/usr/bin/env bash
set -euo pipefail

MODE="${1:-all}"
PRODUCER_PORT="${PRODUCER_PORT:-8080}"
CONSUMER_PORT="${CONSUMER_PORT:-8081}"
KAFKA_PORT="${KAFKA_PORT:-9092}"
STARTUP_TIMEOUT="${STARTUP_TIMEOUT:-60}"
EVENT_TIMEOUT="${EVENT_TIMEOUT:-30}"
SKIP_BUILD="${SKIP_BUILD:-false}"
PRODUCER_URL="http://localhost:${PRODUCER_PORT}"
CONSUMER_URL="http://localhost:${CONSUMER_PORT}"
KAFKA_IMAGE="apache/kafka:3.9.1"
PRODUCER_IMAGE="template/event-driven-architecture-template-producer:smoke-test"
CONSUMER_IMAGE="template/event-driven-architecture-template-consumer:smoke-test"
NETWORK="event-driven-architecture-template-smoke-test"
KAFKA_CONTAINER="event-driven-architecture-template-smoke-test-kafka"
PRODUCER_CONTAINER="event-driven-architecture-template-smoke-test-producer"
CONSUMER_CONTAINER="event-driven-architecture-template-smoke-test-consumer"

cd "$(dirname "$0")"
WORK_DIR="$(mktemp -d)"
PRODUCER_PID=""
CONSUMER_PID=""
CHECKS=0

log() {
    echo "[smoke-test] $*"
}

fail() {
    echo "[smoke-test] FAILED: $*" >&2
    print_app_logs
    exit 1
}

container_exists() {
    docker container inspect "$1" &>/dev/null
}

print_app_logs() {
    local name
    for name in producer consumer; do
        if [[ -f "$WORK_DIR/$name.log" ]]; then
            echo "----- $name logs -----" >&2
            tail -n 50 "$WORK_DIR/$name.log" >&2
        fi
    done
    for name in "$PRODUCER_CONTAINER" "$CONSUMER_CONTAINER" "$KAFKA_CONTAINER"; do
        if container_exists "$name"; then
            echo "----- $name container logs -----" >&2
            docker logs --tail 50 "$name" >&2
        fi
    done
}

stop_process() {
    local pid="$1"
    if [[ -n "$pid" ]]; then
        kill "$pid" 2>/dev/null || true
        wait "$pid" 2>/dev/null || true
    fi
}

stop_all() {
    stop_process "$PRODUCER_PID"
    stop_process "$CONSUMER_PID"
    PRODUCER_PID=""
    CONSUMER_PID=""
    rm -f "$WORK_DIR/producer.log" "$WORK_DIR/consumer.log"
    local name
    for name in "$PRODUCER_CONTAINER" "$CONSUMER_CONTAINER" "$KAFKA_CONTAINER"; do
        if container_exists "$name"; then
            docker rm -f "$name" >/dev/null
        fi
    done
    if docker network inspect "$NETWORK" &>/dev/null; then
        docker network rm "$NETWORK" >/dev/null
    fi
}

cleanup() {
    stop_all
    rm -rf "$WORK_DIR"
}
trap cleanup EXIT

request() {
    local method="$1" url="$2" data="${3:-}"
    local args=(-s -o "$WORK_DIR/body" -w "%{http_code}" -X "$method")
    if [[ -n "$data" ]]; then
        args+=(-H "Content-Type: application/json" -d "$data")
    fi
    STATUS="$(curl "${args[@]}" "$url")" || fail "$method $url - connection error"
    BODY="$(cat "$WORK_DIR/body")"
}

expect() {
    local expected_status="$1" method="$2" url="$3" data="${4:-}"
    shift 4 || shift $#
    request "$method" "$url" "$data"
    [[ "$STATUS" == "$expected_status" ]] || fail "$method $url - expected status $expected_status but was $STATUS, body: $BODY"
    local fragment
    for fragment in "$@"; do
        [[ "$BODY" == *"$fragment"* ]] || fail "$method $url - expected body to contain '$fragment' but was: $BODY"
    done
    CHECKS=$((CHECKS + 1))
    log "OK  $method $url -> $STATUS"
}

start_kafka() {
    local port_args=("$@")
    log "Starting Kafka..."
    docker network create "$NETWORK" >/dev/null
    docker run -d --name "$KAFKA_CONTAINER" --network "$NETWORK" "${port_args[@]}" \
        -e KAFKA_NODE_ID=1 \
        -e KAFKA_PROCESS_ROLES=broker,controller \
        -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 \
        -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,EXTERNAL:PLAINTEXT \
        -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093,EXTERNAL://0.0.0.0:9094 \
        -e KAFKA_ADVERTISED_LISTENERS="PLAINTEXT://${KAFKA_CONTAINER}:9092,EXTERNAL://localhost:${KAFKA_PORT}" \
        -e KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT \
        -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER \
        -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
        -e KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1 \
        -e KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS=0 \
        "$KAFKA_IMAGE" >/dev/null

    local deadline=$((SECONDS + STARTUP_TIMEOUT))
    until kafka_topics &>/dev/null; do
        if (( SECONDS >= deadline )); then
            fail "Kafka did not become ready within ${STARTUP_TIMEOUT}s"
        fi
        sleep 1
    done
    log "Kafka is ready"
}

kafka_topics() {
    MSYS_NO_PATHCONV=1 docker exec "$KAFKA_CONTAINER" /opt/kafka/bin/kafka-topics.sh --bootstrap-server "${KAFKA_CONTAINER}:9092" --list
}

wait_until_healthy() {
    local name="$1" url="$2" pid="${3:-}"
    log "Waiting up to ${STARTUP_TIMEOUT}s for $name to become healthy..."
    local start=$SECONDS
    local deadline=$((SECONDS + STARTUP_TIMEOUT))
    until curl -sf "${url}/actuator/health" 2>/dev/null | grep -q '"status":"UP"'; do
        if [[ -n "$pid" ]] && ! kill -0 "$pid" 2>/dev/null; then
            fail "$name process exited during startup"
        fi
        if (( SECONDS >= deadline )); then
            fail "$name did not become healthy within ${STARTUP_TIMEOUT}s"
        fi
        sleep 1
    done
    log "$name is up after $((SECONDS - start))s"
}

wait_until_consumed() {
    local item_id="$1"
    local deadline=$((SECONDS + EVENT_TIMEOUT))
    until request GET "${CONSUMER_URL}/items" && [[ "$STATUS" == "200" && "$BODY" == *"\"id\":\"${item_id}\""* ]]; do
        if (( SECONDS >= deadline )); then
            fail "item $item_id was not consumed within ${EVENT_TIMEOUT}s, consumer items: $BODY"
        fi
        sleep 1
    done
    CHECKS=$((CHECKS + 1))
    log "OK  item $item_id created by producer is available in consumer"
}

run_checks() {
    # actuator
    expect 200 GET "${PRODUCER_URL}/actuator" "" '/actuator/health'
    expect 200 GET "${PRODUCER_URL}/actuator/health" "" '"status":"UP"'
    expect 200 GET "${CONSUMER_URL}/actuator" "" '/actuator/health'
    expect 200 GET "${CONSUMER_URL}/actuator/health" "" '"status":"UP"'

    # consumer starts with no items
    expect 200 GET "${CONSUMER_URL}/items" "" '[]'

    # producer validates requests
    expect 400 POST "${PRODUCER_URL}/items" '{"name":""}'

    # create item and publish event
    expect 201 POST "${PRODUCER_URL}/items" '{"name":"Smoke Item A"}' '"id":"' '"name":"Smoke Item A"'
    local item_id
    item_id="$(sed -n 's/.*"id":"\([^"]*\)".*/\1/p' <<<"$BODY")"
    [[ -n "$item_id" ]] || fail "no item ID in response: $BODY"

    # event is sent to topic and consumed
    kafka_topics | grep -qx 'item-created' || fail "topic 'item-created' does not exist"
    CHECKS=$((CHECKS + 1))
    log "OK  topic 'item-created' exists"
    wait_until_consumed "$item_id"
    expect 200 GET "${CONSUMER_URL}/items" "" "\"id\":\"${item_id}\"" '"name":"Smoke Item A"'
}

find_jar() {
    local module="$1" jar
    jar="$(ls "$module"/target/"$module"-*.jar 2>/dev/null | grep -v '\.original$' | head -n 1)"
    [[ -n "$jar" ]] || fail "no JAR found in $module/target directory"
    echo "$jar"
}

build_jars() {
    if [[ "$SKIP_BUILD" == "true" ]]; then
        log "Skipping build"
    else
        log "Building application..."
        ./mvnw -B -q clean package -DskipTests
    fi
    PRODUCER_JAR="$(find_jar producer)"
    CONSUMER_JAR="$(find_jar consumer)"
}

smoke_test_jar() {
    log "=== JAR deployment: $PRODUCER_JAR, $CONSUMER_JAR ==="
    start_kafka -p "${KAFKA_PORT}:9094"
    java -jar "$PRODUCER_JAR" --server.port="$PRODUCER_PORT" --spring.kafka.bootstrap-servers="localhost:${KAFKA_PORT}" >"$WORK_DIR/producer.log" 2>&1 &
    PRODUCER_PID=$!
    java -jar "$CONSUMER_JAR" --server.port="$CONSUMER_PORT" --spring.kafka.bootstrap-servers="localhost:${KAFKA_PORT}" >"$WORK_DIR/consumer.log" 2>&1 &
    CONSUMER_PID=$!
    wait_until_healthy producer "$PRODUCER_URL" "$PRODUCER_PID"
    wait_until_healthy consumer "$CONSUMER_URL" "$CONSUMER_PID"
    run_checks
    stop_all
}

check_non_root_user() {
    local container="$1" user
    user="$(docker exec "$container" whoami)"
    [[ "$user" == "template-user" ]] || fail "$container runs as '$user' instead of 'template-user'"
    CHECKS=$((CHECKS + 1))
    log "OK  $container runs as non-root user '$user'"
}

smoke_test_docker() {
    log "=== Docker deployment: $PRODUCER_IMAGE, $CONSUMER_IMAGE ==="
    docker build -q -t "$PRODUCER_IMAGE" -f producer/Dockerfile ./producer >/dev/null
    docker build -q -t "$CONSUMER_IMAGE" -f consumer/Dockerfile ./consumer >/dev/null
    start_kafka
    docker run -d --name "$PRODUCER_CONTAINER" --network "$NETWORK" -p "${PRODUCER_PORT}:8080" \
        -e SPRING_KAFKA_BOOTSTRAP_SERVERS="${KAFKA_CONTAINER}:9092" "$PRODUCER_IMAGE" >/dev/null
    docker run -d --name "$CONSUMER_CONTAINER" --network "$NETWORK" -p "${CONSUMER_PORT}:8081" \
        -e SPRING_KAFKA_BOOTSTRAP_SERVERS="${KAFKA_CONTAINER}:9092" "$CONSUMER_IMAGE" >/dev/null
    wait_until_healthy producer "$PRODUCER_URL"
    wait_until_healthy consumer "$CONSUMER_URL"
    check_non_root_user "$PRODUCER_CONTAINER"
    check_non_root_user "$CONSUMER_CONTAINER"
    run_checks
    stop_all
}

case "$MODE" in
    jar|docker|all) ;;
    *) echo "Usage: $0 [jar|docker|all]" >&2; exit 2 ;;
esac

command -v docker >/dev/null || fail "docker is not installed"
build_jars
[[ "$MODE" == "docker" ]] || smoke_test_jar
[[ "$MODE" == "jar" ]] || smoke_test_docker
log "PASSED ($CHECKS checks)"
