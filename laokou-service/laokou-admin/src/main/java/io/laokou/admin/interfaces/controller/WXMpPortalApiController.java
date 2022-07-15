package io.laokou.admin.interfaces.controller;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
/**
 * 微信认证、回调处理
 * @author limingze
 * @create: 2022-07-13 11:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/wx/mp/portal/api/{appId}")
@Api(tags="微信认证API",value = "微信认证API",protocols = "http")
public class WXMpPortalApiController {

//    /**
//     * 服务端Token验证
//     */
//    @GetMapping(produces = "text/plain;charset=utf-8")
//    public String authGet(@PathVariable String appId,
//        @RequestParam(name = "signature", required = false) String signature,
//        @RequestParam(name = "timestamp", required = false) String timestamp,
//        @RequestParam(name = "nonce", required = false) String nonce,
//        @RequestParam(name = "echostr", required = false) String echostr) {
//
//        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}, {}]", appId, signature, timestamp, nonce, echostr);
//        if (StringUtils.isAnyBlank(appId, signature, timestamp, nonce, echostr)) {
//            throw new IllegalArgumentException("请求参数非法，请核实!");
//        }
//
//        if (!this.wxService.switchover(appId)) {
//            throw new IllegalArgumentException(String.format("未找到对应appId=[%s]的配置，请核实！", appId));
//        }
//
//        if (wxService.checkSignature(timestamp, nonce, signature)) {
//            return echostr;
//        }
//
//        return "非法请求";
//    }
//
//    /**
//     * 处理微信回调事件
//     */
//    @PostMapping(produces = "application/xml; charset=UTF-8")
//    public String post(@PathVariable String appId, @RequestBody String requestBody,
//                       @RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
//                       @RequestParam("nonce") String nonce, @RequestParam("openid") String openid,
//                       @RequestParam(name = "encrypt_type", required = false) String encType,
//                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
//
//        log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
//                + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
//            openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
//
//        if (!this.wxService.switchover(appId)) {
//            throw new IllegalArgumentException(String.format("未找到对应appId=[%s]的配置，请核实！", appId));
//        }
//
//        if (!wxService.checkSignature(timestamp, nonce, signature)) {
//            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
//        }
//
//        String out = null;
//        if (encType == null) {
//            // 明文传输的消息
//            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
//            WxMpXmlOutMessage outMessage = this.route(inMessage);
//            if (outMessage == null) {
//                return "";
//            }
//
//            out = outMessage.toXml();
//        } else if ("aes".equalsIgnoreCase(encType)) {
//            // aes加密的消息
//            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxService.getWxMpConfigStorage(),
//                timestamp, nonce, msgSignature);
//            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
//            WxMpXmlOutMessage outMessage = this.route(inMessage);
//            if (outMessage == null) {
//                return "";
//            }
//
//            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
//        }
//
//        log.debug("\n组装回复信息：{}", out);
//        return out;
//    }
//
//    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
//        try {
//            return this.messageRouter.route(message);
//        } catch (Exception e) {
//            log.error("路由消息时出现异常！", e);
//        }
//
//        return null;
//    }

}
