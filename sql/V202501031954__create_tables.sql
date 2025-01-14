CREATE TABLE users (
   user_id uuid primary key default uuid_generate_v4(),
   username varchar(50) not null unique,
   created_at timestamp default now()
);

CREATE TABLE balances (
   user_id uuid not null references users(user_id),
   ticker varchar(50) not null,
   balance bigint not null default 0,
   locked_balance bigint not null default 0,
   primary key(user_id, ticker)
);

CREATE TABLE trades (
    trade_id uuid primary key default uuid_generate_v4(),
    buyer_id uuid not null references users(user_id),
    seller_id uuid not null references users(user_id),
    ticker varchar(50) not null ,
    quantity bigint NOT NULL,
    price bigint NOT NULL,
    trade_time timestamp default now()
);

CREATE TABLE open_orders (
     order_id uuid primary key default uuid_generate_v4(),
     user_id uuid not null references users(user_id),
     side varchar(4) not null check (side in ('BUY', 'SELL')),
     ticker varchar(50) not null,
     remaining_quantity bigint not null,
     initial_quantity bigint not null,
     price bigint not null,
     created_at timestamp default now()
);