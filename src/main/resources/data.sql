INSERT INTO user (email, password, role)
VALUES ('master@example.com', '$2a$10$NBpBg7DZGXkMbHD8IAfGve9DUUao/fPJGKle5jmwovimtc7B.J/7K', 'Master')
ON DUPLICATE KEY UPDATE email = VALUES(email); 
