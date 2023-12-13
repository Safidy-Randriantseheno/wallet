INSERT INTO "category" (id, name, type)
SELECT id, name, type
FROM( VALUES
    ('ca1','gift','credit'::"category_type"),
    ('ca2','buy','debit'::"category_type"),
    ('ca3','loan','debit'::"category_type")
)
AS data(id, name, type)
WHERE NOT EXISTS (SELECT 1 FROM "category" WHERE id = data.id);