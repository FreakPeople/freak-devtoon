-- 비속어 정책 테이블에 초기 데이터 삽입
-- 비속어 사용 경고 임계값 설정, 정책 적용 시작 및 종료 시간 지정
INSERT INTO `bad_words_policy` (
    `warning_threshold`,
    `start_date`,
    `end_date`,
    `created_at`,
    `updated_at`
) VALUES (
             3,  -- 경고 임계값: 누적 3회 경고시 조치
             '2024-06-01 00:00:00',  -- 정책 시작 날짜
             '2024-12-31 23:59:59',  -- 정책 종료 날짜
             CURRENT_TIMESTAMP,       -- 생성 시간
             CURRENT_TIMESTAMP        -- 업데이트 시간
         );

-- 쿠키 정책 테이블에 초기 데이터 삽입
-- 쿠키 가격과 에피소드당 쿠키 수량 설정, 정책 적용 시작 및 종료 시간 지정
INSERT INTO `cookie_policy` (
    `cookie_price`,
    `cookie_quantity_per_episode`,
    `start_date`,
    `end_date`,
    `created_at`,
    `updated_at`
) VALUES (
             200,  -- 쿠키 가격: 200
             2,    -- 에피소드당 쿠키 필요 수량: 2
             '2024-06-01 00:00:00',  -- 정책 시작 날짜
             '2024-12-31 23:59:59',  -- 정책 종료 날짜
             CURRENT_TIMESTAMP,       -- 생성 시간
             CURRENT_TIMESTAMP        -- 업데이트 시간
         );
