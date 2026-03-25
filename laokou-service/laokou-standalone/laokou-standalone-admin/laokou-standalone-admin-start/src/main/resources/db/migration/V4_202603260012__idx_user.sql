DROP INDEX IF EXISTS "public"."sys_user_username_tenantId_idx";
CREATE UNIQUE INDEX "sys_user_username_idx" ON "public"."sys_user" USING btree (
	"username"
);
COMMENT ON INDEX "public"."sys_user_username_idx" IS '用户名_唯一索引';
