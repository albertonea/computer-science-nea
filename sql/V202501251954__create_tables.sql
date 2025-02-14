create table users (
   user_id uuid primary key default uuid_generate_v4(),
   username varchar(50) not null unique,
   password varchar(255) not null,
   created_at timestamp default now()
);

create table balances (
   user_id uuid not null references users(user_id),
   ticker varchar(50) not null,
   balance bigint not null default 0,
   locked_balance bigint not null default 0,
   primary key(user_id, ticker)
);

create table trades (
    trade_id uuid primary key default uuid_generate_v4(),
    buyer_id uuid not null references users(user_id),
    seller_id uuid not null references users(user_id),
    ticker varchar(50) not null ,
    quantity bigint NOT NULL,
    price bigint NOT NULL,
    trade_time timestamp default now()
);

create table open_orders (
     order_id uuid primary key default uuid_generate_v4(),
     user_id uuid not null references users(user_id),
     side varchar(4) not null check (side in ('BUY', 'SELL')),
     ticker varchar(50) not null,
     executed_value bigint not null,
     remaining_quantity bigint not null,
     initial_quantity bigint not null,
     price bigint,
     order_type varchar(10) not null check (order_type in ('MARKET', 'LIMIT', 'STOPLIMIT', 'STOPMARKET')),
     created_at timestamp default now()
);