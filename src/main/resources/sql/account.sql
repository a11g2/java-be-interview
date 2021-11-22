CREATE TABLE account
(
    id         BIGINT       NOT NULL,
    first_name VARCHAR(255) NULL,
    last_name  VARCHAR(255) NULL,
    balance    FLOAT        NOT NULL,
    PRIMARY KEY (id)
)