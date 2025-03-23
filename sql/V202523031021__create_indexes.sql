create index idx_open_orders_user_id_ticker
    on open_orders(user_id, ticker);

create unique index  idx_users_username
    on users(username);