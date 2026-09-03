# 🚀 Deployment Guide & Resume Showcase Guide

This guide details how to deploy the **Developer Portal & Interactive API Hub** online for **100% free**, and what links/bullet points to put on your resume.

---

## 1. Deploying the Developer Portal on GitHub Pages (100% Free, Zero Cost)

Because the Developer Portal is built with modern HTML/CSS/Vanilla JS and includes a **Built-in Mock Sandbox Engine**, you can host it on GitHub Pages so recruiters can **always** interact with your API Hub 24/7 without running out of server credits.

### Steps:
1. Commit and push the project to your GitHub repository:
   ```bash
   git add .
   git commit -m "feat: add single developer portal and interactive API hub"
   git push origin main
   ```
2. Go to your GitHub Repository $\rightarrow$ **Settings** $\rightarrow$ **Pages** (on the left sidebar).
3. Under **Build and deployment**:
   - **Source**: `Deploy from a branch`
   - **Branch**: `main`
   - **Folder**: `/docs` (or `/docs/developer-portal`)
   - Click **Save**.
4. In ~60 seconds, GitHub will give you a live URL:
   `https://<your-username>.github.io/Insurance-Eligibility-Claims-Processing-System/developer-portal/`

---

## 2. Deploying the Spring Boot Backend on Render (Free Tier)

If you also want to host the live Spring Boot API Gateway and services:

1. Create a free account at [Render.com](https://render.com/).
2. Click **New +** $\rightarrow$ **Web Service**.
3. Connect your GitHub repository: `Claims-Processing-System`.
4. Choose **Docker** environment or build with Maven:
   - **Root Directory**: `api-gateway` (or root with Dockerfile)
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/api-gateway-1.0.0.jar`
   - **Instance Type**: **Free (512MB RAM)**
5. Render will assign you a live HTTPS domain (e.g. `https://his-claims-gateway.onrender.com`).
6. In the Developer Portal, recruiters can switch between **Mock Sandbox** and your **Live Render Cloud Gateway** seamlessly!

---

## 3. Exact Links to Put on Your Resume

When adding this project to your resume, format it cleanly with direct links:

### 📄 Recommended Resume Format

```markdown
Health Insurance Claims Processing System | Java, Spring Boot, Microservices, Docker, Kafka, PostgreSQL
Developer Portal: https://<your-username>.github.io/Insurance-Eligibility-Claims-Processing-System/developer-portal/
GitHub: https://github.com/<your-username>/Insurance-Eligibility-Claims-Processing-System
Postman Collection: [Download via Developer Portal]

• Architected an enterprise-grade Claims Processing System across 8 microservices (Spring Cloud Gateway, Eureka, Resilience4j, Kafka, PostgreSQL).
• Engineered a centralized Developer Portal & API Hub featuring interactive "Try It Out" consoles, JWT token authentication simulations, and automated cURL/Fetch snippet generators.
• Implemented Transactional Outbox pattern in AR Service to guarantee atomicity between PostgreSQL database commits and Kafka event publishing.
• Designed a high-throughput Eligibility Rules Engine evaluating income thresholds and dependent criteria for social assistance programs (SNAP, Medicaid, CCAP).
```

---

## 4. What Makes This Stand Out to Hiring Managers

1. **Zero Downtime Experience**: Even if cloud free-tier servers sleep after 15 minutes of inactivity, your Developer Portal instantly responds with high-fidelity simulated responses via the **Mock Sandbox Mode**.
2. **Interactive Proof of Backend Skills**: Recruiters can test endpoints, view request headers, copy cURL commands, and download Postman collections with a single click.
3. **Comprehensive System Architecture**: Displays interactive microservices topologies and end-to-end claim lifecycle sequence diagrams right inside the portal.
