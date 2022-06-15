package io.laokou.admin.application.service;

import java.awt.image.BufferedImage;

/**
 * @author Kou Shenhai
 */
public interface CaptchaApplicationService {

    /**
     * 图片验证码
     * @param uuid
     * @return
     */
    BufferedImage createImage(String uuid);

    /**
     * 验证码效验
     * @param uuid
     * @param code
     * @return
     */
    boolean validate(String uuid,String code);
}
