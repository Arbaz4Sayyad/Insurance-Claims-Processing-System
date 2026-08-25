#!/bin/bash
set -e

echo "========================================================"
echo " Starting IECS Enterprise Backend on Hugging Face Space "
echo "========================================================"

# Set up database URLs from Neon environment variables
DB_HOST="${NEON_HOST:-localhost}"
DB_PORT="${NEON_PORT:-5432}"
DB_USER="${NEON_USER:-his_user}"
DB_PASS="${NEON_PASSWORD:-his_password}"
DB_SSL=""

if [ -n "$NEON_HOST" ]; then
    DB_SSL="?sslmode=require"
    echo ">> Configured Neon PostgreSQL Host: $NEON_HOST"
fi

if [ -n "$NEON_SINGLE_DB" ]; then
    echo ">> Using single Neon Database: $NEON_SINGLE_DB"
    USER_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${NEON_SINGLE_DB}${DB_SSL}"
    AR_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${NEON_SINGLE_DB}${DB_SSL}"
    DC_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${NEON_SINGLE_DB}${DB_SSL}"
    ED_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${NEON_SINGLE_DB}${DB_SSL}"
    CO_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${NEON_SINGLE_DB}${DB_SSL}"
    BI_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${NEON_SINGLE_DB}${DB_SSL}"
    ANALYTICS_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${NEON_SINGLE_DB}${DB_SSL}"
else
    echo ">> Using multi-database URLs"
    USER_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/his_users${DB_SSL}"
    AR_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/his_ar${DB_SSL}"
    DC_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/his_dc${DB_SSL}"
    ED_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/his_ed${DB_SSL}"
    CO_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/his_co${DB_SSL}"
    BI_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/his_bi${DB_SSL}"
    ANALYTICS_DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/his_analytics${DB_SSL}"
fi

# Start Redis
echo ">> Starting Redis Cache..."
redis-server --daemonize yes || echo "Redis started or already running"

# Start KRaft Kafka
echo ">> Initializing KRaft Kafka..."
if [ -d "/opt/kafka" ]; then
    KAFKA_CLUSTER_ID="$(/opt/kafka/bin/kafka-storage.sh random-cluster-id 2>/dev/null || echo '4L622n22Q6qSoYYWqdaFiw')"
    /opt/kafka/bin/kafka-storage.sh format -t "$KAFKA_CLUSTER_ID" -c /opt/kafka/config/kraft/server.properties --ignore-formatted > /dev/null 2>&1 || true
    /opt/kafka/bin/kafka-server-start.sh -daemon /opt/kafka/config/kraft/server.properties
    echo ">> Kafka started in KRaft mode on localhost:9092"
fi

sleep 4

# Optimized JVM memory settings (total usage ~3GB out of available 16GB)
export JVM_BASE_OPTS="-Xms128m -Xmx350m -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -Djava.security.egd=file:/dev/./urandom"
export EUREKA_ZONE="http://127.0.0.1:8761/eureka/"
export CONFIG_IMPORT="optional:configserver:http://127.0.0.1:8888"
export KAFKA_BROKERS="127.0.0.1:9092"
export AR_SECRET="${AR_ENCRYPTION_SECRET:-3c5fG9H1j2K3l4N5p6Q7r8S9t0U1v2W3}"

# 1. Start Config Server (Port 8888)
echo ">> [1/11] Starting Config Server (Port 8888)..."
java $JVM_BASE_OPTS -Dserver.port=8888 -Dspring.profiles.active=native -jar /app/config-server.jar > /tmp/config-server.log 2>&1 &
sleep 12

# 2. Start Discovery Server / Eureka (Port 8761)
echo ">> [2/11] Starting Discovery Server / Eureka (Port 8761)..."
java $JVM_BASE_OPTS -Dserver.port=8761 -Deureka.instance.hostname=127.0.0.1 -Deureka.client.serviceUrl.defaultZone=http://127.0.0.1:8761/eureka/ -jar /app/discovery-server.jar > /tmp/discovery-server.log 2>&1 &
sleep 12

# 3. Start User Service (Port 8081)
echo ">> [3/11] Starting User Service (Port 8081)..."
java $JVM_BASE_OPTS \
  -Dserver.port=8081 \
  -Dspring.config.import="$CONFIG_IMPORT" \
  -Deureka.client.serviceUrl.defaultZone="$EUREKA_ZONE" \
  -Dspring.datasource.url="$USER_DB_URL" \
  -Dspring.datasource.username="$DB_USER" \
  -Dspring.datasource.password="$DB_PASS" \
  -jar /app/user-service.jar > /tmp/user-service.log 2>&1 &

# 4. Start AR Service (Port 8082)
echo ">> [4/11] Starting AR Service (Port 8082)..."
java $JVM_BASE_OPTS \
  -Dserver.port=8082 \
  -Dspring.config.import="$CONFIG_IMPORT" \
  -Deureka.client.serviceUrl.defaultZone="$EUREKA_ZONE" \
  -Dspring.datasource.url="$AR_DB_URL" \
  -Dspring.datasource.username="$DB_USER" \
  -Dspring.datasource.password="$DB_PASS" \
  -Dspring.cloud.stream.kafka.binder.brokers="$KAFKA_BROKERS" \
  -Dar.encryption.secret="$AR_SECRET" \
  -jar /app/ar-service.jar > /tmp/ar-service.log 2>&1 &

# 5. Start DC Service (Port 8083)
echo ">> [5/11] Starting DC Service (Port 8083)..."
java $JVM_BASE_OPTS \
  -Dserver.port=8083 \
  -Dspring.config.import="$CONFIG_IMPORT" \
  -Deureka.client.serviceUrl.defaultZone="$EUREKA_ZONE" \
  -Dspring.datasource.url="$DC_DB_URL" \
  -Dspring.datasource.username="$DB_USER" \
  -Dspring.datasource.password="$DB_PASS" \
  -Dspring.cloud.stream.kafka.binder.brokers="$KAFKA_BROKERS" \
  -Ddc.validation.client.url="http://127.0.0.1:7860" \
  -jar /app/dc-service.jar > /tmp/dc-service.log 2>&1 &

# 6. Start ED Service (Port 8084)
echo ">> [6/11] Starting ED Service (Port 8084)..."
java $JVM_BASE_OPTS \
  -Dserver.port=8084 \
  -Dspring.config.import="$CONFIG_IMPORT" \
  -Deureka.client.serviceUrl.defaultZone="$EUREKA_ZONE" \
  -Dspring.datasource.url="$ED_DB_URL" \
  -Dspring.datasource.username="$DB_USER" \
  -Dspring.datasource.password="$DB_PASS" \
  -Dspring.cloud.stream.kafka.binder.brokers="$KAFKA_BROKERS" \
  -Ded.client.ar.url="http://127.0.0.1:7860" \
  -Ded.client.dc.url="http://127.0.0.1:7860" \
  -jar /app/ed-service.jar > /tmp/ed-service.log 2>&1 &

# 7. Start CO Service (Port 8085)
echo ">> [7/11] Starting CO Service (Port 8085)..."
java $JVM_BASE_OPTS \
  -Dserver.port=8085 \
  -Dspring.config.import="$CONFIG_IMPORT" \
  -Deureka.client.serviceUrl.defaultZone="$EUREKA_ZONE" \
  -Dspring.datasource.url="$CO_DB_URL" \
  -Dspring.datasource.username="$DB_USER" \
  -Dspring.datasource.password="$DB_PASS" \
  -Dspring.cloud.stream.kafka.binder.brokers="$KAFKA_BROKERS" \
  -jar /app/co-service.jar > /tmp/co-service.log 2>&1 &

# 8. Start BI Service (Port 8086)
echo ">> [8/11] Starting BI Service (Port 8086)..."
java $JVM_BASE_OPTS \
  -Dserver.port=8086 \
  -Dspring.config.import="$CONFIG_IMPORT" \
  -Deureka.client.serviceUrl.defaultZone="$EUREKA_ZONE" \
  -Dspring.datasource.url="$BI_DB_URL" \
  -Dspring.datasource.username="$DB_USER" \
  -Dspring.datasource.password="$DB_PASS" \
  -Dspring.cloud.stream.kafka.binder.brokers="$KAFKA_BROKERS" \
  -jar /app/bi-service.jar > /tmp/bi-service.log 2>&1 &

# 9. Start Analytics Service (Port 8087)
echo ">> [9/11] Starting Analytics Service (Port 8087)..."
java $JVM_BASE_OPTS \
  -Dserver.port=8087 \
  -Dspring.config.import="$CONFIG_IMPORT" \
  -Deureka.client.serviceUrl.defaultZone="$EUREKA_ZONE" \
  -Dspring.datasource.url="$ANALYTICS_DB_URL" \
  -Dspring.datasource.username="$DB_USER" \
  -Dspring.datasource.password="$DB_PASS" \
  -Dspring.cloud.stream.kafka.binder.brokers="$KAFKA_BROKERS" \
  -jar /app/analytics-service.jar > /tmp/analytics-service.log 2>&1 &

# 10. Start Dashboard Aggregator (Port 8088)
echo ">> [10/11] Starting Dashboard Aggregator (Port 8088)..."
java $JVM_BASE_OPTS \
  -Dserver.port=8088 \
  -Dspring.config.import="$CONFIG_IMPORT" \
  -Deureka.client.serviceUrl.defaultZone="$EUREKA_ZONE" \
  -jar /app/dashboard-aggregator.jar > /tmp/dashboard-aggregator.log 2>&1 &

echo ">> Waiting 15 seconds for microservices to register with Eureka..."
sleep 15

# 11. Start API Gateway on Port 7860 (Hugging Face default ingress port) in Foreground
echo ">> [11/11] Starting API Gateway on Port 7860..."
exec java $JVM_BASE_OPTS \
  -Dserver.port=7860 \
  -Dspring.profiles.active=dev \
  -Dspring.config.import="$CONFIG_IMPORT" \
  -Deureka.client.serviceUrl.defaultZone="$EUREKA_ZONE" \
  -jar /app/api-gateway.jar
