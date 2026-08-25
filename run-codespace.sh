#!/bin/bash
set -e

echo "========================================================="
echo "   Building & Launching IECS Microservices in Codespace  "
echo "========================================================="

echo ">> Step 1/2: Compiling Java microservices..."
if command -v mvn >/dev/null 2>&1; then
    echo ">> Using local Maven..."
    mvn clean package -DskipTests -B
else
    echo ">> Maven not found locally. Using Docker Maven build container..."
    docker run --rm \
      -v "$PWD":/app \
      -w /app \
      -v maven-cache:/root/.m2 \
      maven:3.9.6-eclipse-temurin-17-alpine \
      mvn clean package -DskipTests -B
fi

echo ">> Step 2/2: Starting Docker Compose cluster..."
docker compose up -d --build

echo ""
echo "========================================================="
echo "  ✅ All microservices, Kafka, Redis & DBs are running! "
echo "========================================================="
docker compose ps
echo ""
echo ">> Your public API Gateway is accessible on Port 18080."
echo ">> Check the 'Ports' tab in Codespaces to copy the public URL."
