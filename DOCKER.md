# Run Splendor with Docker

The Docker image runs the Java 21 API. The browser client is deployed separately
on Vercel. No frontend build, database, or host-specific configuration is required
for the API image.

## Build and run

Run these commands from the repository root (the directory containing `client/`
and `server/`), with Docker running:

```sh
docker build -f server/Dockerfile -t splendor .
docker run --rm -p 8080:8080 splendor
```

The API listens on `http://localhost:8080`. The same image can run on any compatible Linux
Docker host. It runs as a non-root user and writes logs to standard output/error.

To change the listening port, set `PORT` and adjust the port mapping:

```sh
docker run --rm -e PORT=9000 -p 9000:9000 splendor
```

The server binds to `0.0.0.0`. `EXPOSE 8080` documents the default port; it does
not prevent listening on another port when `PORT` is set.

## Configuration

| Setting | Default | Purpose |
| --- | --- | --- |
| `PORT` | `8080` | HTTP listening port. Hosting platforms can override it. |
| `JAVA_TOOL_OPTIONS` | `-XX:MaxRAMPercentage=60.0 -XX:+ExitOnOutOfMemoryError` in Docker | JVM settings; limits heap relative to available container memory. |

An optional classpath `config.properties` still supports `server.port` and
`spec.url` for local workflows. `PORT` takes precedence over `server.port`. The
default specification is bundled as `splendor-api-spec.yaml`. Local
configuration files are excluded from the Docker build context.

API routes such as `/games` are at the root, without an `/api` prefix. The API
does not serve a homepage.

## Connect the Vercel client

After deploying the API, update `getAPIUrl()` in
`client/src/assets/js/config.js` to return the backend's public HTTPS URL, for
example `https://your-backend.onrender.com` (without a trailing slash or `/api`).
Redeploy the client on Vercel so it uses the new URL. For local development, use
the existing `LOCALSERVER` constant instead.

The client's original API URL is preserved until you have the new backend URL.
The server's existing CORS handler supports cross-origin requests, including the
`X-Group-Secret` and `Authorization` headers used by the client.

## Render web service example

In Render, create a **Web Service**, connect the repository, and choose **Docker**
as the language/runtime. Use these settings:

| Render setting | Value |
| --- | --- |
| Root Directory | Leave empty |
| Dockerfile Path | `./server/Dockerfile` |
| Docker Build Context Directory | `.` (repository root) |
| Docker Command | Leave empty to use the image's default command |

Render supplies `PORT`; the application uses that value automatically. Use the
service's assigned HTTPS URL in the Vercel client's configuration. No Blueprint file is needed.
See Render's [Docker deployment guide](https://render.com/docs/docker).

## Run without Docker

Install Java 21, then run these commands from `server/`:

```sh
./gradlew test :server:shadowJar
java -jar server/build/libs/server-1.0-SNAPSHOT-fat.jar
```

On Windows use `./gradlew.bat`. The API listens on `http://localhost:8080`.

## Application state

Games live in process memory. Run one instance: multiple instances would have separate game lists. Persistent games or horizontal scaling require changes to the application's storage. Browser profiles remain in each browser's local storage.
