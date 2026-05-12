# Flower Catalog Frontend

React SPA for the catalog-service REST API.

## Run

Backend must be up first (port 8081 with Postgres on 5433):

```
docker compose up -d
./mvnw.cmd spring-boot:run
```

Then in this folder:

```
npm install
npm run dev
```

Open http://localhost:5173.

The dev server's origin (`http://localhost:5173`) is whitelisted by `WebConfig` in the Spring app — change both sides together if you move the port.