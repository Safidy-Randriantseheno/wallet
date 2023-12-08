CREATE TABLE IF NOT EXISTS transfer_history (
    id                   VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    debit_transaction_id varchar NOT NULL,
    credit_transaction_id varchar NOT NULL,
    transfer_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (debit_transaction_id) REFERENCES transaction(id),
    FOREIGN KEY (credit_transaction_id) REFERENCES transaction(id)
);