INSERT INTO "transaction" (id, account_id, amount, transaction_name, transaction_date)
SELECT id, account_id, amount, transaction_name, transaction_date
FROM (VALUES
    ('t1', 'a1', 100.00,'first_transaction', CURRENT_TIMESTAMP),
    ('t2', 'a2', 50.00,'second_transaction', CURRENT_TIMESTAMP)
) AS data(id, account_id, amount, transaction_name, transaction_date)
WHERE NOT EXISTS (SELECT 1 FROM "transaction" WHERE id = data.id);