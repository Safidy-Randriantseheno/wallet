INSERT INTO "transaction" (id, account_id, label, amount, type, transaction_date)
SELECT id, account_id, label, amount, type, transaction_date
FROM (VALUES
    ('1', 'a1','Salaire', 100.00, 'Crédit', 2023-12-01 12:15AM),
    ('2', 'a2', 'Cadeau de Noël', 50.00, 'Débit', 2023-12-02 02:00PM)
    ('3', 'a3', 'Nouvelle chaussure', 20.00, 'Débit', 2023-12-06 04:00PM)
) AS data(id, account_id, label, amount, type, transaction_date)
WHERE NOT EXISTS (SELECT 1 FROM "transaction" WHERE id = data.id);