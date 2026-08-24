#!/usr/bin/env bash
# =====================================================================
# IECS Enterprise: Resilience4j Circuit Breaker & Chaos Simulation Test
# =====================================================================

set -e

GATEWAY_URL="http://localhost:18080"
echo "=== 1. Checking Baseline Health ==="
curl -s "${GATEWAY_URL}/actuator/health" | grep "UP" && echo "API Gateway is UP"

echo "=== 2. Simulating ED-Service Degradation / Latency ==="
# Temporarily pause ed-service container to simulate network outage
docker pause his_ed || true

echo "=== 3. Sending 15 Requests to trigger Circuit Breaker Transition ==="
for i in {1..15}; do
  RESPONSE=$(curl -s -w "\nHTTP_STATUS:%{http_code}\nTIME:%{time_total}s\n" \
    -X POST "${GATEWAY_URL}/api/ar/register" \
    -H "Content-Type: application/json" \
    -d '{"fullName":"Chaos Test User","ssn":"666-45-1234","planName":"MEDICAID"}')
  echo "Request #$i -> $RESPONSE"
  sleep 0.2
done

echo "=== 4. Checking Resilience4j Metrics ==="
curl -s "${GATEWAY_URL}/actuator/metrics/resilience4j.circuitbreaker.state" || true

echo "=== 5. Resuming ED-Service Container ==="
docker unpause his_ed || true
echo "Chaos test completed successfully."
