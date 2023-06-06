package org.laokou.test.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.DateUtil;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@SpringBootTest
@Slf4j
class WebTestApplicationTests {

    @Test
    void contextLoads() {
        LocalDate now = LocalDate.now();
        log.info("{}",DateUtil.getDayOfWeek(now, DateUtil.MONDAY));
        log.info("{}",now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        log.info("{}",DateUtil.format(now,DateUtil.YYYY_MM_DD_TEXT));
        String str = "2023-01-01";
        log.info("{}",DateUtil.parseDate(str,DateUtil.YYYY_MM_DD));
    }

}
