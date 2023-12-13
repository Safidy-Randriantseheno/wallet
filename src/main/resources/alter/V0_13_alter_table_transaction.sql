ALTER TABLE "transaction"
ADD COLUMN category_id VARCHAR;

ALTER TABLE "transaction"
DROP COLUMN label;

ALTER TABLE "transaction"
DROP COLUMN transaction_type;

ALTER TABLE "transaction"
ADD CONSTRAINT fk_transaction_category
    FOREIGN KEY (category_id)
    REFERENCES "category" (id);