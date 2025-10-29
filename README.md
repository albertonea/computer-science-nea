# Running the Application

## Docker Deployment

From the root directory, build and start all services:

```
docker compose up --build
```

## Backend (Spring Boot)

From the root directory, run the application using Gradle:

```
./gradlew bootRun
```

This starts the backend on the default port.

## Frontend (Node.js)

1. Navigate to the frontend directory:

```
cd modules/front-end
```

2. Install dependencies:

```
npm install
```

3. Start the development server:

```
npm run dev
```


The frontend will be available at `http://localhost:5173` (or configured port), with hot-reloading for changes.[^13]

