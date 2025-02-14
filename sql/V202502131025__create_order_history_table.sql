create table order_history (
   order_id uuid primary key default uuid_generate_v4(),
   user_id uuid not null references users(user_id),
   side varchar(4) not null check (side in ('BUY', 'SELL')),
   ticker varchar(50) not null,
   executed_value bigint not null,
   remaining_quantity bigint not null,
   initial_quantity bigint not null,
   price bigint,
   order_type varchar(10) not null check (order_type in ('MARKET', 'LIMIT', 'STOPLIMIT', 'STOPMARKET')),
   created_at timestamp not null
);

create table stop_orders (
    id uuid primary key default uuid_generate_v4(),
    execution_price bigint not null,
    order_id uuid not null references open_orders(order_id) on delete cascade
)