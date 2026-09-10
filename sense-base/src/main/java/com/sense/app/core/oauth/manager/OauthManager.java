package com.sense.app.core.oauth.manager;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.sense.app.core.exception.BizException;
import com.sense.app.core.exception.NoAuthException;
import com.sense.app.core.oauth.dto.CaptchaCode;
import com.sense.app.core.oauth.dto.TokenResponse;
import com.sense.app.core.oauth.dto.TokenResquest;
import com.sense.app.core.oauth.dto.WxTokenRequest;
import com.sense.app.core.user.domain.SysUser;
import com.sense.app.core.user.domain.WxUser;
import com.sense.app.core.user.service.CustomUserService;
import com.sense.app.core.user.service.WxUserService;
import com.sense.app.core.utils.CurrentUser;
import com.sense.app.core.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional(rollbackFor = Exception.class)
public class OauthManager {

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private SmsCodeManager smsCodeManager;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 创建验证码
     */
    public CaptchaCode createCaptchaCode(){
        // 生成验证码
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);

        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(80, 40);
        captcha.setGenerator(randomGenerator);
        // 重新生成code
        captcha.createCode();

        String uuid = IdWorker.get32UUID();
        String image = captcha.getImageBase64Data();

        // 缓存, 有效期5分钟
        redisTemplate.opsForValue().set("SENSE:CAPTCHA_CODE:" + uuid, captcha.getCode());
        redisTemplate.expire("SENSE:CAPTCHA_CODE:" + uuid, 5, TimeUnit.MINUTES);

        return CaptchaCode.builder().id(uuid).image(image).build();
    }

    public void verifyCaptchaCode(String id, String code){
        String redisCode = redisTemplate.opsForValue().get("SENSE:CAPTCHA_CODE:" + id);
        if (null == redisCode){
            throw new BizException("图片验证码已过期，请点击图片刷新");
        }

        if (!code.equals(redisCode)){
            throw new BizException("图片验证码填写错误");
        }
    }

    /**
     * 发送登录认证短信
     * @param phone 手机号
     */
    public void sendSmsCode(String phone, String captchaId, String captchaCode){
        // 校验验证码
        verifyCaptchaCode(captchaId, captchaCode);

        // 发送短信
        String code = smsCodeManager.sendSmsCode(phone);

        // 缓存, 有效期5分钟
        redisTemplate.opsForValue().set("SENSE:SMS_CODE:" + phone, code, 5, TimeUnit.MINUTES);
        redisTemplate.expire("SENSE:SMS_CODE:" + phone, 5, TimeUnit.MINUTES);
    }

    public void verifySmsCode(String phone, String code){
        String redisCode = redisTemplate.opsForValue().get("SENSE:SMS_CODE:" + phone);
        if (null == redisCode){
            throw new BizException("短信验证码已过期，请重新发送");
        }

        if (!code.equals(redisCode)){
            throw new BizException("短信验证码填写错误");
        }
    }

    /**
     * 微信端获取Token
     */
    public TokenResponse getTokenFromWx(WxTokenRequest tokenRequest){
        // 验证徒刑验证码
        verifyCaptchaCode(tokenRequest.getCaptchaId(), tokenRequest.getCaptchaCode());

        // 验证手机验证码
        verifySmsCode(tokenRequest.getPhone(), tokenRequest.getSmsCode());

        // 尝试查找用户
        WxUser wxUser = wxUserService.queryWxUser(tokenRequest.getPhone());
        if (null == wxUser){
            // 自动创建用户
            wxUser = wxUserService.autoRegister(null, tokenRequest.getPhone());
        }

        // 补全用户信息
        SysUser sysUser = wxUserService.buildFullUserInfo(wxUser, tokenRequest.getTenantCode());

        StpUtil.login(sysUser.getId());
        StpUtil.getTokenSession().set("user", JSONUtil.toJsonStr(sysUser));
        return TokenResponse.builder().access_token(StpUtil.getTokenValue()).build();
    }

    /**
     * web端获取Token
     */
    public TokenResponse getToken(TokenResquest tokenRequest){
        // 验证用户名密码

        // 尝试查找用户
        SysUser sysUser = customUserService.querySysUser(tokenRequest.getUsername());
        if (null==sysUser){
            throw new NoAuthException("用户不存在");
        }

        // 检查密码
        if (!sysUser.getPassword().equals(PasswordUtils.encryptPassword(tokenRequest.getPassword()))){
            throw new NoAuthException("密码错误！");
        }


        // 获取用户全量信息
        sysUser = customUserService.buildFullUserInfo(sysUser);

        // 登录成功,设置缓存
        StpUtil.login(sysUser.getId());
        StpUtil.getTokenSession().set("user", JSONUtil.toJsonStr(sysUser));

        return TokenResponse.builder().access_token(StpUtil.getTokenValue()).build();
    }

    public SysUser checkToken(){
        SysUser user = CurrentUser.getUser();
        if (null==user){
            throw new NoAuthException("登录失效，请重新登录");
        }

        return user;
    }

    /**
     * 注销token
     */
    public void revokeToken(){
        StpUtil.logout();
    }
}
