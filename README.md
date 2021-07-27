# formdump

dumps form field input into a CSV and optionally to a Microsoft Forms spreadsheet

## Development

You need [Leiningen](https://leiningen.org/) and Java/OpenJDK installed.

```bash
lein ring server
```

## Deployment

Note that data is written to `./data.csv`.

### Docker

#### Docker Hub

There is an image on Docker Hub labelled [`hueyy/formdump`](https://hub.docker.com/r/hueyy/formdump).

```bash
docker run -p 8000:8000 hueyy/formdump
```

#### Build from source

```bash
docker build -t formdump .
docker run formdump
```

If you're using a volume you probably want to set the `OUTPUT_FILE` environment variable to a path within your volume, e.g. `/data/data.csv`.

### Manual

```bash
lein uberjar
java -jar target/uberjar/formdump-0.0.1-standalone.jar
```
