create table if not exists tbl_user
(
    id           bigint auto_increment comment 'id' primary key,
    username     varchar(256)                           null comment '账号',
    account  varchar(256)                           not null comment '昵称',
    avatar   varchar(1024)                          null comment '头像',
    gender       tinyint                                null comment '性别',
    role     varchar(256) default 'user'            not null comment '角色：user / admin',
    password varchar(512)                           not null comment '密码',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_username
    unique (username)
) comment '用户';
