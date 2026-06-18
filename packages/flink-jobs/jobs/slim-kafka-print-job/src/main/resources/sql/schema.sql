CREATE TABLE kafka (
    `log_line` STRING
) WITH (
    'connector' = 'kafka',
    'topic' = '${kafka.topic}'
    ${format-map: path=kafka.options, indent=4}
);

CREATE TABLE iceberg (
    `log_line` STRING,
    `insert_datetime` TIMESTAMP
) WITH (
    'connector' = 'iceberg',
    'catalog-type' = 'rest'
    ${format-map: path=iceberg.options, indent=4}
);

CREATE TABLE print (
    `log_line` STRING,
    `insert_datetime` TIMESTAMP
) WITH (
    'connector' = 'print'
);
