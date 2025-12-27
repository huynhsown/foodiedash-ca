## FoodieDash CA

Backend Spring Boot (Java 21) theo Clean Architecture.

### Local dev (PostgreSQL + Elasticsearch)

- **Start services**:

```bash
docker compose up -d postgres elasticsearch
```

- **Run app**:

```bash
mvn -DskipTests spring-boot:run
```

### Search engine switch (rollback-friendly)

App hỗ trợ chuyển search engine bằng property `search.engine`:

- **Elasticsearch (mặc định)**:
  - `search.engine=elasticsearch`
  - `elasticsearch.host=http://localhost:9200`

- **Meilisearch (rollback)**:
  - `search.engine=meilisearch`
  - `meilisearch.host=http://localhost:7700`

### Full reindex (Elasticsearch)

Sau khi service chạy và Elasticsearch healthy:

- `POST /api/v1/restaurants/search/reindex`

### Observability (Actuator)

- `GET /actuator/health`
- `GET /actuator/metrics`
- `GET /actuator/prometheus`

### Runbooks

- DB cutover MySQL → PostgreSQL: `docs/runbooks/postgresql-cutover.md`
- Search cutover Meilisearch → Elasticsearch: `docs/runbooks/search-cutover.md`

### Notes

- Repo hiện có `src/main/resources/application.properties` dùng cho local. Với môi trường thật, nên chuyển secrets/config sang biến môi trường hoặc secret manager.

