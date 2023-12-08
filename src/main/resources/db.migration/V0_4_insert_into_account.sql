INSERT INTO "accounts" (id, name, currencyId, type, transactionList, balanceId)
SELECT id, name, currencyId, type, transactionList, balanceId
FROM (VALUES
    ('1', 'C1', '1'),

) AS data(id, name, currencyId, type, transactionList, balanceId)
WHERE NOT EXISTS (SELECT 1 FROM "accounts" WHERE id = data.id);