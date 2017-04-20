insert into account (id, address, age, birth_day, email, name, password, roles, zip) values (1, 'Tokyo', 20, null, 'user@user.com', 'user', '$2a$10$coKaoOAgM2xgCMmAAoQpwem8TmKsVJhVzC4mVsh10APSCTePYz7V6', null, '111-1111');
insert into category (id, category_name) values (1, 'book');
insert into category (id, category_name) values (2, 'cd');
insert into goods (id, category_id, description, goods_name, price) values (1, 1, 'this is book1', 'book1', 100);
insert into goods (id, category_id, description, goods_name, price) values (2, 2, 'this is cd1', 'cd1', 200);
