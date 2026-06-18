CREATE TABLE iceberg (
    `log_line` STRING,
    `insert_datetime` as CURRENT_TIMESTAMP
) WITH (
    'connector' = 'iceberg',
    'catalog-type' = 'rest'
    ${format-map: path=iceberg.options, indent=4}
)
