-- 1) 유저 생성 (ID: 1)
INSERT IGNORE INTO users (id, username, password, role, created_at)
VALUES (1, 'testuser', '{noop}password', 'ROLE_USER', NOW());

-- 2) 지갑 생성 (유저 ID 1 참조)
INSERT IGNORE INTO wallet (id, user_id, balance)
VALUES (1, 1, 10000000);