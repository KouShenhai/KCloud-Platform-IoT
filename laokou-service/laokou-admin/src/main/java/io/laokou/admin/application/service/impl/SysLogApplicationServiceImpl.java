package io.laokou.admin.application.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.laokou.admin.application.service.SysLogApplicationService;
import io.laokou.admin.domain.sys.entity.SysLoginLogDO;
import io.laokou.admin.domain.sys.entity.SysOperateLogDO;
import io.laokou.admin.domain.sys.repository.service.SysLoginLogService;
import io.laokou.admin.domain.sys.repository.service.SysOperateLogService;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.admin.interfaces.qo.LoginLogQO;
import io.laokou.admin.interfaces.qo.SysOperateLogQO;
import io.laokou.admin.interfaces.vo.SysLoginLogVO;
import io.laokou.admin.interfaces.vo.SysOperateLogVO;
import io.laokou.common.dto.LoginLogDTO;
import io.laokou.common.dto.OperateLogDTO;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.AddressUtil;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.common.utils.DateUtil;
import io.laokou.common.utils.HttpUtil;
import io.laokou.datasource.annotation.DataFilter;
import io.laokou.datasource.annotation.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysLogApplicationServiceImpl implements SysLogApplicationService {

    @Autowired
    private SysOperateLogService sysOperateLogService;

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    @DataSource("master")
    public Boolean insertOperateLog(OperateLogDTO dto) {
        SysOperateLogDO logDO = ConvertUtil.sourceToTarget(dto, SysOperateLogDO.class);
        final UserDetail userDetail = sysUserService.getUserDetail(logDO.getCreator());
        logDO.setDeptId(userDetail.getDeptId());
        return sysOperateLogService.save(logDO);
    }

    @Override
    @DataSource("master")
    public Boolean insertLoginLog(LoginLogDTO dto) {
        SysLoginLogDO logDO = ConvertUtil.sourceToTarget(dto, SysLoginLogDO.class);
        return sysLoginLogService.save(logDO);
    }

    @Override
    @DataSource("master")
    @DataFilter(tableAlias = "boot_sys_operate_log")
    public IPage<SysOperateLogVO> queryOperateLogPage(SysOperateLogQO qo) {
        IPage<SysOperateLogVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysOperateLogService.getOperateLogList(page,qo);
    }

    @Override
    @DataSource("master")
    public IPage<SysLoginLogVO> queryLoginLogPage(LoginLogQO qo) {
        IPage<SysLoginLogVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysLoginLogService.getLoginLogList(page,qo);
    }

    @Override
    public void test() throws IOException, ParseException {
        String url = "https://ruoyi.setworld.net/api/monitor/logininfor/list";
        final HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjY4Njk1OWIwLTlmOTktNDgxYy1hMDdlLTI5NTA2NGQ5MmZkNSJ9.3OY_N6U3CHWlqYvnzivaMyrJU5rhb1i243gzIHqjJiuehd_ge84whUYEgZhIigRwa6_XSDywV1RTUhTURa52yQ");
        final String s = HttpUtil.doGet(url, new HashMap<>(0), objectObjectHashMap);
        JSONObject json = JSONObject.parseObject(s);
        final String rows = json.getString("rows");
        final List<Test> tests = JSON.parseArray(rows, Test.class);
        List<SysLoginLogDO> list = Lists.newArrayList();
        for (int i = 0; i < tests.size(); i++) {
            final Test test = tests.get(i);
            SysLoginLogDO sysLoginLogDO = new SysLoginLogDO();
            sysLoginLogDO.setRequestStatus(Integer.valueOf(test.getStatus()));
            sysLoginLogDO.setLoginName(test.getUserName());
            sysLoginLogDO.setMsg(test.getMsg());
            sysLoginLogDO.setOs(test.getOs());
            sysLoginLogDO.setRequestIp(test.getIpaddr());
            sysLoginLogDO.setCreateDate(DateUtils.parseDate(test.getLoginTime(),DateUtil.DATE_TIME_PATTERN));
            sysLoginLogDO.setBrowser(test.getBrowser());
            //sysLoginLogDO.setRequestAddress(AddressUtil.getRealAddress(test.getIpaddr().split(",")[0]));
            list.add(sysLoginLogDO);
        }
        sysLoginLogService.saveBatch(list);
    }

    static class Test {
        private String searchValue;
       private String createBy;
       private String createTime;
       private String updateBy;
       private String updateTime;
       private String remark;
       private String params;
       private String infoId;
       private String userName;
       private String status;
       private String ipaddr;
       private String loginLocation;
       private String browser;
       private String os;
       private String msg;
       private String loginTime;

        public String getSearchValue() {
            return searchValue;
        }

        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public String getInfoId() {
            return infoId;
        }

        public void setInfoId(String infoId) {
            this.infoId = infoId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIpaddr() {
            return ipaddr;
        }

        public void setIpaddr(String ipaddr) {
            this.ipaddr = ipaddr;
        }

        public String getLoginLocation() {
            return loginLocation;
        }

        public void setLoginLocation(String loginLocation) {
            this.loginLocation = loginLocation;
        }

        public String getBrowser() {
            return browser;
        }

        public void setBrowser(String browser) {
            this.browser = browser;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }
    }

}
