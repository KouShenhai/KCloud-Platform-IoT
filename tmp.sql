select distinct id from (

	with recursive dept_tree as (
		select id, pid
		from sys_dept
		where id = 1584488175088373761
		union all
		select d.id, d.pid
		from sys_dept d
		join dept_tree dt on d.pid = dt.id
	)
	select id from dept_tree

	union all

	select 1584488175088373761

	union all

	select rd.dept_id from sys_role_dept rd
	join sys_user_role ur on ur.role_id = rd.role_id
	where ur.user_id = 1

);

select distinct data_scope from sys_role r
join sys_user_role ur on ur.role_id = r.id
where ur.user_id = 1
