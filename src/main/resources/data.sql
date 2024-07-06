-- 권한(역할) 초기 데이터 삽입
INSERT INTO authority (authority_name)
VALUES ('ADMIN'), ('MEMBER');

-- 회원 초기 데이터 삽입
INSERT INTO member (member_no, email, name, password, membership_status, created_at, deleted_at)
VALUES (1, 'email_1@naver.com','회원1', '$2a$10$57KKBJtMH.oJbOB97htDVO2DakBvfzZALHpbQ3er7k/BNFX.GKMMW', 'GENERAL', '2024-07-06 16:26:03.835477', null);

INSERT INTO member (member_no, email, name, password, membership_status, created_at, deleted_at)
VALUES (2, 'email_2@naver.com','회원2', '$2a$10$nReEDQ9T/eURLvTbgneaBu3hiRFo/l09C5WdLc4qO6jydidPcn336', 'GENERAL', '2024-07-07 16:26:03.835477', null);

-- 회원 권한 테이블
INSERT INTO member_authority (member_no, authority_name)
VALUES (1, 'MEMBER');

INSERT INTO member_authority (member_no, authority_name)
VALUES (2, 'ADMIN');


-- $2a$10$nReEDQ9T/eURLvTbgneaBu3hiRFo/l09C5WdLc4qO6jydidPcn336

-- 비속어 정책 테이블에 초기 데이터 삽입
INSERT INTO `bad_words_policy` (`warning_threshold`, `start_date`, `end_date`, `created_at`, `updated_at`)
SELECT
    3,  -- 경고 임계값: 누적 3회 경고시 조치
    '2024-05-01 00:00:00',  -- 정책 시작 날짜
    null,  -- 정책 종료 날짜
    CURRENT_TIMESTAMP,  -- 생성 시간
    CURRENT_TIMESTAMP  -- 업데이트 시간
WHERE NOT EXISTS (
    SELECT 1 FROM `bad_words_policy`
    WHERE `warning_threshold` = 3
      AND `start_date` = '2024-05-01 00:00:00'
);

-- 쿠키 정책 테이블에 초기 데이터 삽입
INSERT INTO `cookie_policy` (`cookie_price`, `cookie_quantity_per_episode`, `start_date`, `end_date`, `created_at`, `updated_at`)
SELECT
    200,  -- 쿠키 가격: 200
    2,  -- 에피소드당 쿠키 필요 수량: 2
    '2024-04-01 00:00:00',  -- 정책 시작 날짜
    null,  -- 정책 종료 날짜
    CURRENT_TIMESTAMP,  -- 생성 시간
    CURRENT_TIMESTAMP  -- 업데이트 시간
WHERE NOT EXISTS (
    SELECT 1 FROM `cookie_policy`
    WHERE `cookie_price` = 200
      AND `start_date` = '2024-04-01 00:00:00'
);

-- 프로모션 데이터 삽입
-- 프로모션 1 (과거 프로모션)
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable,
                       start_date, end_date, deleted_at, created_at, updated_at)
SELECT '프로모션 1', 'COOKIE_QUANTITY_DISCOUNT', 3, TRUE, '2024-04-01 00:00:00', '2024-05-01
00:00:00', '2024-06-15 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 1');

-- 프로모션 2 (과거 프로모션)
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable,
                       start_date, end_date, deleted_at, created_at, updated_at)
SELECT '프로모션 2', 'COOKIE_QUANTITY_DISCOUNT', 2, FALSE, '2024-03-01 00:00:00', '2024-03-31
00:00:00', '2024-06-16 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 2');

-- 프로모션 3 (과거 프로모션)
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable,
                       start_date, end_date, deleted_at, created_at, updated_at)
SELECT '프로모션 3', 'COOKIE_QUANTITY_DISCOUNT', 4, TRUE, '2024-02-01 00:00:00', '2024-02-28
00:00:00', '2024-06-17 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 3');

-- 프로모션 4
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable, start_date, end_date, created_at, updated_at)
SELECT '프로모션 4', 'COOKIE_QUANTITY_DISCOUNT', 10, TRUE, '2024-06-01 00:00:00', '2024-06-29
00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 4');

-- 프로모션 5
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable, start_date, end_date, created_at, updated_at)
SELECT '프로모션 5', 'COOKIE_QUANTITY_DISCOUNT', 5, FALSE, '2024-06-02 00:00:00', '2024-06-30
00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 5');

-- 프로모션 6
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable, start_date, end_date, created_at, updated_at)
SELECT '프로모션 6', 'COOKIE_QUANTITY_DISCOUNT', 7, TRUE, '2024-05-01 00:00:00', '2024-06-28
00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 6');

-- 프로모션 7 (미래 프로모션)
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable, start_date, end_date, created_at, updated_at)
SELECT '프로모션 7', 'COOKIE_QUANTITY_DISCOUNT', 8, FALSE, '2024-07-01 00:00:00', '2024-08-01 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 7');

-- 프로모션 8 (미래 프로모션)
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable, start_date, end_date, created_at, updated_at)
SELECT '프로모션 8', 'COOKIE_QUANTITY_DISCOUNT', 6, FALSE, '2024-08-01 00:00:00', '2024-09-01 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 8');

-- 프로모션 9 (미래 프로모션)
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable, start_date, end_date, created_at, updated_at)
SELECT '프로모션 9', 'COOKIE_QUANTITY_DISCOUNT', 9, TRUE, '2024-09-01 00:00:00', '2024-10-01 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 9');

-- 프로모션 10 (미래 프로모션)
INSERT INTO promotion (description, discount_type, discount_quantity, is_discount_duplicatable, start_date, end_date, created_at, updated_at)
SELECT '프로모션 10', 'COOKIE_QUANTITY_DISCOUNT', 1, TRUE, '2024-11-01 00:00:00', '2024-12-01 00:00:00', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM promotion WHERE description = '프로모션 10');
