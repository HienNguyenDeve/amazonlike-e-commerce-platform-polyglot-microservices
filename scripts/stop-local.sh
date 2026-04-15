#!/bin/bash

set -e

echo "Stopping infrastructure..."
cd infra/docker
docker compose down
cd ../..

echo "Stopping Spring Boot services on ports 8080, 8081, 8082..."

lsof -ti :8080 | xargs kill -9 2>/dev/null || true
lsof -ti :8081 | xargs kill -9 2>/dev/null || true
lsof -ti :8082 | xargs kill -9 2>/dev/null || true

echo "All local services stopped."