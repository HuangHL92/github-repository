package com.ruoyi.web.controller.wap;

import com.ruoyi.config.WxMpConfig;
import com.ruoyi.demo.service.WeixinService;
import com.ruoyi.framework.web.base.BaseController;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * 移动端
 *
 * @author tao.liang
 * @date 2019-02-26
 */
@Controller
@RequestMapping("/wap/wechat")
public class WechatController extends BaseController {

    private String prefix = "wap/wechat";

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private WxMpConfig wxMpConfig;

    /**
     * 请求微信服务器认证接口
     * @return
     */
    @GetMapping("authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
        // 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
        // snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
        String redirectUri = weixinService.oauth2buildAuthorizationUrl(wxMpConfig.getWebPath() + "/wap/wechat/userInfo",
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(wxMpConfig.getWebPath() + returnUrl));
        return "redirect:" + redirectUri;
    }

    /**
     * 换取微信用户openId&&获得用户信息
     * @param code
     * @param returnUrl
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code, @RequestParam("state") String returnUrl) throws WxErrorException {
        // 获得access token
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = weixinService.oauth2getAccessToken(code);
        // 获得用户基本信息
        WxMpUser wxMpUser = weixinService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        //获取openId
        String openId = wxMpUser.getOpenId();
        String nickName = URLEncoder.encode(wxMpUser.getNickname());
        String headImgUrl = wxMpUser.getHeadImgUrl();
        return "redirect:" + returnUrl + "?openId=" + openId + "&nickName=" + nickName + "&headImgUrl=" + headImgUrl;
    }

    /**
     * 首页
     * @return
     */
    @RequestMapping(value = "index")
    public String list(@RequestParam("openId") String openId,
                       @RequestParam("nickName") String nickName,
                       @RequestParam("headImgUrl") String headImgUrl,
                       Model model) {
        model.addAttribute("openId", openId);
        model.addAttribute("nickName", URLDecoder.decode(nickName));
        model.addAttribute("headImgUrl", headImgUrl);
        return prefix + "/index";
    }

}
