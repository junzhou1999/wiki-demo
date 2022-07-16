-- postgres12.11
CREATE TABLE T_TEST
(
    "id"       BIGINT NOT NULL,
    "name"     VARCHAR(51),
    "password" VARCHAR(51),
    CONSTRAINT PK_TEST PRIMARY KEY ("id")
);

insert into T_TEST(id, name, password)
values (1001, '黎明', '123456');

select *
from T_TEST;

create sequence SEQ_DEMO as bigint
    INCREMENT 2 -- 步进
    MINVALUE 1
    MAXVALUE 999999999
    START 1001 -- 初始值
    CACHE 20; -- 提前把数据放进缓冲区中

create type ENUM_DEMO as enum ('M','F');
create table T_DEMO
(
    "id"     BIGINT      NOT NULL default nextval('SEQ_DEMO'),
    "name"   varchar(21) not null,
    age      int,
    sex      ENUM_DEMO            default null,
    height   decimal(5, 2),
    birthday timestamptz,
    constraint PK_DEMO primary key ("id"),
    constraint CKC_AGE_DEMO check (age > 0)
);
alter sequence SEQ_DEMO owned by T_DEMO.id;

select *
from T_DEMO;

insert into T_DEMO("name", age, sex, height, birthday)
values ('波多', 34, 'F', 163.88, '1988-05-24');

select age(now(), '1988-05-24');