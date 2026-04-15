#!/bin/bash

set -e

echo "Starting infrastructure..."
cd infra/docker
docker compose up -d
cd ../..

echo "Starting api-gateway..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/services/api-gateway\" && ./mvnw spring-boot:run"'

echo "Starting auth-service..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/services/auth-service\" && ./mvnw spring-boot:run"'

echo "Starting product-service..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/services/product-service\" && ./mvnw spring-boot:run"'

echo "All local services are starting..."