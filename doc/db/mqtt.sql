-- https://docs.emqx.com/zh/emqx/latest/access-control/authn/postgresql.html
-- 密码加密方式：sha256
-- 加盐方式：suffix
CREATE TABLE mqtt_user (
   id serial PRIMARY KEY,
   username text NOT NULL UNIQUE,
   password_hash  text NOT NULL,
   salt text NOT NULL,
   is_superuser boolean DEFAULT false,
   created timestamp with time zone DEFAULT NOW()
);
-- 用户名/密码 => root/laokou123
INSERT INTO mqtt_user("id", username, password_hash, salt, is_superuser) VALUES (1, 'root', 'b1c641a40053b6b86d34df6fdfecf7dd60c6fa9370ca27a659e77483ebd0aee3', 'laokou', true);

CREATE unique INDEX "idx_username" ON "public"."mqtt_user" USING btree ("username");
COMMENT ON INDEX "public"."idx_username" IS '用户名_唯一索引';

-- https://docs.emqx.com/zh/emqx/latest/access-control/authz/postgresql.html
CREATE TABLE mqtt_acl(
	 id serial PRIMARY KEY,
	 username text NOT NULL,
	 permission text NOT NULL,
	 action text NOT NULL,
	 topic text NOT NULL,
	 qos smallint,
	 retain smallint
);
CREATE INDEX mqtt_acl_username_idx ON mqtt_acl(username);

INSERT INTO mqtt_acl (username,	permission,	action,	topic) VALUES ('root','allow','subscribe','$SYS/brokers/+/clients/#');
