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

update T_EBOOK
set "cover"='image/wiki_spring.png'
where "id" = 1;
update T_EBOOK
set "cover"='image/wiki_vue.jpg'
where "id" = 2;
update T_EBOOK
set "cover"='image/wiki_python.jpg'
where "id" = 3;
update T_EBOOK
set "cover"='image/wiki_mysql.png'
where "id" = 4;
update T_EBOOK
set "cover"='image/wiki_oracle.jpg'
where "id" = 5;

update T_EBOOK
set "category1_id"=11,
    "category2_id"=22,
    "doc_count"=156,
    "view_count"=23,
    vote_count=20
where "id" = 2;

----------
----------
-- 分类表
----------
----------
drop table if exists T_CATEGORY;
create table T_CATEGORY
(
    "id"     bigint      not null,           -- id
    "parent" bigint      not null default 0, -- 父id
    "name"   varchar(50) not null,           -- 名称
    "sort"   int,                            -- 顺序
    constraint PK_CATEGORY primary key ("id")
);
comment on table T_CATEGORY is '分类';

insert into T_CATEGORY (id, parent, name, sort)
values (100, 000, '前端开发', 100);
insert into T_CATEGORY (id, parent, name, sort)
values (101, 100, 'Vue', 101);
insert into T_CATEGORY (id, parent, name, sort)
values (102, 100, 'HTML & CSS', 102);
insert into T_CATEGORY (id, parent, name, sort)
values (200, 000, 'Java', 200);
insert into T_CATEGORY (id, parent, name, sort)
values (201, 200, '基础应用', 201);
insert into T_CATEGORY (id, parent, name, sort)
values (202, 200, '框架应用', 202);
insert into T_CATEGORY (id, parent, name, sort)
values (300, 000, 'Python', 300);
insert into T_CATEGORY (id, parent, name, sort)
values (301, 300, '基础应用', 301);
insert into T_CATEGORY (id, parent, name, sort)
values (302, 300, '进阶方向应用', 302);
insert into T_CATEGORY (id, parent, name, sort)
values (400, 000, '数据库', 400);
insert into T_CATEGORY (id, parent, name, sort)
values (401, 400, 'MySQL', 401);
insert into T_CATEGORY (id, parent, name, sort)
values (500, 000, '其它', 500);
insert into T_CATEGORY (id, parent, name, sort)
values (501, 500, '服务器', 501);
insert into T_CATEGORY (id, parent, name, sort)
values (502, 500, '开发工具', 502);
insert into T_CATEGORY (id, parent, name, sort)
values (503, 500, '热门服务端语言', 503);

select *
from T_CATEGORY;

-- 文档表
drop table if exists T_DOC;
create table T_DOC
(
    "id"         bigint      not null,
    "ebook_id"   bigint      not null default 0, -- 电子书id
    "parent"     bigint      not null default 0, -- 父id
    "name"       varchar(50) not null,           -- 名称
    "sort"       int,                            -- 顺序，读取的顺序
    "view_count" int                  default 0, -- 阅读数
    "vote_count" int                  default 0, -- 点赞数
    constraint PK_DOC primary key ("id")
);

insert into T_DOC (id, ebook_id, parent, name, sort, view_count, vote_count)
values (1, 1, 0, '文档1', 1, 0, 0);
insert into T_DOC (id, ebook_id, parent, name, sort, view_count, vote_count)
values (2, 1, 1, '文档1.1', 1, 0, 0);
insert into T_DOC (id, ebook_id, parent, name, sort, view_count, vote_count)
values (3, 1, 0, '文档2', 2, 0, 0);
insert into T_DOC (id, ebook_id, parent, name, sort, view_count, vote_count)
values (4, 1, 3, '文档2.1', 1, 0, 0);
insert into T_DOC (id, ebook_id, parent, name, sort, view_count, vote_count)
values (5, 1, 3, '文档2.2', 2, 0, 0);
insert into T_DOC (id, ebook_id, parent, name, sort, view_count, vote_count)
values (6, 1, 5, '文档2.2.1', 1, 0, 0);

-- 文档内容表
drop table if exists T_CONTENT;
create table T_CONTENT
(
    "id"      bigint not null, -- 同文档表id，可以看作分表操作
    "content" text   not null, -- 文档内容文本（HTML）
    constraint PK_CONTENT primary key ("id")
);

-- 用户表
drop table if exists T_USER;
create table T_USER
(
    "id"         bigint      not null, -- ID
    "login_name" varchar(50) not null, --登陆名
    "name"       varchar(50),          -- 昵称
    "password"   char(32)    not null, -- 密码
    constraint PK_USER primary key ("id"),
    constraint UK_LOGIN_NAME unique ("login_name")
);

-- Postgre更新表信息
update public.T_EBOOK
set doc_count = t1.doc_count,
    view_count=t1.view_count,
    vote_count=t1.vote_count
from (select ebook_id, count(1) doc_count, sum(view_count) view_count, sum(vote_count) vote_count
      from public.T_DOC
      group by ebook_id) t1
where T_EBOOK.id = t1.ebook_id;

-- 电子书快照表
drop table if exists T_EBOOK_SNAPSHOT;
create table T_EBOOK_SNAPSHOT
(
    "id"            bigserial not null,           -- 获取ebook数据不经过Java代码
    "ebook_id"      bigint    not null default 0, -- 电子书ID
    "date"          date      not null,           -- 快照日期
    "view_count"    int       not null default 0, -- 阅读数
    "vote_count"    int       not null default 0, -- 点赞数
    "view_increase" int       not null default 0, -- 阅读增长
    "vote_increase" int       not null default 0, -- 点赞增长
    constraint PK_EBOOKSNAP primary key ("id"),
    constraint UK_EBOOKID_DATE unique ("ebook_id", "date")
);

-- 为所有电子表生成一条今天的记录，如果还没有
-- 更新总阅读数，总点赞数（累计历史的）
-- 更新今天阅读数，今天点赞数
insert into T_EBOOK_SNAPSHOT(ebook_id, date, view_count, vote_count, view_increase, vote_increase)
    (
        select id, current_date, 0, 0, 0, 0
        from T_EBOOK t1
        where not exists
            (
                select 1
                from T_EBOOK_SNAPSHOT t2
                where t2.date = current_date
                  and t2.ebook_id = t1.id
            )
    );

update T_EBOOK_SNAPSHOT t1
set view_count = t2.view_count,
    vote_count = t2.vote_count
from T_EBOOK t2
where t1."date" = current_date
  and t1.ebook_id = t2.id;

-- 获取昨天的数据
-- 今天所有的电子书左联结昨天所有的电子书，阅读数点赞数取昨天的
select t1.ebook_id, t2.view_count, t2.vote_count
from T_EBOOK_SNAPSHOT t1
         left join
     (select ebook_id, view_count, vote_count from T_EBOOK_SNAPSHOT where date = current_date - 1) t2
     on t1.ebook_id = t2.ebook_id
where t1."date" = current_date;

-- t3：今天所有的电子书(取其中的ebookId)
-- t4：昨天的电子书（取其中的viewCount和voteCount）
-- t2：t3和t4组成的表
update T_EBOOK_SNAPSHOT t1
set view_increase = (t1.view_count - coalesce(t2.view_count, 0)),
    vote_increase = (t1.vote_count - coalesce(t2.vote_count, 0))
from (
         select t3.ebook_id, t4.view_count, t4.vote_count
         from T_EBOOK_SNAPSHOT t3
                  left join
              (
                  select ebook_id, view_count, vote_count from T_EBOOK_SNAPSHOT where date = current_date - 1
              ) t4
              on t3.ebook_id = t4.ebook_id
         where t3."date" = current_date
     ) t2
where t1.ebook_id = t2.ebook_id
  and t1."date" = current_date;

-- 统计昨天今天的数值
select t1."date", sum(t1.view_count), sum(t1.vote_count), sum(t1.view_increase), sum(vote_increase)
from T_EBOOK_SNAPSHOT t1
where t1."date" >= current_date - 1
group by t1."date"
order by t1."date" asc
