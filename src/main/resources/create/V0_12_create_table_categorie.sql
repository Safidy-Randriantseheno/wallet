DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'category_type') THEN
        CREATE TYPE "category_type" AS ENUM ('credit', 'debit');
    END IF;
END
$$;

CREATE TABLE IF NOT EXISTS "category" (
    id      VARCHAR PRIMARY KEY DEFAULT uuid_generate_v4(),
    name    VARCHAR(255) UNIQUE NOT NULL,
    type    category_type NOT NULL
);
 create index if not exists name_index on "category" (name);