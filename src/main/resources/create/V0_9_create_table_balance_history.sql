CREATE TABLE IF NOT EXISTS "balance_history" (
    id               VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_id       VARCHAR NOT NULL REFERENCES "accounts"("id"),
    balance_id       VARCHAR NOT NULL REFERENCES "balance"("id"),
    history_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX IF NOT EXISTS balance_history_account_index ON "balance_history" (account_id);
