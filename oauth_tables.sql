-- Creacion del esquema 

-- Schema: aut
-- DROP SCHEMA aut;

CREATE SCHEMA aut AUTHORIZATION postgres;
GRANT ALL ON SCHEMA aut TO postgres;
GRANT USAGE ON SCHEMA aut TO sfinx_aut;

-- Creacion de las entidades 

CREATE TABLE aut.oauth_access_token
(
  token_id character varying(256),
  token bytea,
  authentication_id character varying(256) NOT NULL,
  user_name character varying(256),
  client_id character varying(256),
  authentication bytea,
  refresh_token character varying(256),
  CONSTRAINT oauth_access_token_pkey PRIMARY KEY (authentication_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aut.oauth_access_token OWNER TO postgres;
GRANT ALL ON TABLE aut.oauth_access_token TO postgres;
GRANT ALL ON TABLE aut.oauth_access_token TO sfinx_aut;



CREATE TABLE aut.oauth_approvals
(
  userid character varying(256),
  clientid character varying(256),
  scope character varying(256),
  status character varying(10),
  expiresat timestamp without time zone,
  lastmodifiedat timestamp without time zone
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aut.oauth_approvals OWNER TO postgres;
GRANT ALL ON TABLE aut.oauth_approvals TO postgres;
GRANT ALL ON TABLE aut.oauth_approvals TO sfinx_aut;



CREATE TABLE aut.oauth_client_details
(
  client_id character varying(255) NOT NULL,
  resource_ids character varying(255),
  client_secret character varying(255),
  scope character varying(255),
  authorized_grant_types character varying(255),
  web_server_redirect_uri character varying(255),
  authorities character varying(255),
  access_token_validity integer,
  refresh_token_validity integer,
  additional_information character varying(4096),
  autoapprove character varying(255),
  CONSTRAINT oauth_client_details_pkey PRIMARY KEY (client_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aut.oauth_client_details OWNER TO postgres;
GRANT ALL ON TABLE aut.oauth_client_details TO postgres;
GRANT ALL ON TABLE aut.oauth_client_details TO sfinx_aut;


CREATE TABLE aut.oauth_client_token
(
  token_id character varying(255),
  token bytea,
  authentication_id character varying(255),
  user_name character varying(255),
  client_id character varying(255)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aut.oauth_client_token OWNER TO postgres;
GRANT ALL ON TABLE aut.oauth_client_token TO postgres;
GRANT ALL ON TABLE aut.oauth_client_token TO sfinx_aut;


CREATE TABLE aut.oauth_code
(
  code character varying(255),
  authentication bytea
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aut.oauth_code OWNER TO postgres;
GRANT ALL ON TABLE aut.oauth_code TO postgres;
GRANT ALL ON TABLE aut.oauth_code TO sfinx_aut;


CREATE TABLE aut.oauth_refresh_token
(
  token_id character varying(256),
  token bytea,
  authentication bytea
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aut.oauth_refresh_token OWNER TO postgres;
GRANT ALL ON TABLE aut.oauth_refresh_token TO postgres;
GRANT ALL ON TABLE aut.oauth_refresh_token TO sfinx_aut;

INSERT INTO aut.oauth_client_details(
            client_id, resource_ids, client_secret, scope, authorized_grant_types, 
            web_server_redirect_uri, authorities, access_token_validity, 
            refresh_token_validity, additional_information, autoapprove)
    VALUES ('pdmm', 'pdmm', 'pdmm', 'read,write', 'implicit', 
            'http://localhost/,http://example.com/', 'ROLE_USER', NULL, 
            NULL, NULL, NULL);

CREATE TABLE aut.authorities
(
  id serial NOT NULL,
  authority character varying(255),
  CONSTRAINT authority_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aut.authorities OWNER TO postgres;
GRANT ALL ON TABLE aut.authorities TO postgres;
GRANT ALL ON TABLE aut.authorities TO sfinx_aut;

CREATE TABLE aut.users
(
  id serial NOT NULL,
  username character varying(128),
  password character varying(256),
  enabled boolean,
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT users_username_key UNIQUE (username)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aut.users OWNER TO postgres;
GRANT ALL ON TABLE aut.users TO postgres;
GRANT ALL ON TABLE aut.users TO sfinx_aut;


CREATE TABLE aut.users_authorities
(
  id serial NOT NULL,
  user_id bigint NOT NULL,
  authority_id bigint NOT NULL,
  CONSTRAINT users_authorities_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aut.users_authorities OWNER TO postgres;
GRANT ALL ON TABLE aut.users_authorities TO postgres;
GRANT ALL ON TABLE aut.users_authorities TO sfinx_aut;

INSERT INTO aut.authorities(id, authority) VALUES (1, 'ROLE_OAUTH_ADMIN');
INSERT INTO aut.authorities(id, authority) VALUES (2, 'ROLE_ADMIN');
INSERT INTO aut.authorities(id, authority) VALUES (3, 'ROLE_USER');

INSERT INTO aut.users(id, username, password, enabled) VALUES (1, 'admin@example.com', '$2a$10$D4OLKI6yy68crm.3imC9X.P2xqKHs5TloWUcr6z5XdOqnTrAK84ri', TRUE);
INSERT INTO aut.users(id, username, password, enabled) VALUES (2, 'moderator@example.com', '$2a$10$D4OLKI6yy68crm.3imC9X.P2xqKHs5TloWUcr6z5XdOqnTrAK84ri', TRUE);
INSERT INTO aut.users(id, username, password, enabled) VALUES (3, 'example@example.com', '$2a$10$D4OLKI6yy68crm.3imC9X.P2xqKHs5TloWUcr6z5XdOqnTrAK84ri', TRUE);

INSERT INTO aut.users_authorities(id, user_id, authority_id) VALUES (1, 1, 1);
INSERT INTO aut.users_authorities(id, user_id, authority_id) VALUES (2, 2, 2);
INSERT INTO aut.users_authorities(id, user_id, authority_id) VALUES (3, 3, 3);

ALTER SEQUENCE aut.users_id_seq RESTART WITH 3;
ALTER SEQUENCE aut.authorities_id_seq RESTART WITH 3;
ALTER SEQUENCE aut.users_authorities_id_seq RESTART WITH 3;
