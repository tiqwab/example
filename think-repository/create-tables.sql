CREATE TABLE IF NOT EXISTS orders (
  serialkey bigint not null auto_increment primary key,
  id bigint not null unique,
  storerkey nvarchar(20) not null,
  order_date timestamp
);