CREATE TABLE source (
    `log_line` STRING
) WITH (
    'connector' = 'kafka',
    'topic' = '${kafka.topic}'
    ${format-map: path=kafka.options, indent=4}
)
