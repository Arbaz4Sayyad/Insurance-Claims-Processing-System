import http from 'k6/http';
import { check, sleep } from 'k6';

// k6 Load Test Configuration for Claims Processing API Gateway
export const options = {
  stages: [
    { duration: '30s', target: 500 },   // Ramp-up to 500 virtual users
    { duration: '1m',  target: 2000 },  // Sustained peak load at 2,000 concurrent VUs
    { duration: '30s', target: 0 },     // Ramp-down to 0
  ],
  thresholds: {
    // Assert that 99% of requests complete in under 250ms
    http_req_duration: ['p(99)<250'],
    // Assert error rate is below 1%
    http_req_failed: ['rate<0.01'],
  },
};

export default function () {
  const url = __ENV.API_GATEWAY_URL || 'http://localhost:18080/api/ar/register';
  
  const payload = JSON.stringify({
    fullName: `Test Citizen ${__VU}`,
    email: `citizen_${__VU}_${__ITER}@example.com`,
    ssn: "666-45-1234",
    dob: "1990-05-15",
    gender: "MALE",
    planName: "MEDICAID"
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer MOCK_JWT_TEST_TOKEN'
    },
  };

  const res = http.post(url, payload, params);

  check(res, {
    'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
    'response has status field': (r) => r.body && r.body.includes('status'),
  });

  // Short pause between iterations to simulate realistic user pace
  sleep(0.1);
}
