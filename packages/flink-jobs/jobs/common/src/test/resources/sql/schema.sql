CREATE TABLE user_events_source (
    user_id BIGINT,
    event_type STRING
) WITH (
    'connector' = 'kafka',
    'topic' = '${kafka.topic}'
    ${format-map: path=kafka.options, indent=4}
);

CREATE TABLE print_sink (
    user_id BIGINT,
    event_type STRING
) WITH (
    'connector' = 'print',
    'print-identifier' = '[${job.name} | ${env}]'
);

CREATE TABLE iceberg_sink (
    user_id BIGINT,
    event_type STRING,
    ts TIMESTAMP(3)
) WITH (
    'connector' = 'iceberg',
    'catalog-name' = '${iceberg.catalog}',
    'catalog-type' = 'hive',
    'uri' = '${iceberg.uri}',
    'warehouse' = '${iceberg.warehouse}',
    'format-version' = '2'
);
