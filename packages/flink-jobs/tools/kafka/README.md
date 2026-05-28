# Kafka (Redpanda)

## Usage

```bash
docker-compose up -d
```

## Create Cert

```bash
mkdir -p certs
cd certs

openssl req -x509 -newkey rsa:2048 -days 365 -nodes \
  -keyout server.key -out server.crt \
  -subj "/C=KR/ST=Seoul/L=Seoul/O=TestOrg/CN=localhost"

chmod 644 server.key
```
