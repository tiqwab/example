create table account (id bigint not null auto_increment, address varchar(255), age integer, birth_day datetime, email varchar(255), name varchar(255), password varchar(255), roles tinyblob, zip varchar(255), primary key (id));
create table category (id bigint not null auto_increment, category_name varchar(255), primary key (id));
create table demo_order (id bigint not null auto_increment, email varchar(255), order_date datetime, primary key (id));
create table goods (id bigint not null auto_increment, description varchar(255), goods_name varchar(255), price integer, category_id bigint, primary key (id));
create table order_line (id bigint not null auto_increment, line_no integer, quantity integer, goods_id bigint, order_id bigint, primary key (id));
alter table goods add constraint FKn1hbjbxhwrd3wynoruqh31xk8 foreign key (category_id) references category (id);
alter table order_line add constraint FK63ds2u1i37e0s0rg8353p3whd foreign key (goods_id) references goods (id);
alter table order_line add constraint FK4r3n8am2gues8d4ki84jxffs1 foreign key (order_id) references demo_order (id);
