DROP INDEX IF EXISTS "public"."sys_user_username_idx";
CREATE UNIQUE INDEX "idx_user_username" ON "public"."sys_user" USING btree (
	"username"
);
COMMENT ON INDEX "public"."idx_user_username" IS '用户_用户名_唯一索引';
