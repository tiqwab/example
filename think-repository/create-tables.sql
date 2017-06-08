CREATE TABLE orders (
  id bigint not null auto_increment primary key,
  storerkey nvarchar(20) not null,
  order_date timestamp
);