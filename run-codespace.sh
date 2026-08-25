#!/bin/bash
set -e

echo "========================================================="
echo "   Building & Launching IECS Microservices in Codespace  "
echo "========================================================="

echo ">> Step 1/2: Compiling Java microservices..."
mvn clean package -DskipTests

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
