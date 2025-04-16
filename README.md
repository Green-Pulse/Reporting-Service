# Reporting Service

The **Reporting Service** is responsible for generating weekly weather reports in **PDF format**.  
It fetches historical weather data from Redis (collected by the Data Ingestion Service) and compiles it into a readable, downloadable document.

---

## Features

-  Weekly report generation based on stored weather data
-  Reads from Redis using `weather-data:{city}:{timestamp}` key pattern
-  PDF generation using [Apache PDFBox](https://pdfbox.apache.org/)
-  Easily integratable with notification/email services (e.g., send report every week)

---

##  Technologies Used

- Java 21  
- Spring Boot  
- Redis  
- Apache PDFBox  
- Lombok  
- Gradle  
- Docker (production use) //TODO

---

##  How It Works

1. Weather data is collected every minute and stored by the Data Ingestion Service into Redis using a key
2. This service scans all such keys for the requested city and generates a PDF report.

---

##  Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/reports/weekly/{city}` | Generates a weekly PDF report for a given city |

---

# Redis Config
spring.data.redis.host=localhost
spring.data.redis.port=6379

---

##  TODOs

- [x]  **Read data from Redis by key pattern**  
  Retrieve historical weather data using `weather-data:{city}:{timestamp}` structure.

- [x]  **Group data by city and time**  
  Organize data in-memory to generate accurate time-based reports per city.

- [x]  **Generate PDF reports using PDFBox**  


- [x]  **Add automatic weekly scheduler**  
  Automatically trigger report generation every Sunday at midnight using Spring `@Scheduled(cron = "...")`.

---

- [ ]  **Send PDF via email or Telegram**  
  Integrate with SMTP or Telegram Bot API to deliver weekly reports to subscribed users.

- [ ]  **Save reports to AWS S3 or local file server**  
  Configure optional cloud storage or volume-mapped location to store generated reports.

- [ ]  **Add charts/visuals using PDFBox Graphics2D**  
  Add simple graphs (temperature trends, humidity levels, etc.) to improve report readability.

- [ ]  **Add unit & integration tests**  
  Test PDF generation logic, Redis data access, and endpoint behavior.

- [ ]  **Add support for multiple cities dynamically**  
  Generate and store reports per city with dynamic city parameter.

- [ ]  **Secure endpoints with role-based access (e.g., ADMIN only)**  
  Restrict access to report endpoints via Spring Security and JWT-based role validation.

- [ ]  **Allow file download through endpoint `/api/reports/download?file=...`**  
  Enable clients to download reports on demand using file name as a query param.
