#!/bin/bash

set -e

echo "Starting UI tests with configuration:"
echo "  BASE_URL=${BASE_URL}"
echo "  BROWSER=${BROWSER}"
echo "  ENVIRONMENT=${ENVIRONMENT}"
echo "  EXECUTION=${EXECUTION}"
echo "  LANG=${LANG}"
echo "  PLATFORM=${PLATFORM}"
echo "  HEADLESS=${HEADLESS}"
echo "  RETRY_COUNT=${RETRY_COUNT}"
echo "  RETRY_DELAY_MS=${RETRY_DELAY_MS}"

# Install Playwright browsers if running in Docker
if [ "$EXECUTION" = "docker" ]; then
    echo "Installing Playwright browsers."
    mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps ${BROWSER}" -f /app/test-automation-ui/pom.xml
fi

cd /app/test-automation-ui

# Use for virtual display when headless mode is disabled
if [ "$EXECUTION" = "docker" ] && [ "$HEADLESS" != "true" ]; then
    echo "Running tests with virtual display..."
    xvfb-run --auto-servernum --server-args="-screen 0 1920x1080x24" mvn test
else
    echo "Running tests."
    mvn test
fi