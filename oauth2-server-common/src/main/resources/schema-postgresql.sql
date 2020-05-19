
DROP TABLE IF EXISTS rolesperuser;
DROP TABLE IF EXISTS rolespergroup;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS oauth_client_details;
DROP TABLE IF EXISTS groupmembers;
DROP TABLE IF EXISTS userdetails;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS userstates;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS custom_templates;

DROP SEQUENCE IF EXISTS groups_seq;
DROP SEQUENCE IF EXISTS users_seq;
DROP SEQUENCE IF EXISTS userdetails_seq;
DROP SEQUENCE IF EXISTS roles_seq;
DROP SEQUENCE IF EXISTS templates_seq;

CREATE SEQUENCE groups_seq;

CREATE TABLE groups (
  idgroup int NOT NULL DEFAULT NEXTVAL ('groups_seq'),
  groupname varchar(45) NOT NULL,
  PRIMARY KEY (idgroup)
)  ;

ALTER SEQUENCE groups_seq RESTART WITH 2;


CREATE TABLE userstates (
  iduserstate int NOT NULL,
  description varchar(45) NOT NULL,
  PRIMARY KEY (iduserstate)
) ;

CREATE SEQUENCE users_seq;

CREATE TABLE users (
  iduser int NOT NULL DEFAULT NEXTVAL ('users_seq'),
  username varchar(45) NOT NULL,
  password varchar(100) NOT NULL,
  name varchar(100) NOT NULL,
  phone varchar(45) DEFAULT NULL,
  phone1 varchar(45) DEFAULT NULL,
  phone2 varchar(45) DEFAULT NULL,
  email varchar(100) NOT NULL,
  postaladdress varchar(512) DEFAULT NULL,
  iduserstate int NOT NULL,
  PRIMARY KEY (iduser),
  CONSTRAINT login_UNIQUE UNIQUE  (username),
  CONSTRAINT email_UNIQUE UNIQUE  (email),
  CONSTRAINT userstatestousers FOREIGN KEY (iduserstate) REFERENCES userstates (iduserstate) ON DELETE NO ACTION ON UPDATE NO ACTION
)  ;

ALTER SEQUENCE users_seq RESTART WITH 2;

CREATE INDEX userstatestousers_idx ON users (iduserstate);

CREATE SEQUENCE userdetails_seq;

CREATE TABLE userdetails (
  iduserdetail int NOT NULL DEFAULT NEXTVAL ('userdetails_seq'),
  iduser int NOT NULL,
  detailkey varchar(30) NOT NULL,
  detailvalue varchar(100) NULL,
  PRIMARY KEY (iduserdetail),
  CONSTRAINT userstousersdetail FOREIGN KEY (iduser) REFERENCES users (iduser) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

CREATE TABLE groupmembers (
  iduser int NOT NULL,
  idgroup int NOT NULL,
  PRIMARY KEY (iduser,idgroup)
 ,
  CONSTRAINT groupstogroupmembers FOREIGN KEY (idgroup) REFERENCES groups (idgroup) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT userstogroupmembers FOREIGN KEY (iduser) REFERENCES users (iduser) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

CREATE INDEX groupstogroupmembers_idx ON groupmembers (idgroup);

CREATE TABLE oauth_client_details (
  client_id varchar(255) NOT NULL,
  resource_ids varchar(255) DEFAULT NULL,
  client_secret varchar(255) DEFAULT NULL,
  scope varchar(255) DEFAULT NULL,
  authorized_grant_types varchar(255) DEFAULT NULL,
  web_server_redirect_uri varchar(255) DEFAULT NULL,
  authorities varchar(255) DEFAULT NULL,
  access_token_validity int DEFAULT NULL,
  refresh_token_validity int DEFAULT NULL,
  additional_information varchar(4096) DEFAULT NULL,
  autoapprove varchar(255) DEFAULT NULL,
  PRIMARY KEY (client_id)
) ;

CREATE SEQUENCE roles_seq;

CREATE TABLE roles (
  idrole int NOT NULL DEFAULT NEXTVAL ('roles_seq'),
  rolename varchar(45) NOT NULL,
  PRIMARY KEY (idrole)
)  ;

ALTER SEQUENCE roles_seq RESTART WITH 2;

CREATE TABLE rolespergroup (
  idgroup int NOT NULL,
  idrole int NOT NULL,
  PRIMARY KEY (idgroup,idrole)
 ,
  CONSTRAINT groupstorolespergroup FOREIGN KEY (idgroup) REFERENCES groups (idgroup) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT rolestorolespergroup FOREIGN KEY (idrole) REFERENCES roles (idrole) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

CREATE INDEX rolestorolespergroup_idx ON rolespergroup (idrole);

CREATE TABLE rolesperuser (
  iduser int NOT NULL,
  idrole int NOT NULL,
  PRIMARY KEY (iduser,idrole),
  CONSTRAINT rolestorolesperuser FOREIGN KEY (idrole) REFERENCES roles (idrole) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT userstorolesperuser FOREIGN KEY (iduser) REFERENCES users (iduser) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

CREATE INDEX rolestorolesperuser_idx ON rolesperuser (idrole);

CREATE SEQUENCE templates_seq;

CREATE TABLE custom_templates (
  id int NOT NULL DEFAULT NEXTVAL ('templates_seq'),
  name varchar(20) NOT NULL,
  content bytea NOT NULL,
  encoding varchar(20) NOT NULL,
  last_modified timestamp(0) DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT name_unique UNIQUE  (name)
);

ALTER SEQUENCE templates_seq RESTART WITH 2;
