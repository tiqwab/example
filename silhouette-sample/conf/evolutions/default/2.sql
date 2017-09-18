# --- !Ups

CREATE TABLE LOGIN_INFO (
    id BIGINT PRIMARY KEY NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    provider_key VARCHAR(255) NOT NULL,
    user_id BIGINT,
    CONSTRAINT FK_LI_USER_ID FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    INDEX IX_LI_PROVIDER_ID_KEY (PROVIDER_ID, PROVIDER_KEY)
);

# --- !Downs

DROP TABLE LOGIN_INFO;