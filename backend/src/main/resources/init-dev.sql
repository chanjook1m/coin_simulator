-- 1) 유저 생성 (ID: 1)
INSERT IGNORE INTO users (id, username, password, role, created_at)
VALUES (1, 'testuser', '{noop}password', 'ROLE_USER', NOW());

-- 2) 지갑 생성 (유저 ID 1 참조)
INSERT IGNORE INTO wallet (id, user_id, balance)
VALUES (1, 1, 10000000);

-- 3. ★ 핵심: 장부(Ledger) 초기 데이터 추가
-- 엔티티 필드: user_id, asset, change_amount, reason, created_at
INSERT INTO ledger (user_id, asset, change_amount, reason, created_at)
VALUES (1, 'KRW', 10000000, 'INITIAL_DEPOSIT', NOW());
