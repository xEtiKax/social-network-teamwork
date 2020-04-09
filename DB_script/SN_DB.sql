create table users
(
    id         int auto_increment
        primary key,
    first_name varchar(50)       null,
    last_name  varchar(50)       null,
    username   varchar(50)       not null,
    email      varchar(50)       null,
    password   varchar(68)       not null,
    picture    blob              null,
    enabled    tinyint default 1 not null,
    age        int               null
);

create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint authorities_users_username_fk
        foreign key (username) references users (username)
);

create table posts
(
    id         int auto_increment
        primary key,
    text       varchar(300)                          null,
    picture    blob                                  null,
    created_by int                                   null,
    is_public  tinyint(1)                            null,
    created_at timestamp default current_timestamp() not null on update current_timestamp(),
    enabled    tinyint   default 1                   not null,
    constraint posts_users_id_fk
        foreign key (created_by) references users (id)
);

create table comments
(
    id          int auto_increment
        primary key,
    description varchar(200)                          not null,
    user_id     int                                   not null,
    created_at  timestamp default current_timestamp() not null on update current_timestamp(),
    post_id     int                                   not null,
    constraint comments_posts_id_fk
        foreign key (post_id) references posts (id),
    constraint comments_users_id_fk
        foreign key (user_id) references users (id)
);

create table likes
(
    like_id int auto_increment
        primary key,
    user_id int not null,
    post_id int not null,
    constraint UQ_UserID_PostID
        unique (user_id, post_id),
    constraint likes_posts_id_fk
        foreign key (post_id) references posts (id),
    constraint likes_users_id_fk
        foreign key (user_id) references users (id)
);

create table requests
(
    id          int auto_increment
        primary key,
    sender_id   int                  not null,
    receiver_id int                  not null,
    isAccepted  tinyint(1) default 0 not null,
    isPending   tinyint(1) default 1 not null,
    constraint requests_users_id_fk
        foreign key (sender_id) references users (id),
    constraint requests_users_id_fk_2
        foreign key (receiver_id) references users (id)
);

create index users_authorities_username_fk
    on users (username);

create table users_friends
(
    user_id   int not null,
    friend_id int not null,
    constraint USERS_FRIENDS_UK
        unique (user_id, friend_id),
    constraint users_friends_users_id_fk
        foreign key (user_id) references users (id),
    constraint users_friends_users_id_fk_2
        foreign key (friend_id) references users (id)
);


