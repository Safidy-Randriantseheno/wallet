ALTER TABLE "transaction"
ADD COLUMN category_id VARCHAR;

ALTER TABLE "transaction"
DROP COLUMN label;

ALTER TABLE "transaction"
DROP COLUMN transaction_type;