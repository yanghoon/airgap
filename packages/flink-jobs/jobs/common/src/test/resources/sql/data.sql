INSERT INTO print_sink
SELECT 
    user_id,
    event_type
FROM user_events_source
-- 필요한 경우 필터링 조건 추가
WHERE event_type IS NOT NULL;

INSERT INTO iceberg_sink
SELECT 
    user_id,
    UPPER(event_type) AS event_type, -- 간단한 데이터 전처리 예시
    PROCTIME() AS ts                 -- 처리 시간(Processing Time) 추가
FROM user_events_source
WHERE user_id > 0;
