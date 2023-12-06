do
$$
    begin
        if not exists(select from pg_type where typname = 'type') then
            create type "type" as enum ('debit', 'credit');
        end if;
        if not exists(select from pg_type where typname = 'label') then
                    create type "label" as enum ('loan', 'purchase', 'repayment');
                end if;
    end
$$;
CREATE TABLE IF NOT EXISTS "transaction" (
    id            varchar               constraint transaction_pk primary key default uuid_generate_v4(),
    account_id          VARCHAR         NOT NULL REFERENCES "accounts"("id"),
    label               label           not null,
    type                type            not null,
    amount              DOUBLE          NOT NULL,
    transaction_date    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
    );
    create index if not exists amount_index on "transaction" (amount);