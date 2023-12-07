INSERT INTO "accounts" (id, name, currency_id, account_type, transaction_list, balance_id)
SELECT id, name, currency_id, account_type, transaction_list, balance_id
FROM (VALUES
    ('a1', 'Account1', 'c1', 'bank'::"account_type", 't1', 'b1'),
    ('a2', 'Account2', 'c2', 'cash'::"account_type", 't2', 'b2'),
    ('a3', 'Account3', 'c3', 'mobile_money'::"account_type", 't3', 'b3')
) AS data(id, name, currency_id, account_type, transaction_list, balance_id)
WHERE NOT EXISTS (SELECT 1 FROM "accounts" WHERE id = data.id);
