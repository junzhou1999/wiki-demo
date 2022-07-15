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