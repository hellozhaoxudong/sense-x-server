package com.sense.app.core.oauth.web;

import com.sense.app.core.oauth.dto.CaptchaCode;
import com.sense.app.core.oauth.dto.TokenResponse;
import com.sense.app.core.oauth.dto.TokenResquest;
import com.sense.app.core.oauth.dto.WxTokenRequest;
import com.sense.app.core.oauth.manager.OauthManager;
import com.sense.app.core.user.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName SysRoleController
 * @description 角色管理接口
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
@RestController
@RequestMapping("/api/sense/oauth")
public class OauthController {

    @Autowired
    private OauthManager service;

    /**
     * 获取Token
     */
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getToken(@RequestBody TokenResquest tokenRequest){
        return new ResponseEntity(service.getToken(tokenRequest), HttpStatus.OK);
    }

    /**
     * 微信端获取Token
     */
    @PostMapping("/wx/token")
    public ResponseEntity<TokenResponse> getTokenFromWx(@RequestBody WxTokenRequest tokenRequest){
        return new ResponseEntity(service.getTokenFromWx(tokenRequest), HttpStatus.OK);
    }

    /**
     * 创建验证码
     */
    @GetMapping("/captcha_code/create")
    public ResponseEntity<CaptchaCode> createCaptchaCode(){
        return new ResponseEntity(service.createCaptchaCode(), HttpStatus.OK);
    }

    /**
     * 发送短信
     */
    @GetMapping("/sms_code/send")
    public ResponseEntity sendSmsCode(@RequestParam("phone") String phone,
                                      @RequestParam("captchaId") String captchaId,
                                      @RequestParam("captchaCode") String captchaCode){
        service.sendSmsCode(phone, captchaId, captchaCode);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 验证token
     */
    @GetMapping("/check_token")
    public ResponseEntity<SysUser> checkToken(){
        return new ResponseEntity(service.checkToken(), HttpStatus.OK);
    }

    /**
     * 注销token
     */
    @GetMapping("/revoke")
    public ResponseEntity revokeToken(){
        service.revokeToken();
        return new ResponseEntity(HttpStatus.OK);
    }
}