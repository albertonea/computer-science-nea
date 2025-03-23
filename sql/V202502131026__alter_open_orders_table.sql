alter table open_orders
    alter column order_type
        set data type varchar(10),
    alter column order_type
        set not null,
    add constraint check_order_type
        check (order_type in ('MARKET', 'LIMIT', 'STOPLIMIT', 'STOPMARKET'));

