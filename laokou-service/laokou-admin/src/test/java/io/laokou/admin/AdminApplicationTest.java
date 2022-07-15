package io.laokou.admin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/14 0014 下午 9:20
 */

@SpringBootTest(classes = {AdminApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class AdminApplicationTest {

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void dynamicDatasourceTest() {
        System.out.println(sysUserService.getOne(Wrappers.emptyWrapper()));
    }

}
