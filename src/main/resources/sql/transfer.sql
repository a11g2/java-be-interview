CREATE TABLE transfer
(
    id            BIGINT   NOT NULL,
    source_id     BIGINT   NOT NULL,
    target_id     BIGINT   NOT NULL,
    amount        FLOAT    NOT NULL,
    transfer_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT account_source_fk FOREIGN KEY (source_id) REFERENCES account (id),
    CONSTRAINT account_target_fk FOREIGN KEY (target_id) REFERENCES account (id)
)