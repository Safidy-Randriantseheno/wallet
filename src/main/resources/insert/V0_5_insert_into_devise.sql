-- Inserting data into the currency table if the ID does not exist
INSERT INTO "currency" (id, code, name)
SELECT id, code, name
FROM (VALUES
  ('c1', 'EUR'::"code", 'Euro'::"name"),
  ('c2', 'MGA'::"code", 'Arriary'::"name")
  ('c3', 'EUR'::"code", 'Euro'::"name")
) AS data(id, code, name)
WHERE NOT EXISTS (SELECT 1 FROM "currency" WHERE id = data.id);
