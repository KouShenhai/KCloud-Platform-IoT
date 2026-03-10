-- liquibase formatted sql

-- changeset dev:000003
UPDATE "public".sys_menu SET "name" = '国际化菜单', "path" = '/sys/base/i18nMenu' where id = 29;
