CREATE TABLE IF NOT EXISTS orders (
  serialkey bigint not null auto_increment primary key,
  id bigint not null unique,
  storer_id Long not null,
  order_date timestamp
);

CREATE TABLE IF NOT EXISTS orderdetail (
  serialkey bigint not null auto_increment primary key,
  id bigint not null unique,
  order_id bigint not null,
  sku nvarchar(50) not null,
  qty int not null
);

CREATE TABLE IF NOT EXISTS storer (
  serialkey bigint not null auto_increment primary key,
  id bigint not null unique,
  name nvarchar(50)
);