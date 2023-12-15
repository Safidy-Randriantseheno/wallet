
INSERT INTO "transaction" (id, amount, transaction_date, account_id, category_id)
SELECT id, amount, transaction_date, account_id, category_id
FROM (VALUES
    ('t1', 100.00, CURRENT_TIMESTAMP,'a1','ca1'),
    ('t2', 50.00, CURRENT_TIMESTAMP,'a2','ca2'),
    ('t3', 50.00, CURRENT_TIMESTAMP,'a1','ca3'),
    ('t4', 20.00, CURRENT_TIMESTAMP,'a2','ca1')
) AS data(id, amount, transaction_date, account_id, category_id)
WHERE NOT EXISTS (SELECT 1 FROM "transaction" WHERE id = data.id);


INSERT INTO "transaction" (id, amount, transaction_date, account_id, category_id)
SELECT id, amount, transaction_date, account_id, category_id
FROM (VALUES
    ('t4', 20.00, CURRENT_TIMESTAMP,'a2','ca1')
) AS data(id, amount, transaction_date, account_id, category_id)
WHERE NOT EXISTS (SELECT 1 FROM "transaction" WHERE id = data.id);