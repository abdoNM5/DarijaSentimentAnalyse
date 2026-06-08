# Twitter Sentiment Analyzer

A full-stack, real-time **sentiment analysis** platform built with **Java 17**, **Spring Boot 3**, and **React 18**. The application streams tweets, processes them through Stanford CoreNLP for natural language processing, and displays live sentiment visualizations through a premium dark-mode dashboard.

Built as a demonstration of modern **full-stack Java development** with enterprise-grade architecture patterns including **REST APIs**, **reactive streams (WebFlux)**, and a **React** single-page application frontend.

---

## 🌟 Key Features

- **Real-Time Streaming via REST API**: Server-Sent Events (SSE) endpoint built with **Spring Boot WebFlux** for reactive, non-blocking data streaming.
- **NLP Sentiment Analysis**: Uses Stanford CoreNLP to classify text into 5 sentiment categories (Very Negative → Very Positive).
- **Live Dashboard**: **React 18** frontend with real-time updating Highcharts doughnut visualizations.
- **Premium UI**: Sleek dark-mode glassmorphism interface with micro-animations, built with modern CSS3.
- **Demo Mode**: Works out of the box with realistic demo data — no external API keys required.

---

## 🛠️ Technology Stack

### Backend — Java + Spring Boot
| Technology | Purpose |
|---|---|
| **Java 17** | Core language (LTS release, enterprise standard) |
| **Spring Boot 3.2** | Application framework with auto-configuration |
| **Spring Web** | **REST API** controllers with `@RestController` |
| **Spring WebFlux** | Reactive streaming with `Flux<T>` for SSE endpoints |
| **Spring Security** *(ready)* | CORS configuration, extensible for **JWT** authentication |
| **Stanford CoreNLP** | NLP pipeline: tokenization, POS tagging, parsing, sentiment |
| **Maven** | Build tool and dependency management |

### Frontend — React
| Technology | Purpose |
|---|---|
| **React 18** | Component-based UI with functional components & hooks |
| **TypeScript** | Type-safe development |
| **Vite** | Next-gen build tooling (replaces Webpack/CRA) |
| **Highcharts** | Interactive data visualizations (doughnut chart) |
| **Lucide React** | Modern icon library |

### DevOps & Tooling
| Technology | Purpose |
|---|---|
| **Git / GitHub** | Version control and collaboration |
| **Docker** *(ready)* | Containerization support for both backend and frontend |
| **Maven Wrapper** | Reproducible builds without pre-installed Maven |
| **npm** | Frontend package management |

---

## 🏗️ Architecture

```
┌──────────────────────────────┐       ┌──────────────────────────────┐
│       React 18 Frontend      │       │    Spring Boot 3 Backend     │
│                              │       │                              │
│  ┌────────────────────────┐  │  SSE  │  ┌────────────────────────┐  │
│  │   App.tsx (Dashboard)  │──┼───────┼──│  TweetsResource (REST) │  │
│  └────────────────────────┘  │       │  └───────────┬────────────┘  │
│  ┌────────────────────────┐  │       │              │               │
│  │  DoughnutChart (Live)  │  │       │  ┌───────────▼────────────┐  │
│  └────────────────────────┘  │       │  │   DemoService /        │  │
│                              │       │  │   TwitterService       │  │
│  Vite Dev Server :5173       │       │  └───────────┬────────────┘  │
└──────────────────────────────┘       │              │               │
                                       │  ┌───────────▼────────────┐  │
                                       │  │  SentimentAnalyzer     │  │
                                       │  │  (Stanford CoreNLP)    │  │
                                       │  └────────────────────────┘  │
                                       │                              │
                                       │  Tomcat Server :8080         │
                                       └──────────────────────────────┘
```

The backend exposes two **REST API** endpoints using **Spring Boot**:

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/search/{keyword}/{count}` | Fetch & analyze a batch of tweets (SSE stream) |
| `GET` | `/stream/{keyword}` | Live-stream analyzed tweets in real-time (SSE) |

Both endpoints return `Flux<TwitterStatus>` using **Spring WebFlux** for reactive, non-blocking responses streamed as `text/event-stream`.

---

## 🚀 Getting Started

### Prerequisites
- **Java 17** (LTS) — [Download](https://adoptium.net/)
- **Node.js 18+** and **npm** — [Download](https://nodejs.org/)
- **Git** — [Download](https://git-scm.com/)

### 1. Clone the Repository
```bash
git clone https://github.com/abdoNM5/DarijaSentimentAnalyse.git
cd DarijaSentimentAnalyse
```

### 2. Start the Backend (Spring Boot)
```bash
cd backend
./mvnw spring-boot:run        # Linux/Mac
.\mvnw.cmd spring-boot:run    # Windows
```
Wait for: `Started SentimentanalysisApplication`

### 3. Start the Frontend (React)
Open a **new terminal**:
```bash
cd frontend
npm install
npm run dev
```
Open `http://localhost:5173` in your browser.

### 4. Use the App
1. Type a keyword (e.g., "Morocco", "AI", "Football").
2. Click **Search Past** → fetches and analyzes a batch of tweets.
3. Click **Live Stream** → tweets arrive every 2 seconds in real-time.
4. Watch the sentiment doughnut chart update live!

---

## 🐳 Docker Support

### Backend
```dockerfile
FROM eclipse-temurin:17-jdk-alpine
COPY backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Frontend
```dockerfile
FROM node:18-alpine AS build
WORKDIR /app
COPY frontend/ .
RUN npm install && npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
```

Build and run with **Docker**:
```bash
# Backend
cd backend && ./mvnw clean package -DskipTests
docker build -t sentiment-backend .

# Frontend
cd frontend
docker build -t sentiment-frontend .
```

---

## 📁 Project Structure
```
├── backend/                              # Spring Boot 3 Backend
│   ├── src/main/java/
│   │   ├── controllers/
│   │   │   └── TweetsResource.java       # REST API endpoints (SSE)
│   │   ├── services/
│   │   │   ├── SentimentAnalyzerService  # Stanford CoreNLP integration
│   │   │   ├── DemoService.java          # Demo tweet generator
│   │   │   └── TwitterService.java       # Twitter API integration
│   │   ├── model/
│   │   │   ├── TwitterStatus.java        # Tweet data model
│   │   │   └── SentimentType.java        # Sentiment enum
│   │   └── config/
│   │       ├── TwitterConfig.java        # Twitter4J configuration
│   │       └── TwitterProperties.java    # Spring Boot properties binding
│   ├── src/main/resources/
│   │   └── application.yaml              # Spring Boot configuration
│   └── pom.xml                           # Maven dependencies
│
├── frontend/                             # React 18 + Vite Frontend
│   ├── src/
│   │   ├── App.tsx                       # Main dashboard component
│   │   ├── components/
│   │   │   └── DoughnutChart.tsx          # Highcharts sentiment chart
│   │   ├── model/
│   │   │   └── Tweet.ts                  # TypeScript interface
│   │   └── index.css                     # Premium dark-mode styles
│   ├── package.json
│   └── vite.config.ts
│
└── README.md
```

---

## 🔮 Roadmap / Future Improvements

- [ ] **Spring Security + JWT Authentication**: Add user login and role-based access control using **Spring Security** with **JWT** tokens.
- [ ] **Database Persistence**: Store analyzed tweets in **PostgreSQL** or **MongoDB** for historical analysis.
- [ ] **Docker Compose**: Full **Docker** orchestration for one-command deployment.
- [ ] **CI/CD Pipeline**: Automated build, test, and deploy with **GitHub Actions**.
- [ ] **Message Queue**: Decouple tweet ingestion using **Kafka** or **RabbitMQ** for scalable processing.
- [ ] **Microservices Architecture**: Split into independent services using **Spring Cloud** (API Gateway, Service Discovery).
- [ ] **GraphQL API**: Add a **GraphQL** endpoint alongside REST for flexible data querying.
- [ ] **Kubernetes Deployment**: Helm charts for **Kubernetes** orchestration.
- [ ] **Angular Frontend**: Alternative **Angular** dashboard for the same backend API.

---

## 🔧 Optional: Connect to Real Twitter/X API

To use real tweets instead of demo data, you need a [Twitter Developer Account](https://developer.twitter.com) with a paid plan. Update `backend/src/main/resources/application.yaml` with your API keys:

```yaml
twitter:
  consumer-key: YOUR_API_KEY
  consumer-secret: YOUR_API_SECRET
  access-token: YOUR_ACCESS_TOKEN
  access-token-secret: YOUR_ACCESS_TOKEN_SECRET
```

Then swap `DemoService` for `TwitterService` in `TweetsResource.java`.

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
