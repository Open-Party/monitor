create database monitor;

create table SysUser (id int not null auto_increment,
  username char(50) not null, email varchar(50),
  password char(50),
  primary key(id)) ENGINE=InnoDB;

create table SysRole (id  int not null auto_increment,
  name char(50) not null, primary key(id)) ENGINE=InnoDB;

create table SysRoleUser (id  int not null auto_increment,
  SysUserId int not null, SysRoleId int not null, primary key(id)) ENGINE=InnoDB;


insert into SysUser(username, email, password) values ('admin', 'admin@gmail.com', 'ISMvKXpXpadDiUoOSoAfww');
insert into SysUser(username, email, password) values ('test', 'test@gmail.com', 'CY9rzUYh03PK3k6DJie09g');

insert into SysRole(name) values('ROLE_ADMIN');
insert into SysRole(name) values('ROLE_USER');

insert into SysRoleUser(SysUserId, SysRoleId) values(1,1);
insert into SysRoleUser(SysUserId, SysRoleId) values(2,2);

select u.*, r.name from SysUser u LEFT JOIN SysRoleUser sru on u.id=sru.SysUserId LEFT JOIN SysRole r on sru.SysRoleId=r.id where username="admin@gmail.com";

CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
);

ALTER TABLE SysUser ADD COLUMN email VARCHAR(50) AFTER username;
ALTER TABLE SysUser ADD COLUMN dob TIMESTAMP DEFAULT CURRENT_TIMESTAMP AFTER password;

update SysUser set username = "admin", email = "admin@gmail.com" where id = 1;
update SysUser set username = "test", email = "test@gmail.com" where id = 2;