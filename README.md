# Kundtjänst

Detta projekt är en kundtjänst som hanterar kunddata och kommunicerar med bokningstjänsten.

Bokningstjänst: [Hisham-Abdoun/pensionat](https://github.com/Hisham-Abdoun/pensionat)

## Förutsättningar

- Docker och Docker Compose installerat
- Bokningstjänst-projektet (måste köras separat)

## Starta projektet

### 1. Skapa Docker-nätverk (görs bara en gång)

Båda projekten (kundtjänst och bokningstjänst) måste vara på samma Docker-nätverk för att kunna kommunicera med varandra. Skapa nätverket en gång:

```bash
docker network create app-network
```

### 2. Starta kundtjänsten

Navigera till kundtjänst-projektet och kör:

```bash
docker-compose up --build
```

### 3. Starta bokningstjänsten

Navigera till bokningstjänst-projektet och kör samma kommando:

```bash
docker-compose up --build
```

## Användning

När båda tjänsterna är startade kan du komma åt dem via:

- **Kundtjänst**: http://localhost:8081
- **Bokningstjänst**: http://localhost:8080

## Stoppa tjänsterna

För att stoppa tjänsterna, kör i respektive projekt:

```bash
docker-compose down
```

## Arkitektur

- Kundtjänsten körs på port 8081
- Bokningstjänsten körs på port 8080
- Båda tjänsterna använder MySQL-databaser
- Tjänsterna kommunicerar via Docker-nätverket `app-network`
