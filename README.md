# Virtual Library Web App

A full‑stack virtual library where users can register, search for books by title, author, or genre using the Google Books API, and save them into personal reading lists.[web:1][web:3]

## Stack

- **Backend:** Java 17, Spring Boot (Web, Security, Data JPA, Validation), PostgreSQL, Maven.[file:30]
- **Frontend:** Next.js (React, TypeScript), Tailwind CSS (or similar).
- **External:** Google Books API for book search and metadata.[web:1][web:6]
- **Quality:** Checkstyle, SpotBugs, Maven `test`/`verify`.

## Features

- JWT‑based user registration and login.
- Search books by title, author, or genre (mapped to `intitle:`, `inauthor:`, `subject:` in Google Books `/volumes` endpoint).[web:1][web:6]
- Add books from Google Books into a personal library with reading status (To Read, Reading, Completed, Favourite), rating, and notes.
- PostgreSQL persistence for users, cached book metadata, and user–book associations.

## Running Locally

### Without Docker

**Backend**

1. Configure `application.yml` with your PostgreSQL URL and credentials.
2. Add your Google Books API key:
   ```yaml
   google:
     books:
       api-key: YOUR_GOOGLE_BOOKS_API_KEY
   ```
3. From `backend/`:
   ```bash
   mvn clean verify
   mvn spring-boot:run
   ```

**Frontend**

From `frontend/`:

```bash
npm install
npm run dev
```

- Backend: `http://localhost:8080`
- Frontend: `http://localhost:3000`
- Set `NEXT_PUBLIC_API_BASE_URL=http://localhost:8080` in `frontend/.env.local`.

## Dockerisation

The project is designed to run via **Docker Compose** with three services:

- `backend`: Spring Boot app built from `backend/Dockerfile`, exposing port `8080`.
- `frontend`: Next.js app built from `frontend/Dockerfile`, exposing port `3000`.
- `db`: PostgreSQL container with a `VirtualLibrary` database.

Example `docker-compose.yml` (simplified):

```yaml
services:
  db:
    image: postgres:16
    environment:
      POSTGRES_DB: VirtualLibrary
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"

  backend:
    build: ./backend
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/VirtualLibrary
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      GOOGLE_BOOKS_API_KEY: your_key_here
    depends_on:
      - db
    ports:
      - "8080:8080"

  frontend:
    build: ./frontend
    environment:
      NEXT_PUBLIC_API_BASE_URL: http://backend:8080
    depends_on:
      - backend
    ports:
      - "3000:3000"
```

Then from the project root:

```bash
docker compose up --build
```

## Quality Checks

From `backend/`:

```bash
# Run tests + Checkstyle + SpotBugs
mvn clean verify
```

Add frontend tests (e.g. `npm test`) as needed in your CI pipeline.
