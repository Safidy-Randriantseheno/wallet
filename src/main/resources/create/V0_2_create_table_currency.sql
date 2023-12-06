do
$$
    begin
        if not exists(select from pg_type where typname = 'code') then
            create type "code" as enum ('EUR', 'MGA');
        end if;
        if not exists(select from pg_type where typname = 'name') then
                    create type "name" as enum ('Euro', 'Arriary');
                end if;
    end
$$;
CREATE TABLE IF NOT EXISTS "currency" (
    id            varchar           constraint devise_pk primary key default uuid_generate_v4(),
    code          code              not null,
    name          name              not null

);
create index if not exists currency_name_index on "currency" (name);