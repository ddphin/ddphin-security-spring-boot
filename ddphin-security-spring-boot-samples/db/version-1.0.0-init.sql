-- init database and user X
drop user if exists 'ddphin'@'%';
CREATE USER IF NOT EXISTS 'ddphin'@'%' IDENTIFIED BY 'ddphin';
GRANT ALL PRIVILEGES ON ddphin.* TO 'ddphin'@'%';

drop database if exists ddphin;
CREATE DATABASE IF NOT EXISTS ddphin
    DEFAULT CHARACTER SET utf8mb4  DEFAULT COLLATE utf8mb4_unicode_ci;

use ddphin;

create table if not exists auth_user_credential
(
    id bigint auto_increment
        primary key,
    userId bigint not null,
    credentialType int not null,
    credentialValue varchar(128) not null,
    constraint auth_user_credential_uid_ct_index
        unique (userId, credentialType)
);

create table if not exists  auth_user_identifier
(
    id bigint auto_increment
        primary key,
    userId bigint not null,
    identifierType int not null,
    identifierValue varchar(128) not null,
    constraint auth_user_identifier_it_iv_uindex
        unique (identifierType, identifierValue),
    constraint auth_user_identifier_uid_it_index
        unique (userId, identifierType)
);

create table if not exists  auth_user_social
(
    id bigint auto_increment
        primary key,
    userId bigint not null,
    identifierType int not null,
    socialType int not null,
    socialValue varchar(128) not null,
    accessToken varchar(128) null,
    refreshToken varchar(128) null,
    sessionKey varchar(128) null,
    expireTime datetime null,
    name varchar(128) null,
    gender int null,
    avatar varchar(1000) null,
    constraint auth_user_social_uid_it_st_index
        unique (userId, identifierType, socialType)
);

create table if not exists  auth_role
(
    id bigint auto_increment
        primary key,
    name varchar(32) not null
);
create table if not exists  auth_permission
(
    id bigint auto_increment
        primary key,
    name varchar(32) not null,
    requestUrl varchar(164) not null,
    requestMethod varchar(32) not null
);

create table if not exists  auth_role_permission
(
    id bigint auto_increment
        primary key,
    roleId bigint not null,
    permissionId bigint not null
);
create index auth_role_permission_roleId_index
    on auth_role_permission (roleId);

create table if not exists  auth_user_role
(
    id bigint auto_increment
        primary key,
    userId bigint not null,
    roleId bigint not null
);
create index auth_user_role_userId_index
    on auth_user_role (userId);