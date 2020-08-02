-- auto update updated_at timestamp
create or replace function trigger_set_timestamp()
returns trigger as $$
begin
  new.updated_at = now();
  return new;
end;
$$ language plpgsql;

create trigger set_timestamp
before update on users
for each row
execute procedure trigger_set_timestamp();


-- auto encrypt passwords
-- create or replace function trigger_encrypt_password()
-- returns trigger as $$
-- begin
--   new.password = crypt(new.password, gen_salt('bf'));
--   return new;
-- end;
-- $$ language plpgsql;

-- create trigger encrypt_password
-- before insert or update on users
-- for each row
-- execute procedure trigger_encrypt_password();