package com.qf.controller;

import com.qf.pojo.Item;
import com.qf.service.ItemService;
import com.qf.util.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.qf.constant.SsmConstant.ITEM_ADD_UI;
import static com.qf.constant.SsmConstant.REDIRECT;

@Controller
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    //商品首页
    @GetMapping("/list")
    public String list(String name,
                       @RequestParam(value = "page",defaultValue = "1")Integer page,
                       @RequestParam(value = "size",defaultValue = "5")Integer size,
                       Model model){

        //1. 调用service查询数据
        PageInfo<Item> pageInfo =  itemService.findItemByNameLikeAndLimit(name,page,size);

        //2. controller将pageInfo放到request域中,将接收到的商品名称name也放到request域中.
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("name",name);
        //3. 转发页面

        return "item/item_list";
    }
    // 跳转到添加商品页面
    // /item/add-ui
    @GetMapping("/add-ui")
    public String  addUI(String addInfo,Model model){
        model.addAttribute("addInfo",addInfo);
        return "item/item_add";
    }
    @Value("${pic_types}")
    public String picType;

    //跳转到商品添加界面
    @PostMapping("/add")
    public String add(@Valid Item item, BindingResult bindingResult,
                      RedirectAttributes redirectAttributes,MultipartFile picFile, HttpServletRequest req) throws IOException {
        //============================= 校验参数 ===============================
        //=====================================================================
        if (bindingResult.hasErrors()){
            //获得具体错误信息
            String msg = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addAttribute("addInfo",msg);
            return REDIRECT + ITEM_ADD_UI;
        }
        //===================== 图片上传 =======================================
        //=====================================================================
        //=====================================================================
        //  1. 对图片的大小做校验.  要求图片小于5M.   5242880
        long size = picFile.getSize();
        if(size > 5242880L){
        //  TODO 图片过大
            redirectAttributes.addAttribute("addInfo","图片过大,要求小于5M");
            return REDIRECT + ITEM_ADD_UI;
        }
        boolean flag = false;
        //2. 对图片的类型做校验.   jpg.   png.   gif.
        String[] types = picType.split(",");
        for (String type : types) {
            if(StringUtils.endsWithIgnoreCase(picFile.getOriginalFilename(),type)){
                // 图片类型正确.
                flag = true;
                break;
            }
        }
        if(!flag){
        //TODO 图片类型不正确
            redirectAttributes.addAttribute("addInfo","图片类型不正确,要求" + picType);
            return REDIRECT + ITEM_ADD_UI;
        }
        // 3. 校验图片是否损坏.
        BufferedImage image = ImageIO.read(picFile.getInputStream());
        if(image == null){
        //TODO 图片已经损坏
            redirectAttributes.addAttribute("addInfo","图片已经损坏");
            return REDIRECT + ITEM_ADD_UI;
        }
        //===========================将图片保存到本地===fastDFS=========================================
        //1. 给图片起一个新名字
        String prefix = UUID.randomUUID().toString();
        String suffix = StringUtils.substringAfterLast(picFile.getOriginalFilename(),".");
        String newName = prefix + "." + suffix;
        //2. 将图片保存到本地
        String path = req.getServletContext().getRealPath("") + "\\static\\img\\" + newName;
        File file = new File(path);
        // 健壮性判断.
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        picFile.transferTo(file);
        //3. 封装图片的访问路径.
        String pic = "http://localhost/static/img/" + newName;
        //======================================================================================================
        //=============================================================================
        //=========================保存商品信息=======================================

        item.setPic(pic);


        //调用service 保存商品信息
        Integer count = itemService.save(item);
        //判断count
        if(count==1){
            return REDIRECT + "/item/list";
        }else {
//            添加商品信息失败
            redirectAttributes.addAttribute("addInfo","添加商品信息失败!");
            return REDIRECT + ITEM_ADD_UI;
        }

    }

}
