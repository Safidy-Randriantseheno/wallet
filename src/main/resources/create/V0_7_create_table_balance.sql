CREATE TABLE IF NOT EXISTS "balance" (
    id                  varchar            constraint balance_pk primary key default uuid_generate_v4(),

    balance_value       DECIMAL(10, 2)             NOT NULL,
    balance_date        TIMESTAMP          DEFAULT CURRENT_TIMESTAMP
);
create index if not exists balance_value_index on "balance" (balance_value);
