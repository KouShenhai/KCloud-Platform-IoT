package io.laokou.admin.application.service;

import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchForm;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchVO;

public interface SysSearchApplicationService {

    SearchVO searchResource(SearchForm form);

}
