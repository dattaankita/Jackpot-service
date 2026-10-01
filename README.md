# Jackpot Service

Spring Boot 3 / Java 17, H2 in-memory. Requires JDK 17+ and Maven.

```bash
mvn test
mvn spring-boot:run
```

## Kafka
Default is **mock mode** (`jackpot.kafka.enabled=false`): the producer logs the payload for `jackpot-bets`, then processes the bet via the same service the consumer uses, so the whole flow works without a broker.

Real Kafka: start a broker on localhost:9092 (e.g. `docker run -p 9092:9092 apache/kafka:3.8.0`) and run
`mvn spring-boot:run -Dspring-boot.run.arguments=--jackpot.kafka.enabled=true`

## API
Seeded jackpots (initial pool 1000): `JP-FIXED` (5% contribution, 2% win chance) and `JP-VARIABLE` (10% contribution falling 1 point per 1000 pool growth, floor 2%; win chance 1% rising linearly to 100% at pool 5000).

```bash
# publish a bet (202)
curl -X POST localhost:8080/api/bets -H 'Content-Type: application/json' \
  -d '{"betId":"bet-1","userId":"user-1","jackpotId":"JP-FIXED","betAmount":100}'

# evaluate a contributing bet (rolled once; repeat calls return the stored result)
curl -X POST localhost:8080/api/bets/bet-1/evaluate
```
Swagger UI (try the APIs, see request/response schemas): http://localhost:8080/swagger-ui.html (OpenAPI JSON at `/v3/api-docs`).

Inspect tables at `/h2-console` (JDBC URL `jdbc:h2:mem:jackpotdb`).

## Design
- `ContributionStrategy` / `RewardStrategy`: Fixed and Variable implementations as Spring beans keyed by enum. A new option = one enum value + one class.
- `BetProcessingService`: contribute, then evaluate. A win pays the whole pool and resets it to the initial value.
- Jackpot row is locked during updates; duplicate bet ids are ignored.

## Not done
Jackpot create/update API, dead-letter topic, embedded-Kafka integration test.
