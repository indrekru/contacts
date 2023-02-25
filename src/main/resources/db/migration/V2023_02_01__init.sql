create table if not exists contact (
    id                      uuid                not null primary key,
    name                    text                                    ,
    code_name               text                                    ,
    phone                   text                                    ,
    create_time                     timestamp with time zone not null,
    update_time                     timestamp with time zone not null,
    delete_time                     timestamp with time zone null
);

create index if not exists idx_contact_name_phone on contact(name, phone);