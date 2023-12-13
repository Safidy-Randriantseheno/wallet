CREATE TABLE IF NOT EXISTS "transfer_history" (
    id                   VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    transaction_type varchar NOT NULL REFERENCES "transaction"("id"),
    transfer_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create index if not exists transaction_type_index on "transfer_history" ("transaction_type");