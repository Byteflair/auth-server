INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, authorities, access_token_validity, refresh_token_validity, autoapprove) VALUES ('byteflair','$2a$12$6bEkR5.PZfut4t8azXk9fOqrBQzC8eu/iavS.iQGZrWKXIOYQ.ADW','read,write,trust','authorization_code,password,refresh_token,implicit,client_credentials','ROLE_TRUSTED_CLIENT',900,43200,'true');

INSERT INTO roles (idrole, rolename) VALUES (1,'ROLE_ADMIN');
INSERT INTO roles (idrole, rolename) VALUES (2,'ROLE_USER');

INSERT INTO systems VALUES (13,'System');

INSERT INTO userstates (iduserstate, description) VALUES (0,'INACTIVE');
INSERT INTO userstates (iduserstate, description) VALUES (1,'ACTIVE');
INSERT INTO userstates (iduserstate, description) VALUES (2,'LOCKED');

INSERT INTO users (iduser, username, password, idsystem, name, email, iduserstate) VALUES (1,'admin','$2a$12$6bEkR5.PZfut4t8azXk9fOqrBQzC8eu/iavS.iQGZrWKXIOYQ.ADW', 13, 'admin', 'admin@byteflair.com',1);

INSERT INTO userdetails (detailkey, detailvalue, iduser) VALUES ('key1', 'valor1', 1);
INSERT INTO userdetails (detailkey, detailvalue, iduser) VALUES ('key2', 'valor1', 1);
INSERT INTO userdetails (detailkey, detailvalue, iduser) VALUES ('key3', 'valor1', 1);

INSERT INTO rolesperuser  (iduser, idrole) VALUES (1,1);
INSERT INTO rolesperuser  (iduser, idrole) VALUES (1,2);

