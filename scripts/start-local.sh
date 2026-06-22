#!/bin/bash

# Load env từ root
set -a
source infra/docker/.env
set +a

set -e

echo "Starting infrastructure..."
cd infra/docker
docker compose up -dstart
cd ../..

echo "Starting api-gateway..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/services/api-gateway\" && ./mvnw spring-boot:run"'

echo "Starting auth-service..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/services/auth-service\" && ./mvnw spring-boot:run"'

echo "Starting user-service..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/services/user-service\" && ./mvnw spring-boot:run"'

echo "Starting product-service..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/services/product-service\" && ./mvnw spring-boot:run"'

echo "All local services are starting..."