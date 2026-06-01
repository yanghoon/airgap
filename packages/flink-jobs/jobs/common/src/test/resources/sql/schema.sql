CREATE TABLE user_events_source (
    user_id BIGINT,
    event_type STRING
) WITH (
    'connector' = 'kafka',
    'topic' = '${app.vars.kafka.topic}'
    
    -- 커스텀 매크로를 통한 동적 옵션 및 들여쓰기 자동 렌더링
    ${format-map: path=app.vars.kafka.options, indent=4}
);

CREATE TABLE print_sink (
    user_id BIGINT,
    event_type STRING
) WITH (
    'connector' = 'print',
    'print-identifier' = '[${app.job.name} | ${app.env}]'
);

CREATE TABLE iceberg_sink (
    user_id BIGINT,
    event_type STRING,
    ts TIMESTAMP(3)
) WITH (
    'connector' = 'iceberg',
    'catalog-name' = '${app.vars.iceberg.catalog}',
    'catalog-type' = 'hive',
    'uri' = '${app.vars.iceberg.uri}',
    'warehouse' = '${app.vars.iceberg.warehouse}',
    'format-version' = '2'
);
