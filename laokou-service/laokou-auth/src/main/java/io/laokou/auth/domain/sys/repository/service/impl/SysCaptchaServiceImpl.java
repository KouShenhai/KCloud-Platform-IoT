package io.laokou.auth.domain.sys.repository.service.impl;
import com.google.code.kaptcha.Producer;
import io.laokou.auth.domain.sys.repository.service.SysCaptchaService;
import io.laokou.common.utils.RedisKeyUtil;
import io.laokou.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * 验证码实现类
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/12/19 0019 下午 7:23
 */
@Service
public class SysCaptchaServiceImpl implements SysCaptchaService {

    @Autowired
    private Producer producer;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public BufferedImage createImage(String uuid) {
        //生成文字验证码
        String code = producer.createText();

        //保存到缓存
        setCache(uuid,code);
        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        //获取验证码
        String captcha = getCache(uuid);
        //效验成功
        if (code.equalsIgnoreCase(captcha)) {
            return true;
        }
        return false;
    }

    private void setCache(String key,String value) {
        key = RedisKeyUtil.getUserCaptchaKey(key);
        //保存五分钟
        redisUtil.set(key, value,60 * 5);
    }

    private String getCache(String uuid) {
        String key = RedisKeyUtil.getUserCaptchaKey(uuid);
        Object captcha = redisUtil.get(key);
        if (captcha != null) {
            redisUtil.delete(key);
        }
        return Optional.of(captcha).orElse("").toString();
    }

}
