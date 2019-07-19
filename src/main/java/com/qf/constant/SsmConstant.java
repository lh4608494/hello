package com.qf.constant;

/**
 * create by 郑大仙丶
 * 2019/7/15 16:32
 */
public interface SsmConstant {

    /** 重定向 */
    String REDIRECT = "redirect:";

    /** 放在session域中验证码的key. */
    String CODE = "code";

    /**放在session域中的用户信息*/
    String USER = "user";

    /** 跳转到注册页面的路径 */
    String REGISTER_UI = "/user/register-ui";

    /** 跳转到登录页面的路径 */
    String LOGIN_UI = "/user/login-ui";
    /** 跳转到添加页面 */
    String ITEM_ADD_UI = "/item/add-ui";
}
