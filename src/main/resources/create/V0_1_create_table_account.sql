do
$$
    begin
        if not exists(select from pg_type where typname = 'account_type') then
            create type "account_type" as enum ('bank', 'cash', 'mobile_money');
        end if;
    end
$$;
CREATE TABLE IF NOT EXISTS "accounts" (
    id                          VARCHAR               constraint accounts_pk primary key default uuid_generate_v4(),
    name                        VARCHAR(255)          NOT NULL,
    currency_id                 VARCHAR               NOT NULL REFERENCES "currency"("id"),
    account_type                account_type                  not null,
    transaction_list            VARCHAR               NOT NULL REFERENCES "transaction"("id"),
    balance_id                  VARCHAR               NOT NULL REFERENCES "balance"("id")
);
create index if not exists accounts_name_index on "accounts" (name);
