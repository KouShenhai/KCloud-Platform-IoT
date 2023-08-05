package org.laokou.auth.command.query;

import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.domain.gateway.CaptchaGateway;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@RequiredArgsConstructor
public class CaptchaGetQryExe {

    private final CaptchaGateway captchaGateway;

    public Result<String> execute(String uuid) {
        // 三个参数分别为宽、高、位数
        Captcha captcha = new GifCaptcha(130, 48, 4);
        // 设置字体，有默认字体，可以不用设置
        captcha.setFont(new Font("Verdana", Font.PLAIN, 32));
        // 设置类型，纯数字、纯字母、字母数字混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        String code = captcha.text();
        captchaGateway.set(uuid,code);
        return Result.of(captcha.toBase64());
    }

}
