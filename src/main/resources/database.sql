create table SysUser (id  int not null auto_increment, username char(50) not null, password char(50), primary key(id)) ENGINE=InnoDB;
create table SysRole (id  int not null auto_increment, name char(50) not null, primary key(id)) ENGINE=InnoDB;
create table SysRoleUser (id  int not null auto_increment, SysUserId int not null, RoleId int not null, primary key(id)) ENGINE=InnoDB;


insert into SysUser(username, password) values ('admin', 'admin');
insert into SysUser(username, password) values ('test', 'test');

insert into SysRole(name) values('ROLE_ADMIN');
insert into SysRole(name) values('ROLE_USER');

insert into SysRoleUser(SysUserId, SysRoleId) values(1,1);
insert into SysRoleUser(SysUserId, SysRoleId) values(2,2);

select u.*, r.name from SysUser u LEFT JOIN SysRoleUser sru on u.id=sru.SysUserId LEFT JOIN SysRole r on sru.SysRoleId=r.id where username="admin@gmail.com";