package com.qf.controller;

import static com.qf.constant.SsmConstant.*;
import com.qf.pojo.User;
import com.qf.service.UserService;
import com.qf.util.SendSMSUtil;
import com.qf.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户模块的controller层
 * create by 郑大仙丶
 * 2019/7/15 10:27
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 发短信工具类.
    @Autowired
    private SendSMSUtil sendSMSUtil;


    // 跳转到注册页面
    @GetMapping("/register-ui")
    public String registerUI(){
        // 转发到注册页面.
        return "user/register";
    }

//    Request URL: http://localhost/user/check-username
//    Request Method: POST
//    {"username":"admin"}   <json>
    //1. json数据需要反序列化成pojo对象.
    // jsonlib              比较古老的json工具.        第三方依赖过多.        当POJO类过于复杂时,序列化可能会出现问题.
    // jackson              spring默认使用的工具.      三个依赖.              当POJO类过于复杂时,序列化可能会出现问题.
    // gson                 google的json工具.          一个依赖.
    // jackson和gson和spring framework可以0配置整合.
    // fastJSON             阿里巴巴的json工具.        一个依赖.              当POJO类过于复杂时,序列化可能会出现问题.
    // fastJSON号称自己是最快的json工具.

    // 页面发送json数据时,接收的数据类型 ->   map,pojo类.
    @PostMapping("/check-username")
    @ResponseBody       // 不走视图解析器,直接响应.   如果返回结果为Map/pojo类,自动序列化成json.
    public ResultVO checkUsername(@RequestBody User user){
        //1. 调用service,判断用户名是否可用.
        Integer count = userService.checkUsername(user.getUsername());

        //2. 使用ResultVO响应数据
        return new ResultVO(0,"成功",count);


        /*
        使用Map响应
        //2. 封装响应数据的Map
        Map<String,Object> result = new HashMap<>();
        result.put("code",0);
        result.put("msg","成功");
        result.put("data",count);

        //3. 返回
        return result;
        */
    }




    //发送短信
//    Request URL: http://localhost/user/send-sms
//    Request Method: POST
    @PostMapping(value = "/send-sms",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String sendSMS(@RequestParam String phone, HttpSession session){
        //1. 调用工具发短信.
        String result = sendSMSUtil.sendSMS(session, phone);
        //2. 将result响应给ajax引擎.
        return result;
    }

    // 执行注册
//    Request URL: http://localhost/user/register
//    Request Method: POST
    @PostMapping("/register")
    public String register(@Valid User user, BindingResult bindingResult, String registerCode, HttpSession session, RedirectAttributes redirectAttributes){
        //1. 校验验证码.
        if(!StringUtils.isEmpty(registerCode)){
            // 验证码不为空,
            String code = session.getAttribute(CODE) +"";
            if(!registerCode.equals(code)){
                // 验证码不正确
                redirectAttributes.addAttribute("registerInfo","验证码错误!");
                return REDIRECT + REGISTER_UI;
            }
        }
        //2. 校验参数是否合法.
        if(bindingResult.hasErrors() || StringUtils.isEmpty(registerCode)){
            // 参数不合法.
            String msg = bindingResult.getFieldError().getDefaultMessage();
            if(StringUtils.isEmpty(msg)){
                msg = "验证码为必填项,岂能不填!";
            }
            redirectAttributes.addAttribute("registerInfo",msg);
            return REDIRECT + REGISTER_UI;
        }
        //3. 调用service执行注册功能.
        Integer count = userService.register(user);
        //4. 根据service返回的结果跳转到指定页面.
        if(count == 1){
            // 注册成功.
            return REDIRECT + LOGIN_UI;
        }else{
            // 注册失败
            redirectAttributes.addAttribute("registerInfo","服务器爆炸了!!");
            return REDIRECT + REGISTER_UI;
        }
    }




    @RequestMapping("/login-ui")
    public String loginUI(){
        return "user/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultVO login(String username,String password,HttpSession session) {
        //1. 校验参数是否合法.
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            //参数不合法
            return new ResultVO(1, "用户名和密码为必填项，岂能为空？", null);
        }
        //2. 调用service执行登录.
        User user =  userService.login(username, password);
        //3. 根据service返回结果判断登录是否成功.
        if (user != null) {
            //4. 如果成功,讲用户的信息放到session域中.
            session.setAttribute("USER",user);
            return new ResultVO(0,"登陆成功",null);
        } else {
            //5. 如果失败,响应错误信息给ajax引擎.
            return new ResultVO(2,"用户名或密码错误",null);
        }
    }






















}
