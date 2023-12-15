CREATE TABLE IF NOT EXISTS "balance_result" (
    account_id     VARCHAR               NOT NULL REFERENCES "accounts"("id"),
    total_debit    DECIMAL(19, 2),
    total_credit   DECIMAL(19, 2)
);

CREATE INDEX IF NOT EXISTS balance_result_total_credit_index ON "balance_result" (total_credit);
