
INSERT INTO "transaction" (id, label, transaction_type, amount, transaction_date)
SELECT id, label, type, amount, transaction_date
FROM (VALUES
    ('t1', 'loan'::"label", 'debit'::"transaction_type", 100.00, CURRENT_TIMESTAMP),
    ('t2', 'purchase'::"label", 'credit'::"transaction_type", 50.00, CURRENT_TIMESTAMP),
    ('t3', 'repayment'::"label", 'credit'::"transaction_type", 30.00, CURRENT_TIMESTAMP)
) AS data(id, label, type, amount, transaction_date)
WHERE NOT EXISTS (SELECT 1 FROM "transaction" WHERE id = data.id);
