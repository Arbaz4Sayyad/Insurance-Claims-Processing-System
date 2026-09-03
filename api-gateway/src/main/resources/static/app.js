// Health Insurance System (HIS) - Interactive Developer Portal & API Hub Engine
// Complete 11 Microservices Coverage: Gateway, Eureka, Config, User, AR, DC, ED, BI, CO, Analytics, Dashboard BFF

const API_CATALOG = [
  {
    category: "1. Authentication & User Management",
    endpoints: [
      {
        id: "auth-signin",
        method: "POST",
        path: "/api/auth/signin",
        summary: "User / Admin Signin (JWT Issuer)",
        description: "Authenticates citizen, caseworker, or admin credentials and issues a signed JWT Bearer token with embedded roles.",
        service: "user-service",
        port: "8081",
        requiresAuth: false,
        headers: { "Content-Type": "application/json" },
        params: [],
        body: {
          username: "admin",
          password: "Admin@123"
        },
        mockResponse: {
          success: true,
          message: "Login successful",
          data: {
            token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNTM4MDAwMCwiZXhwIjoxNzI1NDY2NDAwfQ.claims_signature_demo",
            role: "ADMIN",
            userId: 1,
            expiresAt: "2026-09-04T12:00:00Z"
          },
          timestamp: new Date().toISOString()
        }
      },
      {
        id: "auth-signup",
        method: "POST",
        path: "/api/auth/signup",
        summary: "Citizen Public Registration",
        description: "Creates a new citizen account with BCrypt password hashing and assigns default ROLE_USER.",
        service: "user-service",
        port: "8081",
        requiresAuth: false,
        headers: { "Content-Type": "application/json" },
        params: [],
        body: {
          username: "sarah_connor",
          email: "sarah.connor@example.com",
          password: "Password@123",
          role: ["ROLE_USER"]
        },
        mockResponse: {
          success: true,
          message: "User registered successfully!",
          data: null,
          timestamp: new Date().toISOString()
        }
      },
      {
        id: "users-get-all",
        method: "GET",
        path: "/api/users",
        summary: "List All Users by Role (Admin)",
        description: "Fetches system accounts filtered by role (ADMIN, CASE_WORKER, USER). Requires ROLE_ADMIN privilege.",
        service: "user-service",
        port: "8081",
        requiresAuth: true,
        headers: {},
        queryParams: [{ key: "role", value: "CASE_WORKER" }],
        mockResponse: [
          { id: 1, username: "admin", email: "admin@his.gov", enabled: true, roles: [{ id: 1, name: "ROLE_ADMIN" }] },
          { id: 2, username: "cw_johnson", email: "cw.johnson@his.gov", enabled: true, roles: [{ id: 2, name: "ROLE_CASE_WORKER" }] }
        ]
      },
      {
        id: "users-create-caseworker",
        method: "POST",
        path: "/api/users/create-caseworker",
        summary: "Create Caseworker Account (Admin)",
        description: "Provisions a verified Caseworker profile with case assignment permissions.",
        service: "user-service",
        port: "8081",
        requiresAuth: true,
        headers: { "Content-Type": "application/json" },
        body: {
          username: "cw_miller",
          email: "cw.miller@his.gov",
          password: "SecureCW@2026",
          role: ["ROLE_CASE_WORKER"]
        },
        mockResponse: {
          id: 3,
          username: "cw_miller",
          email: "cw.miller@his.gov",
          enabled: true,
          roles: [{ id: 2, name: "ROLE_CASE_WORKER" }]
        }
      },
      {
        id: "users-stats",
        method: "GET",
        path: "/api/users/stats/counts",
        summary: "User Population & Role Statistics",
        description: "Returns distribution counts of registered citizens, caseworkers, and active admin accounts.",
        service: "user-service",
        port: "8081",
        requiresAuth: true,
        headers: {},
        mockResponse: {
          success: true,
          message: "User statistics retrieved successfully",
          data: {
            totalUsers: 1420,
            activeCitizens: 1350,
            caseWorkers: 65,
            admins: 5
          }
        }
      }
    ]
  },
  {
    category: "2. Application Registration (AR)",
    endpoints: [
      {
        id: "ar-register",
        method: "POST",
        path: "/api/ar/register",
        summary: "Register Citizen Claim (SSN Encrypted + Outbox)",
        description: "Validates SSN checksum, AES-256 encrypts citizen PII, persists application record, and writes application.created event to Transactional Outbox.",
        service: "ar-service",
        port: "8082",
        requiresAuth: true,
        headers: { "Content-Type": "application/json" },
        params: [],
        body: {
          firstName: "Sarah",
          lastName: "Connor",
          dob: "1990-05-15",
          gender: "FEMALE",
          ssn: "666-12-3456",
          userId: 1
        },
        mockResponse: {
          success: true,
          message: "Application registered successfully.",
          data: {
            appId: 1001,
            status: "SUCCESS",
            message: "Application registered successfully."
          },
          timestamp: new Date().toISOString()
        }
      },
      {
        id: "ar-get-all",
        method: "GET",
        path: "/api/ar/all",
        summary: "List Applications (Paginated & Filtered)",
        description: "Returns paginated list of claims filtered by workflow or case status (PENDING, APPROVED, REJECTED).",
        service: "ar-service",
        port: "8082",
        requiresAuth: true,
        headers: {},
        queryParams: [
          { key: "status", value: "PENDING" },
          { key: "page", value: "0" },
          { key: "size", value: "10" }
        ],
        mockResponse: {
          success: true,
          message: "Applications retrieved successfully",
          data: {
            content: [
              {
                id: 1001,
                caseStatus: "PENDING",
                workflowStatus: "REGISTRATION_COMPLETE",
                createdAt: "2026-09-03T10:15:00",
                citizen: { id: 1, firstName: "Sarah", lastName: "Connor", gender: "FEMALE" }
              }
            ],
            totalElements: 1,
            totalPages: 1,
            number: 0
          }
        }
      },
      {
        id: "ar-get-by-id",
        method: "GET",
        path: "/api/ar/{appId}",
        summary: "Get Application Details by ID",
        description: "Retrieves complete claim profile and verification stage.",
        service: "ar-service",
        port: "8082",
        requiresAuth: true,
        headers: {},
        params: [{ key: "appId", value: "1001" }],
        mockResponse: {
          success: true,
          message: "Application found",
          data: {
            id: 1001,
            caseStatus: "PENDING",
            workflowStatus: "DATA_COLLECTION_PENDING",
            citizen: { id: 1, firstName: "Sarah", lastName: "Connor", dob: "1990-05-15", gender: "FEMALE" }
          }
        }
      },
      {
        id: "ar-stats",
        method: "GET",
        path: "/api/ar/stats",
        summary: "Application Pipeline Statistics",
        description: "Breakdown of PENDING, APPROVED, and REJECTED claims in queue.",
        service: "ar-service",
        port: "8082",
        requiresAuth: true,
        headers: {},
        mockResponse: {
          success: true,
          message: "Application statistics retrieved",
          data: { PENDING: 18, APPROVED: 245, REJECTED: 12 }
        }
      },
      {
        id: "ar-plans",
        method: "GET",
        path: "/api/ar/plans",
        summary: "List Insurance Benefit Plans",
        description: "Returns government assistance programs (SNAP, Medicaid, CCAP, QHP).",
        service: "ar-service",
        port: "8082",
        requiresAuth: false,
        headers: {},
        mockResponse: {
          success: true,
          message: "Plans retrieved successfully",
          data: [
            { id: 1, name: "SNAP Healthcare Plus", category: "FOOD_ASSISTANCE", status: "ACTIVE", members: 1240 },
            { id: 2, name: "Medicaid Comprehensive", category: "HEALTHCARE", status: "ACTIVE", members: 3410 },
            { id: 3, name: "CCAP Child Care Assistance", category: "CHILD_CARE", status: "ACTIVE", members: 890 }
          ]
        }
      }
    ]
  },
  {
    category: "3. Data Collection (DC)",
    endpoints: [
      {
        id: "dc-create-case",
        method: "POST",
        path: "/api/dc/case/{appId}",
        summary: "Initialize DC Case (Feign Validated)",
        description: "Validates application existence via OpenFeign client to AR service and initializes data collection session.",
        service: "dc-service",
        port: "8083",
        requiresAuth: true,
        headers: {},
        params: [{ key: "appId", value: "1001" }],
        mockResponse: {
          success: true,
          message: "Case created/retrieved for AppId: 1001",
          data: "Case initialized"
        }
      },
      {
        id: "dc-add-income",
        method: "POST",
        path: "/api/dc/income/{appId}",
        summary: "Add Monthly Income Details",
        description: "Captures monthly salary, rental earnings, and property assets for federal poverty line check.",
        service: "dc-service",
        port: "8083",
        requiresAuth: true,
        headers: { "Content-Type": "application/json" },
        params: [{ key: "appId", value: "1001" }],
        body: {
          monthlySalary: 2400.00,
          rentIncome: 0.00,
          propertyIncome: 120.00
        },
        mockResponse: {
          success: true,
          message: "Income added successfully",
          data: "Income added"
        }
      },
      {
        id: "dc-add-household",
        method: "POST",
        path: "/api/dc/household/{appId}",
        summary: "Add Household Dependent",
        description: "Registers dependent children or spouse to compute total household size ratio.",
        service: "dc-service",
        port: "8083",
        requiresAuth: true,
        headers: { "Content-Type": "application/json" },
        params: [{ key: "appId", value: "1001" }],
        body: {
          name: "John Connor",
          dob: "2015-02-28",
          ssn: "666-44-3322",
          relationship: "CHILD"
        },
        mockResponse: {
          success: true,
          message: "Household member added successfully",
          data: "Household member added"
        }
      },
      {
        id: "dc-add-expense",
        method: "POST",
        path: "/api/dc/expense/{appId}",
        summary: "Add Monthly Expenses & Medical Costs",
        description: "Records rent, utilities, and ongoing medical expenses for allowable deduction offsets.",
        service: "dc-service",
        port: "8083",
        requiresAuth: true,
        headers: { "Content-Type": "application/json" },
        params: [{ key: "appId", value: "1001" }],
        body: {
          housingExpense: 950.00,
          utilityExpense: 180.00,
          medicalExpense: 220.00
        },
        mockResponse: {
          success: true,
          message: "Expense added successfully",
          data: "Expense added"
        }
      },
      {
        id: "dc-summary",
        method: "GET",
        path: "/api/dc/summary/{appId}",
        summary: "Get Case Data Collection Summary",
        description: "Aggregates captured income, dependents, and expenses for caseworker verification.",
        service: "dc-service",
        port: "8083",
        requiresAuth: true,
        headers: {},
        params: [{ key: "appId", value: "1001" }],
        mockResponse: {
          success: true,
          message: "Case summary retrieved",
          data: {
            caseId: 401,
            appId: 1001,
            incomeRecords: [{ monthlySalary: 2400.00, propertyIncome: 120.00 }],
            householdMembers: [{ name: "John Connor", relationship: "CHILD" }],
            expenseRecords: [{ housingExpense: 950.00, utilityExpense: 180.00 }]
          }
        }
      },
      {
        id: "dc-complete",
        method: "POST",
        path: "/api/dc/complete/{appId}",
        summary: "Complete DC & Dispatch Kafka Stream Event",
        description: "Publishes 'dataCaptured-out-0' event to Kafka topic to asynchronously trigger Eligibility Determination engine.",
        service: "dc-service",
        port: "8083",
        requiresAuth: true,
        headers: {},
        params: [{ key: "appId", value: "1001" }],
        mockResponse: {
          success: true,
          message: "Data collection marked as complete for AppId: 1001",
          data: "Captured"
        }
      }
    ]
  },
  {
    category: "4. Eligibility Determination (ED)",
    endpoints: [
      {
        id: "ed-determine",
        method: "POST",
        path: "/api/ed/determine/{appId}",
        summary: "Execute Plan Eligibility Rules Engine",
        description: "Runs deterministic rule engine against FPL (Federal Poverty Level) tables, child age criteria, and employment status.",
        service: "ed-service",
        port: "8084",
        requiresAuth: true,
        headers: {},
        params: [{ key: "appId", value: "1001" }],
        mockResponse: {
          success: true,
          message: "Eligibility determined successfully",
          data: [
            {
              edTraceId: 501,
              appId: 1001,
              planName: "SNAP Healthcare Plus",
              status: "APPROVED",
              benefitAmount: 480.00,
              startDate: "2026-10-01",
              endDate: "2027-09-30",
              denialReason: null
            }
          ]
        }
      },
      {
        id: "ed-history",
        method: "GET",
        path: "/api/ed/history/{appId}",
        summary: "Audit History of Eligibility Decisions",
        description: "Retrieves complete audit trail of prior automated and manual determination runs.",
        service: "ed-service",
        port: "8084",
        requiresAuth: true,
        headers: {},
        params: [{ key: "appId", value: "1001" }],
        mockResponse: {
          success: true,
          message: "History retrieved",
          data: [
            { edTraceId: 501, appId: 1001, planName: "SNAP Healthcare Plus", status: "APPROVED", determinedAt: "2026-09-03T10:20:00" }
          ]
        }
      },
      {
        id: "ed-stats",
        method: "GET",
        path: "/api/ed/stats",
        summary: "Daily Rules Engine Throughput & Approval Rate",
        description: "Real-time metrics on claims evaluated today, approvals, rejections, and percentage rates.",
        service: "ed-service",
        port: "8084",
        requiresAuth: false,
        headers: {},
        mockResponse: {
          success: true,
          message: "Stats retrieved",
          data: {
            approvedToday: 52,
            rejectedToday: 4,
            approvalRate: 92.85
          }
        }
      }
    ]
  },
  {
    category: "5. Benefit Issuance (BI)",
    endpoints: [
      {
        id: "bi-status",
        method: "GET",
        path: "/api/bi/status/{appId}",
        summary: "Check Monthly Benefit Account & Balance",
        description: "Retrieves active electronic benefit transfer (EBT) account, active status, and scheduled monthly disbursement amount.",
        service: "bi-service",
        port: "8085",
        requiresAuth: true,
        headers: {},
        params: [{ key: "appId", value: "1001" }],
        mockResponse: {
          success: true,
          message: "Benefit account found",
          data: {
            id: 88,
            appId: 1001,
            benefitAmount: 480.00,
            accountStatus: "ACTIVE",
            disbursementSchedule: "MONTHLY_1ST",
            cardLast4: "4912"
          }
        }
      }
    ]
  },
  {
    category: "6. Correspondence Service (CO)",
    endpoints: [
      {
        id: "co-all-notices",
        method: "GET",
        path: "/api/notifications",
        summary: "List Citizen Notices & Determination Letters",
        description: "Fetches official correspondence letters, approval notices, and PDF download links.",
        service: "co-service",
        port: "8086",
        requiresAuth: true,
        headers: {},
        mockResponse: [
          {
            id: 1,
            appId: 1001,
            noticeType: "ELIGIBILITY_APPROVAL",
            title: "Official Notice: SNAP Healthcare Plan Approved",
            noticePdfUrl: "/api/notifications/download/1001.pdf",
            isRead: false,
            createdAt: "2026-09-03T10:25:00"
          }
        ]
      },
      {
        id: "co-mark-read",
        method: "PATCH",
        path: "/api/notifications/read/{id}",
        summary: "Mark Notice as Read",
        description: "Updates citizen notice read status in PostgreSQL store.",
        service: "co-service",
        port: "8086",
        requiresAuth: true,
        headers: {},
        params: [{ key: "id", value: "1" }],
        mockResponse: {
          success: true,
          message: "Notice marked as read"
        }
      }
    ]
  },
  {
    category: "7. Analytics & System Telemetry Service",
    endpoints: [
      {
        id: "analytics-history",
        method: "GET",
        path: "/api/analytics/history",
        summary: "Get 24-Hour System Analytics Events",
        description: "Streams aggregated system throughput, processed claims per hour, and service latency metrics.",
        service: "analytics-service",
        port: "8088",
        requiresAuth: true,
        headers: {},
        mockResponse: [
          { eventType: "CLAIM_SUBMITTED", count: 184, period: "LAST_24_HOURS" },
          { eventType: "ELIGIBILITY_APPROVED", count: 162, period: "LAST_24_HOURS" },
          { eventType: "BENEFITS_DISBURSED", count: 162, period: "LAST_24_HOURS" }
        ]
      },
      {
        id: "analytics-logs",
        method: "GET",
        path: "/api/analytics/logs",
        summary: "Get Top 50 Distributed Audit Logs",
        description: "Fetches centralized security and business audit logs collected across all microservices.",
        service: "analytics-service",
        port: "8088",
        requiresAuth: true,
        headers: {},
        mockResponse: [
          { id: 91, serviceName: "ed-service", level: "INFO", message: "Rule Engine: AppId 1001 evaluated eligible for SNAP", timestamp: "2026-09-03T10:20:01" },
          { id: 90, serviceName: "ar-service", level: "INFO", message: "SSN verification completed for Sarah Connor", timestamp: "2026-09-03T10:15:05" }
        ]
      },
      {
        id: "analytics-record-log",
        method: "POST",
        path: "/api/analytics/logs",
        summary: "Ingest Microservice Telemetry Log",
        description: "Endpoint used by downstream microservices to ingest centralized distributed log entries.",
        service: "analytics-service",
        port: "8088",
        requiresAuth: true,
        headers: { "Content-Type": "application/json" },
        body: {
          serviceName: "dc-service",
          level: "INFO",
          message: "Income validation passed for Case 401"
        },
        mockResponse: {
          id: 92,
          serviceName: "dc-service",
          level: "INFO",
          message: "Income validation passed for Case 401",
          timestamp: new Date().toISOString()
        }
      }
    ]
  },
  {
    category: "8. Dashboard Aggregator (BFF Service)",
    endpoints: [
      {
        id: "dash-citizen",
        method: "GET",
        path: "/api/dashboard/citizen",
        summary: "Citizen BFF Dashboard Aggregator",
        description: "Aggregates applications, active benefits, and unread correspondence for the citizen in a single unified payload.",
        service: "dashboard-aggregator",
        port: "8087",
        requiresAuth: true,
        headers: { "X-User-Id": "1" },
        mockResponse: {
          success: true,
          message: "Citizen Dashboard Data fetched successfully",
          data: {
            totalApplications: 1,
            activeBenefitsCount: 1,
            monthlyBenefitTotal: 480.00,
            latestStatus: "APPROVED",
            unreadNotifications: 1
          }
        }
      },
      {
        id: "dash-caseworker",
        method: "GET",
        path: "/api/dashboard/caseworker",
        summary: "Caseworker BFF Dashboard Aggregator",
        description: "Aggregates review queues, pending determinations, and daily throughput metrics for case officers.",
        service: "dashboard-aggregator",
        port: "8087",
        requiresAuth: true,
        headers: {},
        mockResponse: {
          success: true,
          message: "Caseworker Dashboard Data fetched successfully",
          data: {
            pendingReviewCount: 14,
            processedToday: 29,
            avgResolutionTimeHours: 1.4,
            priorityAlerts: 2
          }
        }
      },
      {
        id: "dash-admin",
        method: "GET",
        path: "/api/dashboard/admin",
        summary: "Admin BFF Global Governance Overview",
        description: "System-wide overview: total citizens, overall disbursement amount, and microservices health indices.",
        service: "dashboard-aggregator",
        port: "8087",
        requiresAuth: true,
        headers: {},
        mockResponse: {
          success: true,
          message: "Admin dashboard data retrieved successfully",
          data: {
            totalRegisteredCitizens: 1350,
            totalDisbursedMonthToDate: 428900.00,
            systemHealthScore: 99.98,
            activeServicesCount: 11
          }
        }
      }
    ]
  },
  {
    category: "9. API Gateway & Actuator Infrastructure",
    endpoints: [
      {
        id: "gw-health",
        method: "GET",
        path: "/actuator/health",
        summary: "Spring Cloud Gateway Health Probe",
        description: "Returns UP/DOWN liveness state, disk space, and downstream Eureka/Database connectivity.",
        service: "api-gateway",
        port: "8080",
        requiresAuth: false,
        headers: {},
        mockResponse: {
          status: "UP",
          components: {
            discoveryComposite: { status: "UP", details: { eureka: { status: "UP" } } },
            diskSpace: { status: "UP", details: { free: 42949672960, threshold: 10485760 } },
            ping: { status: "UP" },
            reactiveDiscoveryClients: { status: "UP" }
          }
        }
      },
      {
        id: "gw-routes",
        method: "GET",
        path: "/actuator/gateway/routes",
        summary: "Active API Gateway Route Registry",
        description: "Returns dynamically registered Spring Cloud Gateway routes forwarding to Eureka microservices.",
        service: "api-gateway",
        port: "8080",
        requiresAuth: true,
        headers: {},
        mockResponse: [
          { route_id: "user-service", uri: "lb://user-service", predicate: "Paths: [/api/auth/**, /api/users/**]" },
          { route_id: "ar-service", uri: "lb://ar-service", predicate: "Paths: [/api/ar/**]" },
          { route_id: "dc-service", uri: "lb://dc-service", predicate: "Paths: [/api/dc/**]" },
          { route_id: "ed-service", uri: "lb://ed-service", predicate: "Paths: [/api/ed/**]" },
          { route_id: "bi-service", uri: "lb://bi-service", predicate: "Paths: [/api/bi/**]" },
          { route_id: "co-service", uri: "lb://co-service", predicate: "Paths: [/api/notifications/**]" },
          { route_id: "analytics-service", uri: "lb://analytics-service", predicate: "Paths: [/api/analytics/**]" },
          { route_id: "dashboard-aggregator", uri: "lb://dashboard-aggregator", predicate: "Paths: [/api/dashboard/**]" }
        ]
      },
      {
        id: "gw-metrics",
        method: "GET",
        path: "/actuator/prometheus",
        summary: "Prometheus Metrics Telemetry",
        description: "Exposes Micrometer HTTP latency histograms, JVM heap utilization, and circuit breaker states.",
        service: "api-gateway",
        port: "8080",
        requiresAuth: false,
        headers: {},
        mockResponse: "# HELP http_server_requests_seconds Duration of HTTP server requests\n# TYPE http_server_requests_seconds summary\nhttp_server_requests_seconds_count{uri=\"/api/ar/register\",status=\"200\"} 142.0\njvm_memory_used_bytes{area=\"heap\"} 1.4829184E8"
      }
    ]
  }
];

// App State
let currentEndpoint = API_CATALOG[0].endpoints[0];
let activeSnippetTab = "curl";

// DOM Init
document.addEventListener("DOMContentLoaded", () => {
  renderSidebar();
  selectEndpoint(currentEndpoint.id);
  setupEventListeners();
});

// Render Sidebar Service Navigation
function renderSidebar(filterQuery = "") {
  const container = document.getElementById("service-list");
  container.innerHTML = "";

  API_CATALOG.forEach(group => {
    const filteredEndpoints = group.endpoints.filter(ep => {
      const q = filterQuery.toLowerCase();
      return ep.path.toLowerCase().includes(q) || ep.summary.toLowerCase().includes(q) || ep.method.toLowerCase().includes(q) || (ep.service && ep.service.toLowerCase().includes(q));
    });

    if (filteredEndpoints.length === 0) return;

    const groupEl = document.createElement("div");
    groupEl.className = "service-group";

    const titleEl = document.createElement("div");
    titleEl.className = "service-group-title";
    titleEl.innerHTML = `<span>${group.category}</span> <span>${filteredEndpoints.length}</span>`;
    groupEl.appendChild(titleEl);

    filteredEndpoints.forEach(ep => {
      const item = document.createElement("div");
      item.className = `endpoint-item ${currentEndpoint && currentEndpoint.id === ep.id ? 'active' : ''}`;
      item.id = `nav-${ep.id}`;
      item.innerHTML = `
        <span class="badge-method badge-${ep.method.toLowerCase()}">${ep.method}</span>
        <span class="endpoint-path" title="${ep.path}">${ep.path}</span>
      `;
      item.addEventListener("click", () => selectEndpoint(ep.id));
      groupEl.appendChild(item);
    });

    container.appendChild(groupEl);
  });
}

// Select & Display Endpoint Details
function selectEndpoint(id) {
  let found = null;
  for (const group of API_CATALOG) {
    for (const ep of group.endpoints) {
      if (ep.id === id) {
        found = ep;
        break;
      }
    }
  }

  if (!found) return;
  currentEndpoint = found;

  // Update active item in sidebar
  document.querySelectorAll(".endpoint-item").forEach(el => el.classList.remove("active"));
  const activeNav = document.getElementById(`nav-${id}`);
  if (activeNav) activeNav.classList.add("active");

  // Populate Header
  const methodBadge = document.getElementById("selected-method-badge");
  methodBadge.className = `badge-method badge-${found.method.toLowerCase()}`;
  methodBadge.textContent = found.method;

  document.getElementById("selected-path-text").textContent = found.path;
  document.getElementById("selected-desc-text").textContent = found.description;
  document.getElementById("selected-service-tag").textContent = `Microservice: ${found.service} (Port ${found.port})`;

  // Parameters Section
  const paramsCard = document.getElementById("params-container");
  paramsCard.innerHTML = "";

  let hasParams = false;
  if (found.params && found.params.length > 0) {
    hasParams = true;
    found.params.forEach(p => {
      const group = document.createElement("div");
      group.className = "param-group";
      group.innerHTML = `
        <label>Path Parameter: <code>{${p.key}}</code></label>
        <input type="text" class="param-input path-param-input" data-param-key="${p.key}" value="${p.value}">
      `;
      paramsCard.appendChild(group);
    });
  }

  if (found.queryParams && found.queryParams.length > 0) {
    hasParams = true;
    found.queryParams.forEach(qp => {
      const group = document.createElement("div");
      group.className = "param-group";
      group.innerHTML = `
        <label>Query Parameter: <code>?${qp.key}=</code></label>
        <input type="text" class="param-input query-param-input" data-query-key="${qp.key}" value="${qp.value}">
      `;
      paramsCard.appendChild(group);
    });
  }

  if (!hasParams) {
    paramsCard.innerHTML = `<p style="font-size: 12px; color: var(--text-dim); margin-bottom: 12px;">No path or query parameters required for this endpoint.</p>`;
  }

  // Body Editor
  const bodyContainer = document.getElementById("body-editor-container");
  const bodyTextarea = document.getElementById("request-body-input");
  if (found.body) {
    bodyContainer.style.display = "block";
    bodyTextarea.value = JSON.stringify(found.body, null, 2);
  } else {
    bodyContainer.style.display = "none";
    bodyTextarea.value = "";
  }

  // Pre-fill Mock Response in viewer
  renderResponse({
    status: 200,
    statusText: "OK (Ready to test)",
    time: "0 ms",
    data: found.mockResponse
  });

  updateCodeSnippet();
}

// Update Dynamic Code Snippet (cURL, JS Fetch, Python)
function updateCodeSnippet() {
  const envUrl = getActiveBaseUrl();
  let fullPath = currentEndpoint.path;

  // Replace path params
  document.querySelectorAll(".path-param-input").forEach(inp => {
    const key = inp.getAttribute("data-param-key");
    fullPath = fullPath.replace(`{${key}}`, inp.value);
  });

  // Append query params
  const qParams = [];
  document.querySelectorAll(".query-param-input").forEach(inp => {
    const key = inp.getAttribute("data-query-key");
    if (inp.value) qParams.push(`${key}=${encodeURIComponent(inp.value)}`);
  });
  if (qParams.length > 0) {
    fullPath += `?${qParams.join("&")}`;
  }

  const fullUrl = `${envUrl}${fullPath}`;
  const token = document.getElementById("jwt-token-input").value.trim();
  const bodyText = document.getElementById("request-body-input").value.trim();

  let snippet = "";
  if (activeSnippetTab === "curl") {
    snippet = `curl -X ${currentEndpoint.method} "${fullUrl}" \\\n`;
    snippet += `  -H "Content-Type: application/json" \\\n`;
    if (token) {
      snippet += `  -H "Authorization: Bearer ${token.substring(0, 20)}..." \\\n`;
    }
    if (currentEndpoint.body && bodyText) {
      snippet += `  -d '${bodyText.replace(/\n\s*/g, " ")}'`;
    }
  } else if (activeSnippetTab === "fetch") {
    const headersObj = { "Content-Type": "application/json" };
    if (token) headersObj["Authorization"] = `Bearer ${token}`;

    snippet = `const response = await fetch("${fullUrl}", {\n`;
    snippet += `  method: "${currentEndpoint.method}",\n`;
    snippet += `  headers: ${JSON.stringify(headersObj, null, 4)},\n`;
    if (currentEndpoint.body && bodyText) {
      snippet += `  body: JSON.stringify(${bodyText})\n`;
    }
    snippet += `});\nconst result = await response.json();\nconsole.log(result);`;
  } else if (activeSnippetTab === "python") {
    snippet = `import requests\n\nurl = "${fullUrl}"\n`;
    snippet += `headers = {"Content-Type": "application/json"`;
    if (token) snippet += `, "Authorization": "Bearer ${token}"`;
    snippet += `}\n`;
    if (currentEndpoint.body && bodyText) {
      snippet += `payload = ${bodyText}\n`;
      snippet += `response = requests.${currentEndpoint.method.toLowerCase()}(url, json=payload, headers=headers)\n`;
    } else {
      snippet += `response = requests.${currentEndpoint.method.toLowerCase()}(url, headers=headers)\n`;
    }
    snippet += `print(response.json())`;
  }

  document.getElementById("code-snippet-viewer").textContent = snippet;
}

// Get Selected Base URL
function getActiveBaseUrl() {
  const env = document.getElementById("env-select").value;
  if (env === "live") return "http://localhost:8080";
  if (env === "render") return "https://claims-processing-gateway.onrender.com";
  return "https://api.his-claims-mock.internal";
}

// Execute Request (Mock vs Live)
async function executeRequest() {
  const env = document.getElementById("env-select").value;
  const sendBtn = document.getElementById("btn-send-request");
  sendBtn.innerHTML = `<span>Executing...</span>`;
  sendBtn.disabled = true;

  const startTime = performance.now();

  let resolvedPath = currentEndpoint.path;
  document.querySelectorAll(".path-param-input").forEach(inp => {
    const key = inp.getAttribute("data-param-key");
    resolvedPath = resolvedPath.replace(`{${key}}`, inp.value);
  });

  const queryParams = [];
  document.querySelectorAll(".query-param-input").forEach(inp => {
    const key = inp.getAttribute("data-query-key");
    if (inp.value) queryParams.push(`${key}=${encodeURIComponent(inp.value)}`);
  });
  if (queryParams.length > 0) resolvedPath += `?${queryParams.join("&")}`;

  const token = document.getElementById("jwt-token-input").value.trim();
  const bodyText = document.getElementById("request-body-input").value.trim();

  // If Mock Sandbox Mode
  if (env === "mock") {
    setTimeout(() => {
      const duration = Math.round(performance.now() - startTime + 75);
      
      // Auto-update token if this was signin
      if (currentEndpoint.id === "auth-signin" && currentEndpoint.mockResponse && currentEndpoint.mockResponse.data) {
        document.getElementById("jwt-token-input").value = currentEndpoint.mockResponse.data.token;
        showToast("JWT Token auto-injected into Authorization header!");
      }

      renderResponse({
        status: 200,
        statusText: "OK (Simulated Sandbox)",
        time: `${duration} ms`,
        data: currentEndpoint.mockResponse
      });

      sendBtn.innerHTML = `<span>▶ Send Request</span>`;
      sendBtn.disabled = false;
    }, 280);
    return;
  }

  // Live Mode: Real HTTP Fetch
  const targetUrl = `${getActiveBaseUrl()}${resolvedPath}`;
  const headers = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;

  try {
    const reqOptions = {
      method: currentEndpoint.method,
      headers: headers
    };
    if (currentEndpoint.body && bodyText) {
      reqOptions.body = bodyText;
    }

    const response = await fetch(targetUrl, reqOptions);
    const duration = Math.round(performance.now() - startTime);
    const json = await response.json().catch(() => ({ status: response.status, text: "Non-JSON response" }));

    if (currentEndpoint.id === "auth-signin" && json && json.data && json.data.token) {
      document.getElementById("jwt-token-input").value = json.data.token;
      showToast("Live JWT Token captured & saved for subsequent calls!");
    }

    renderResponse({
      status: response.status,
      statusText: response.statusText || (response.status === 200 ? "OK" : "Error"),
      time: `${duration} ms`,
      data: json
    });
  } catch (err) {
    const duration = Math.round(performance.now() - startTime);
    renderResponse({
      status: 0,
      statusText: "Network Error / Offline",
      time: `${duration} ms`,
      data: {
        error: "Connection Refused or CORS Restricted",
        hint: "Make sure Spring Boot API Gateway is running on localhost:8080, or switch to 'Mock Sandbox Mode' to explore simulated responses!",
        details: err.message
      }
    });
  } finally {
    sendBtn.innerHTML = `<span>▶ Send Request</span>`;
    sendBtn.disabled = false;
  }
}

// Render Response Box
function renderResponse({ status, statusText, time, data }) {
  const statusBadge = document.getElementById("resp-status-badge");
  statusBadge.textContent = `${status || 0} ${statusText}`;
  statusBadge.className = `status-badge ${status >= 200 && status < 300 ? 'status-2xx' : (status >= 400 && status < 500 ? 'status-4xx' : 'status-5xx')}`;

  document.getElementById("resp-time-badge").textContent = time;
  document.getElementById("response-json-viewer").textContent = typeof data === "string" ? data : JSON.stringify(data, null, 2);
}

// Toast Helper
function showToast(message) {
  const toast = document.getElementById("toast-msg");
  toast.textContent = message;
  toast.style.display = "block";
  setTimeout(() => {
    toast.style.display = "none";
  }, 3500);
}

// Event Listeners Setup
function setupEventListeners() {
  // Search Filter
  document.getElementById("search-endpoints").addEventListener("input", (e) => {
    renderSidebar(e.target.value);
  });

  // Top Nav View Tabs
  document.querySelectorAll(".nav-tab").forEach(tab => {
    tab.addEventListener("click", () => {
      document.querySelectorAll(".nav-tab").forEach(t => t.classList.remove("active"));
      document.querySelectorAll(".view-section").forEach(s => s.classList.remove("active"));

      tab.classList.add("active");
      const targetView = tab.getAttribute("data-target");
      document.getElementById(targetView).classList.add("active");
    });
  });

  // Snippet Tab switching
  document.querySelectorAll(".snippet-tab").forEach(tab => {
    tab.addEventListener("click", () => {
      document.querySelectorAll(".snippet-tab").forEach(t => t.classList.remove("active"));
      tab.classList.add("active");
      activeSnippetTab = tab.getAttribute("data-lang");
      updateCodeSnippet();
    });
  });

  // Execute Request Button
  document.getElementById("btn-send-request").addEventListener("click", executeRequest);

  // Payload / Param input changes
  document.getElementById("request-body-input").addEventListener("input", updateCodeSnippet);
  document.getElementById("jwt-token-input").addEventListener("input", updateCodeSnippet);
  document.getElementById("env-select").addEventListener("change", updateCodeSnippet);

  // Quick Auth Role Chips
  document.querySelectorAll(".auth-chip").forEach(chip => {
    chip.addEventListener("click", () => {
      const role = chip.getAttribute("data-role");
      const simulatedTokens = {
        admin: "eyJhbGciOiJIUzUxMiJ9.claims_admin_role_token_simulation",
        caseworker: "eyJhbGciOiJIUzUxMiJ9.claims_caseworker_role_token_simulation",
        citizen: "eyJhbGciOiJIUzUxMiJ9.claims_citizen_role_token_simulation"
      };
      document.getElementById("jwt-token-input").value = simulatedTokens[role] || "";
      showToast(`Loaded ${role.toUpperCase()} Bearer Token`);
      updateCodeSnippet();
    });
  });

  // Copy Snippet Button
  document.getElementById("btn-copy-snippet").addEventListener("click", () => {
    const code = document.getElementById("code-snippet-viewer").textContent;
    navigator.clipboard.writeText(code);
    showToast("Code snippet copied to clipboard!");
  });

  // Copy Resume Bullet Point (if button present)
  const resumeBtn = document.getElementById("btn-copy-resume");
  if (resumeBtn) {
    resumeBtn.addEventListener("click", () => {
      const resumeBullet = "• Engineered a distributed Healthcare Claims Processing System across 11 microservices using Spring Boot, Spring Cloud Gateway, Docker, PostgreSQL, Kafka, and Resilience4j; built an interactive Developer Portal for real-time API evaluation & automated eligibility rule validation.";
      navigator.clipboard.writeText(resumeBullet);
      showToast("Resume bullet point copied to clipboard!");
    });
  }

  // Download Postman Collection
  document.getElementById("btn-download-postman").addEventListener("click", () => {
    const link = document.createElement("a");
    link.href = "postman-collection.json";
    link.download = "Claims-Processing-System.postman_collection.json";
    link.click();
    showToast("Downloading Postman Collection...");
  });

  // Download OpenAPI Spec
  document.getElementById("btn-download-openapi").addEventListener("click", () => {
    const link = document.createElement("a");
    link.href = "openapi-spec.json";
    link.download = "claims-openapi-spec.json";
    link.click();
    showToast("Downloading OpenAPI 3.0 Specification...");
  });

  // Initialize ADR Engine
  initAdrEngine();
}

// --- Architectural Decision Records (ADRs) Catalog ---
const ADR_CATALOG = [
  {
    id: "ADR-001",
    title: "Transactional Outbox Pattern for Zero Dual-Write Inconsistency",
    status: "ACCEPTED",
    category: "Data",
    date: "2026-08-15",
    context: "When a citizen submits an application in AR Service, the system must persist the record to PostgreSQL AND dispatch an event to Kafka. A naive dual-write (save to DB then call kafka.send) risks partial failure if the broker is unreachable after database commit, resulting in lost events and desynchronized downstream eligibility processing.",
    alternatives: [
      { name: "Direct Dual-Write in Controller", pros: "Simple to write", cons: "Dual-write inconsistency on network crash or DB rollback", chosen: false },
      { name: "Two-Phase Commit (2PC / XA)", pros: "Strict ACID", cons: "High latency locks, Kafka lacks native XA support, poor horizontal scaling", chosen: false },
      { name: "Transactional Outbox Pattern", pros: "Guaranteed at-least-once delivery, DB and event table committed in a single local ACID transaction", cons: "Requires background publisher and idempotent consumers", chosen: true }
    ],
    decision: "Adopt the Transactional Outbox Pattern using an `outbox_events` table within the `his_ar` PostgreSQL database. Domain entities and outbox events are committed atomically. A CDC or polling relay publishes pending events to Kafka topic `applicationCreated-out-0`.",
    consequences: "Eliminates distributed dual-write inconsistency entirely. Downstream services (e.g., DC Service and ED Service) must maintain idempotent message handlers with unique event IDs.",
    codeReference: "com.his.ar.model.OutboxEvent & com.his.ar.controller.ArController (Lines 99-118)"
  },
  {
    id: "ADR-002",
    title: "Database-per-Service Architecture vs. Shared Relational Schema",
    status: "ACCEPTED",
    category: "Data",
    date: "2026-08-18",
    context: "11 microservices require persistent storage for Users, Claims, Household Income, Eligibility Results, Benefit Accounts, Correspondence, and Telemetry. A single shared database creates tight schema coupling, blast radius risks, and lock contention during high-throughput eligibility engine runs.",
    alternatives: [
      { name: "Shared PostgreSQL Monolithic DB", pros: "Easy SQL cross-table JOINs", cons: "Single point of failure, lock contention, tight deployment coupling", chosen: false },
      { name: "Database-per-Service (Isolated Schemas/Instances)", pros: "Complete domain boundary isolation, independent scaling & migrations, isolated blast radius", cons: "Requires API/Event communication instead of relational foreign keys", chosen: true }
    ],
    decision: "Enforce strict Database-per-Service isolation with dedicated PostgreSQL instances (`his_users`, `his_ar`, `his_dc`, `his_ed`, `his_bi`, `his_co`, `his_analytics`). No microservice is permitted direct SQL access to another service's tables.",
    consequences: "Cross-service validation (e.g., DC verifying Application existence) must occur via OpenFeign client contracts or asynchronous event streams.",
    codeReference: "docker-compose.yml (Services: postgres-user, postgres-ar, postgres-dc, postgres-ed, postgres-bi, postgres-co)"
  },
  {
    id: "ADR-003",
    title: "Edge JWT Validation at Spring Cloud Gateway vs Decentralized Verification",
    status: "ACCEPTED",
    category: "Security",
    date: "2026-08-20",
    context: "Clients transmit JWT tokens for role-based authentication (ADMIN, CASEWORKER, CITIZEN). Decrypting and validating cryptographic JWT signatures repeatedly inside each downstream service creates CPU overhead and redundant security filter code.",
    alternatives: [
      { name: "Decentralized JWT Parsing in Every Service", pros: "Fully autonomous verification", cons: "High duplicate CPU overhead, inconsistent security policies", chosen: false },
      { name: "Centralized Gateway Validation & Header Propagation", pros: "Single security checkpoint, unauthorized requests rejected at perimeter, downstream receives verified identity headers", cons: "Gateway must be highly available", chosen: true }
    ],
    decision: "Validate JWT signatures, expiration, and claims centrally at the Spring Cloud Gateway layer using JJWT. Validated user details and roles are forwarded downstream via trusted `X-User-Id` and `X-User-Roles` HTTP headers.",
    consequences: "Downstream microservices focus on business domain logic while relying on Gateway edge validation. Protected by network perimeter security.",
    codeReference: "api-gateway/pom.xml (jjwt-api 0.11.5) & com.his.gateway.ApiGatewayApplication"
  },
  {
    id: "ADR-004",
    title: "Asynchronous Kafka Event Streaming for Eligibility Engine Pipelines",
    status: "ACCEPTED",
    category: "Messaging",
    date: "2026-08-22",
    context: "Determining plan eligibility involves evaluating complex federal income guidelines, household composition, and medical deductions. Synchronous REST calls between Data Collection and Eligibility Engine cause thread starvation and cascading HTTP timeouts during traffic spikes.",
    alternatives: [
      { name: "Synchronous REST Call Chains (DC -> ED -> BI -> CO)", pros: "Immediate request-response cycle", cons: "Cascading timeouts, tight runtime coupling, unrecoverable upon network drops", chosen: false },
      { name: "Event-Driven Asynchronous Pipeline with Kafka", pros: "High-throughput buffering, backpressure resilience, independent service processing speed", cons: "Eventual consistency UI model", chosen: true }
    ],
    decision: "Adopt Apache Kafka with Spring Cloud Stream (`dataCaptured-out-0` topic). Completing data collection dispatches an event consumed asynchronously by the Eligibility Engine, which subsequently publishes approval events to Benefit Issuance.",
    consequences: "System achieves horizontal scalability and peak load smoothing. The citizen dashboard queries BFF aggregator for current state.",
    codeReference: "com.his.dc.controller.DcController (completeDataCollection with StreamBridge)"
  },
  {
    id: "ADR-005",
    title: "Resilience4j Circuit Breakers & Fallbacks vs Unbounded HTTP Retries",
    status: "ACCEPTED",
    category: "Resilience",
    date: "2026-08-25",
    context: "When high-load microservices (e.g. ED Engine or Database) experience temporary latency spikes, client retries trigger thundering herd problems, causing cascading outages across the entire cluster.",
    alternatives: [
      { name: "Unbounded Immediate HTTP Retries", pros: "Recovers quick transient blips", cons: "Overwhelms struggling services, triggers total cluster collapse", chosen: false },
      { name: "Resilience4j Circuit Breaker with Sliding Window & Fallback", pros: "Fails fast when error rate exceeds 50%, protects downstream services, provides graceful fallback responses", cons: "Requires fallback response handler design", chosen: true }
    ],
    decision: "Implement Resilience4j Circuit Breakers across Spring Cloud Gateway and OpenFeign clients with sliding error rate thresholds (50% failure rate over 10 calls trips the breaker into OPEN state).",
    consequences: "Guarantees system survivability under severe load with predictable, sub-second error responses and automatic transition back to HALF-OPEN state for recovery.",
    codeReference: "api-gateway/pom.xml (spring-cloud-starter-circuitbreaker-resilience4j)"
  },
  {
    id: "ADR-006",
    title: "Modular Strategy Rules Engine vs Heavyweight BRMS (Drools)",
    status: "ACCEPTED",
    category: "Data",
    date: "2026-08-28",
    context: "Eligibility determination requires evaluating income thresholds and criteria for SNAP, Medicaid, CCAP, and QHP. We evaluated integrating Drools BRMS vs building a modular Java Specification/Strategy Engine.",
    alternatives: [
      { name: "Drools BRMS Engine (KIE Workbench)", pros: "Rules written in DRL files", cons: "Massive memory footprint (>300MB extra heap per service), cold-start latency, complex debugging", chosen: false },
      { name: "Modular Java Strategy / Specification Engine", pros: "Sub-millisecond execution, zero external dependency bloat, 100% unit-testable, low memory usage (<30MB)", cons: "Rule changes require standard code deployment", chosen: true }
    ],
    decision: "Build a modular, deterministic rules engine using the Java Strategy & Specification pattern in `ed-service`. Each plan rule (SNAP Rule, Medicaid Rule) implements an isolated evaluation interface.",
    consequences: "Achieves microsecond-level rule execution throughput with minimal RAM usage, ideal for containerized microservices and cloud free-tier hosting.",
    codeReference: "com.his.ed.service.EligibilityEngine & com.his.ed.controller.EdController"
  }
];

let activeAdrId = "ADR-001";
let activeAdrFilter = "all";

// Initialize ADR Engine & UI
function initAdrEngine() {
  renderAdrList();
  renderAdrDetail(activeAdrId);

  // Setup ADR filter buttons
  document.querySelectorAll("[data-adr-filter]").forEach(btn => {
    btn.addEventListener("click", () => {
      document.querySelectorAll("[data-adr-filter]").forEach(b => b.classList.remove("active-filter"));
      btn.classList.add("active-filter");
      activeAdrFilter = btn.getAttribute("data-adr-filter");
      renderAdrList();
    });
  });
}

// Render ADR Sidebar Card List
function renderAdrList() {
  const container = document.getElementById("adr-list-container");
  if (!container) return;
  container.innerHTML = "";

  const filtered = ADR_CATALOG.filter(adr => {
    if (activeAdrFilter === "all") return true;
    return adr.category.toLowerCase().includes(activeAdrFilter.toLowerCase());
  });

  filtered.forEach(adr => {
    const card = document.createElement("div");
    card.className = `adr-card-item ${adr.id === activeAdrId ? 'active' : ''}`;
    card.innerHTML = `
      <div class="adr-card-top">
        <span class="adr-id-tag">${adr.id}</span>
        <span class="adr-status-badge status-2xx">${adr.status}</span>
      </div>
      <h4 class="adr-card-title">${adr.title}</h4>
      <div class="adr-card-meta">
        <span class="service-tag">${adr.category}</span>
        <span>${adr.date}</span>
      </div>
    `;
    card.addEventListener("click", () => {
      activeAdrId = adr.id;
      document.querySelectorAll(".adr-card-item").forEach(c => c.classList.remove("active"));
      card.classList.add("active");
      renderAdrDetail(adr.id);
    });
    container.appendChild(card);
  });
}

// Render Active ADR Full Specification Document
function renderAdrDetail(adrId) {
  const container = document.getElementById("adr-detail-container");
  if (!container) return;

  const adr = ADR_CATALOG.find(a => a.id === adrId);
  if (!adr) return;

  let alternativesHtml = "";
  adr.alternatives.forEach(alt => {
    alternativesHtml += `
      <tr class="${alt.chosen ? 'chosen-row' : ''}">
        <td><strong>${alt.name}</strong> ${alt.chosen ? '<span class="status-badge status-2xx" style="font-size: 10px; margin-left: 6px;">SELECTED</span>' : ''}</td>
        <td style="color: #34d399;">${alt.pros}</td>
        <td style="color: #f87171;">${alt.cons}</td>
      </tr>
    `;
  });

  container.innerHTML = `
    <div class="adr-doc-header">
      <div style="display: flex; gap: 8px; align-items: center; margin-bottom: 6px;">
        <span class="adr-id-tag" style="font-size: 13px;">${adr.id}</span>
        <span class="status-badge status-2xx">${adr.status}</span>
        <span class="service-tag">${adr.category}</span>
        <span style="font-size: 12px; color: var(--text-dim); margin-left: auto;">Date: ${adr.date}</span>
      </div>
      <h2 style="font-size: 18px; font-weight: 800; color: #fff; margin-bottom: 4px;">${adr.title}</h2>
    </div>

    <div class="adr-doc-section">
      <h4>🎯 Context & Problem Statement</h4>
      <p>${adr.context}</p>
    </div>

    <div class="adr-doc-section">
      <h4>⚖️ Evaluated Architecture Alternatives</h4>
      <table class="adr-table">
        <thead>
          <tr>
            <th>Architecture Approach</th>
            <th>Pros</th>
            <th>Cons</th>
          </tr>
        </thead>
        <tbody>
          ${alternativesHtml}
        </tbody>
      </table>
    </div>

    <div class="adr-doc-section">
      <h4>✅ Decision Outcome & Technical Rationale</h4>
      <div class="adr-decision-box">
        <p>${adr.decision}</p>
      </div>
    </div>

    <div class="adr-doc-section">
      <h4>⚠️ Managed Trade-offs & Consequences</h4>
      <p>${adr.consequences}</p>
    </div>

    <div class="adr-doc-section" style="margin-bottom: 0;">
      <h4>💻 Production Code Reference in Repository</h4>
      <code class="adr-code-ref">${adr.codeReference}</code>
    </div>
  `;
}
