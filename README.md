# Instagram Hybrid Comment Collector

This project implements a hybrid approach to collect Instagram comments:
- Real-time via Webhook (POST /webhook/instagram)
- Historical and missed comments via Polling (scheduled job)

## How to use

1. Edit `src/main/resources/application.yml` and set:
   - instagram.verify-token
   - instagram.access-token (long-lived)
   - instagram.ig-user-id

2. Configure your database (Postgres) connection in `spring.datasource` in application.yml.

3. Run Postgres locally or provide a connection.

4. Start the app:
   ```
   mvn spring-boot:run
   ```

5. Expose your webhook to the internet using ngrok:
   ```
   ngrok http 8080
   ```
   Use the ngrok HTTPS URL as your Facebook App Callback URL:
   `https://<ngrok-url>/webhook/instagram`

6. In Meta Developer Portal:
   - Subscribe to **instagram** webhooks
   - Select field **comments** (and optionally live_comments)
   - Use the verify token from application.yml

7. Start a real comment on a post of the linked IG Business account, or wait for polling to fetch existing comments.

## Notes
- Polling runs every 10 minutes by default. Configure via `instagram.polling.delay-ms`.
- Polling uses Graph API v19.0; update if necessary.
