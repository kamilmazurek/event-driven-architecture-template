@echo off
set DIR=%~dp0
call "%DIR%..\mvnw.cmd" -f "%DIR%pom.xml" %*