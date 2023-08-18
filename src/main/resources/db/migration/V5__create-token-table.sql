CREATE TABLE tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(20) NOT NULL,
    revoked BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_tokens_user FOREIGN KEY (user_id) REFERENCES users(id)
);