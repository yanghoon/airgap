INSERT INTO sink SELECT * FROM source;
-- INSERT INTO sink SELECT CONCAT(log_line, ' !!!!') FROM source;

INSERT INTO iceberg
SELECT log_line, CURRENT_TIMESTAMP
  FROM source;