package org.laokou.test.container;

import lombok.SneakyThrows;
import org.laokou.common.core.utils.TemplateUtil;

import java.util.Map;

public class GinCrudServiceGo implements Crud{
	public static void main(String[] args) {
		String inst = "pro";
		String upper = "Protocol";
		String lower = "protocol";
		Map<String, Object> params = Map.of("inst",inst, "upper",upper, "lower",lower);
		Crud crud = new GinCrudServiceGo();
		System.out.println();
		System.out.println(crud.imp(params));
		System.out.println(crud.type(params));
		System.out.println(crud.create(params));
		System.out.println(crud.remove(params));
		System.out.println(crud.modify(params));
		System.out.println(crud.find(params));
		System.out.println();
	}

	@Override
	@SneakyThrows
	public String create(Map<String, Object> params) {
		String str = """
func (service *${upper}Service) Create${upper}(${inst} system.AtSys${upper}) error {
	${inst}.CreateDate = time.Now()
	${inst}.UpdateDate = time.Now()
	return global.Db.Create(&${inst}).Error
}
			""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String remove(Map<String, Object> params) {
		String str = """
func (service *${upper}Service) Remove${upper}(id int) (err error) {
	var ${inst} system.AtSys${upper}
	return global.Db.Model(&${inst}).Where("id = ?", id).Update("del_flag", "1").Error
}
			""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String modify(Map<String, Object> params) {
		String str = """
func (service *${upper}Service) Modify${upper}(${inst} system.AtSys${upper}) (err error) {
	${lower}Map := map[string]interface{}{}
	return global.Db.Updates(${lower}Map).Error
}
			""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String imp(Map<String, Object> params) {
		String str = """
import (
	"lc-base-frame/global"
	"lc-base-frame/model/common/request"
	"lc-base-frame/model/system"
	"time"
)
			""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String find(Map<String, Object> params) {
		String str = """
func (service *${upper}Service) Find${upper}List(info request.PageInfo) (list interface{}, total int64, err error) {
	limit := info.PageSize
	offset := info.PageSize * (info.Page - 1)
	db := global.Db.Model(&system.AtSys${upper}{})
	var ${lower}List []system.AtSys${upper}
	err = db.Count(&total).Error
	if err != nil {
		return
	}
	err = db.Limit(limit).Offset(offset).Find(&${lower}List).Error
	return ${lower}List, total, err
}
			""";
		return TemplateUtil.getContent(str, params);
	}

	@Override
	@SneakyThrows
	public String type(Map<String, Object> params) {
		String str = """
type ${upper}Service struct{}
			""";
		return TemplateUtil.getContent(str, params);
	}
}
