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

insert into T_DEMO("name", age, sex, height, birthday)
values ('莎拉波娃', 35, 'M', 188.82, '1987-04-19 17:57:55');

delete
from T_DEMO
where name = '莎拉波娃';

----------
----------
-- 电子书表
----------
----------
create table T_EBOOK
(
    "id"           bigint not null,           -- id
    "name"         varchar(50),               -- 名称
    "category1_id" bigint,                    -- 分类1
    "category2_id" bigint,                    -- 分类2
    "description"  varchar(200),              -- 描述
    "cover"        varchar(200),              -- 封面
    "doc_count"    int    not null default 0, -- 文档数
    "view_count"   int    not null default 0, -- 阅读数
    "vote_count"   int    not null default 0, -- 点赞数
    constraint PK_EBOOK primary key ("id")
);
comment on table T_EBOOK is '电子书';
-- comment on column T_EBOOK.category1_id is '分类1';

insert into T_EBOOK ("id", "name", description)
values (1, 'Spring Boot 入门教程', '零基础入门 Java 开发，企业级应用开发最佳首选框架');
insert into T_EBOOK ("id", "name", description)
values (2, 'Vue 入门教程', '零基础入门 Vue 开发，企业级应用开发最佳首选框架');
insert into T_EBOOK ("id", "name", description)
values (3, 'Python 入门教程', '零基础入门 Python 开发，企业级应用开发最佳首选框架');
insert into T_EBOOK ("id", "name", description)
values (4, 'Mysql 入门教程', '零基础入门 Mysql 开发，企业级应用开发最佳首选框架');
insert into T_EBOOK ("id", "name", description)
values (5, 'Oracle 入门教程', '零基础入门 Oracle 开发，企业级应用开发最佳首选框架');

select *
from T_EBOOK;