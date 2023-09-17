package com.xxl.job.admin.core.conf;

import com.xxl.job.admin.core.alarm.JobAlarmer;
import com.xxl.job.admin.core.scheduler.XxlJobScheduler;
import com.xxl.job.admin.dao.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */

@Component
public class XxlJobAdminConfig implements InitializingBean, DisposableBean {

	private static XxlJobAdminConfig adminConfig = null;

	private static final int TRIGGER_POOL_FAST_MAX = 200;

	private static final int TRIGGER_POOL_SLOW_MAX = 100;

	private static final int INTENTIONALITIES = 7;

	private static final List<String> I18N = Arrays.asList("zh_CN", "zh_TC", "en");

	public static XxlJobAdminConfig getAdminConfig() {
		return adminConfig;
	}

	private XxlJobScheduler xxlJobScheduler;

	@Override
	public void afterPropertiesSet() throws Exception {
		adminConfig = this;

		xxlJobScheduler = new XxlJobScheduler();
		xxlJobScheduler.init();
	}

	@Override
	public void destroy() throws Exception {
		xxlJobScheduler.destroy();
	}

	@Value("${xxl.job.i18n}")
	private String i18n;

	@Value("${xxl.job.access-token}")
	private String accessToken;

	@Value("${spring.mail.from}")
	private String emailFrom;

	@Value("${xxl.job.trigger-pool.fast.max}")
	private int triggerPoolFastMax;

	@Value("${xxl.job.trigger-pool.slow.max}")
	private int triggerPoolSlowMax;

	@Value("${xxl.job.intentionalities}")
	private int intentionalities;

	@Resource
	private XxlJobLogDao xxlJobLogDao;

	@Resource
	private XxlJobInfoDao xxlJobInfoDao;

	@Resource
	private XxlJobRegistryDao xxlJobRegistryDao;

	@Resource
	private XxlJobGroupDao xxlJobGroupDao;

	@Resource
	private XxlJobLogReportDao xxlJobLogReportDao;

	@Resource
	private JavaMailSender mailSender;

	@Resource
	private DataSource dataSource;

	@Resource
	private JobAlarmer jobAlarmer;

	public String getI18n() {
		if (!I18N.contains(i18n)) {
			return I18N.get(0);
		}
		return i18n;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public int getTriggerPoolFastMax() {
		if (triggerPoolFastMax < TRIGGER_POOL_FAST_MAX) {
			return TRIGGER_POOL_FAST_MAX;
		}
		return triggerPoolFastMax;
	}

	public int getTriggerPoolSlowMax() {
		if (triggerPoolSlowMax < TRIGGER_POOL_SLOW_MAX) {
			return TRIGGER_POOL_SLOW_MAX;
		}
		return triggerPoolSlowMax;
	}

	public int getIntentionalities() {
		if (intentionalities < INTENTIONALITIES) {
			// Limit greater than or equal to 7, otherwise close
			return -1;
		}
		return intentionalities;
	}

	public XxlJobLogDao getXxlJobLogDao() {
		return xxlJobLogDao;
	}

	public XxlJobInfoDao getXxlJobInfoDao() {
		return xxlJobInfoDao;
	}

	public XxlJobRegistryDao getXxlJobRegistryDao() {
		return xxlJobRegistryDao;
	}

	public XxlJobGroupDao getXxlJobGroupDao() {
		return xxlJobGroupDao;
	}

	public XxlJobLogReportDao getXxlJobLogReportDao() {
		return xxlJobLogReportDao;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public JobAlarmer getJobAlarmer() {
		return jobAlarmer;
	}

}
