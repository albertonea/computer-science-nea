create table refresh_tokens (
    id uuid primary key default uuid_generate_v4(),
    user_id uuid not null references users(user_id),
    created_at timestamp default now(),
    expires_at timestamp not null
);