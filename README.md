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

```bash
docker build -t formdump .
docker run formdump
```

### Manual

```bash
lein uberjar
java -jar target/uberjar/formdump-0.0.1-standalone.jar
```
