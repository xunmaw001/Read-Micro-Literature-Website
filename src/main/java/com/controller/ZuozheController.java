
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import org.springframework.beans.BeanUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.ContextLoader;
import javax.servlet.ServletContext;
import com.service.TokenService;
import com.utils.*;
import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 作者
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/zuozhe")
public class ZuozheController {
    private static final Logger logger = LoggerFactory.getLogger(ZuozheController.class);

    @Autowired
    private ZuozheService zuozheService;


    @Autowired
    private TokenService tokenService;
    @Autowired
    private DictionaryService dictionaryService;

    //级联表service

    @Autowired
    private YonghuService yonghuService;


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        else if("作者".equals(role))
            params.put("zuozheId",request.getSession().getAttribute("userId"));
        if(params.get("orderBy")==null || params.get("orderBy")==""){
            params.put("orderBy","id");
        }
        PageUtils page = zuozheService.queryPage(params);

        //字典表数据转换
        List<ZuozheView> list =(List<ZuozheView>)page.getList();
        for(ZuozheView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ZuozheEntity zuozhe = zuozheService.selectById(id);
        if(zuozhe !=null){
            //entity转view
            ZuozheView view = new ZuozheView();
            BeanUtils.copyProperties( zuozhe , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody ZuozheEntity zuozhe, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,zuozhe:{}",this.getClass().getName(),zuozhe.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<ZuozheEntity> queryWrapper = new EntityWrapper<ZuozheEntity>()
            .eq("username", zuozhe.getUsername())
            .or()
            .eq("zuozhe_phone", zuozhe.getZuozhePhone())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ZuozheEntity zuozheEntity = zuozheService.selectOne(queryWrapper);
        if(zuozheEntity==null){
            zuozhe.setCreateTime(new Date());
            zuozhe.setPassword("123456");
            zuozheService.insert(zuozhe);
            return R.ok();
        }else {
            return R.error(511,"账户或者作者手机号已经被使用");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ZuozheEntity zuozhe, HttpServletRequest request){
        logger.debug("update方法:,,Controller:{},,zuozhe:{}",this.getClass().getName(),zuozhe.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        //根据字段查询是否有相同数据
        Wrapper<ZuozheEntity> queryWrapper = new EntityWrapper<ZuozheEntity>()
            .notIn("id",zuozhe.getId())
            .andNew()
            .eq("username", zuozhe.getUsername())
            .or()
            .eq("zuozhe_phone", zuozhe.getZuozhePhone())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ZuozheEntity zuozheEntity = zuozheService.selectOne(queryWrapper);
        if("".equals(zuozhe.getZuozhePhoto()) || "null".equals(zuozhe.getZuozhePhoto())){
                zuozhe.setZuozhePhoto(null);
        }
        if(zuozheEntity==null){
            zuozheService.updateById(zuozhe);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"账户或者作者手机号已经被使用");
        }
    }

    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        zuozheService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        try {
            List<ZuozheEntity> zuozheList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            ZuozheEntity zuozheEntity = new ZuozheEntity();
//                            zuozheEntity.setUsername(data.get(0));                    //账户 要改的
//                            //zuozheEntity.setPassword("123456");//密码
//                            zuozheEntity.setZuozheName(data.get(0));                    //作者姓名 要改的
//                            zuozheEntity.setZuozhePhone(data.get(0));                    //作者手机号 要改的
//                            zuozheEntity.setZuozhePhoto("");//照片
//                            zuozheEntity.setSexTypes(Integer.valueOf(data.get(0)));   //性别 要改的
//                            zuozheEntity.setZuozheEmail(data.get(0));                    //电子邮箱 要改的
//                            zuozheEntity.setNewMoney(data.get(0));                    //余额 要改的
//                            zuozheEntity.setCreateTime(date);//时间
                            zuozheList.add(zuozheEntity);


                            //把要查询是否重复的字段放入map中
                                //账户
                                if(seachFields.containsKey("username")){
                                    List<String> username = seachFields.get("username");
                                    username.add(data.get(0));//要改的
                                }else{
                                    List<String> username = new ArrayList<>();
                                    username.add(data.get(0));//要改的
                                    seachFields.put("username",username);
                                }
                                //作者手机号
                                if(seachFields.containsKey("zuozhePhone")){
                                    List<String> zuozhePhone = seachFields.get("zuozhePhone");
                                    zuozhePhone.add(data.get(0));//要改的
                                }else{
                                    List<String> zuozhePhone = new ArrayList<>();
                                    zuozhePhone.add(data.get(0));//要改的
                                    seachFields.put("zuozhePhone",zuozhePhone);
                                }
                        }

                        //查询是否重复
                         //账户
                        List<ZuozheEntity> zuozheEntities_username = zuozheService.selectList(new EntityWrapper<ZuozheEntity>().in("username", seachFields.get("username")));
                        if(zuozheEntities_username.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(ZuozheEntity s:zuozheEntities_username){
                                repeatFields.add(s.getUsername());
                            }
                            return R.error(511,"数据库的该表中的 [账户] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                         //作者手机号
                        List<ZuozheEntity> zuozheEntities_zuozhePhone = zuozheService.selectList(new EntityWrapper<ZuozheEntity>().in("zuozhe_phone", seachFields.get("zuozhePhone")));
                        if(zuozheEntities_zuozhePhone.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(ZuozheEntity s:zuozheEntities_zuozhePhone){
                                repeatFields.add(s.getZuozhePhone());
                            }
                            return R.error(511,"数据库的该表中的 [作者手机号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        zuozheService.insertBatch(zuozheList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }


    /**
    * 登录
    */
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        ZuozheEntity zuozhe = zuozheService.selectOne(new EntityWrapper<ZuozheEntity>().eq("username", username));
        if(zuozhe==null || !zuozhe.getPassword().equals(password))
            return R.error("账号或密码不正确");
        //  // 获取监听器中的字典表
        // ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
        // Map<String, Map<Integer, String>> dictionaryMap= (Map<String, Map<Integer, String>>) servletContext.getAttribute("dictionaryMap");
        // Map<Integer, String> role_types = dictionaryMap.get("role_types");
        // role_types.get(.getRoleTypes());
        String token = tokenService.generateToken(zuozhe.getId(),username, "zuozhe", "作者");
        R r = R.ok();
        r.put("token", token);
        r.put("role","作者");
        r.put("username",zuozhe.getZuozheName());
        r.put("tableName","zuozhe");
        r.put("userId",zuozhe.getId());
        return r;
    }

    /**
    * 注册
    */
    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody ZuozheEntity zuozhe){
//    	ValidatorUtils.validateEntity(user);
        Wrapper<ZuozheEntity> queryWrapper = new EntityWrapper<ZuozheEntity>()
            .eq("username", zuozhe.getUsername())
            .or()
            .eq("zuozhe_phone", zuozhe.getZuozhePhone())
            ;
        ZuozheEntity zuozheEntity = zuozheService.selectOne(queryWrapper);
        if(zuozheEntity != null)
            return R.error("账户或者作者手机号已经被使用");
        zuozhe.setNewMoney(0.0);
        zuozhe.setCreateTime(new Date());
        zuozheService.insert(zuozhe);
        return R.ok();
    }

    /**
     * 重置密码
     */
    @GetMapping(value = "/resetPassword")
    public R resetPassword(Integer  id){
        ZuozheEntity zuozhe = new ZuozheEntity();
        zuozhe.setPassword("123456");
        zuozhe.setId(id);
        zuozheService.updateById(zuozhe);
        return R.ok();
    }


    /**
     * 忘记密码
     */
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        ZuozheEntity zuozhe = zuozheService.selectOne(new EntityWrapper<ZuozheEntity>().eq("username", username));
        if(zuozhe!=null){
            zuozhe.setPassword("123456");
            boolean b = zuozheService.updateById(zuozhe);
            if(!b){
               return R.error();
            }
        }else{
           return R.error("账号不存在");
        }
        return R.ok();
    }


    /**
    * 获取用户的session用户信息
    */
    @RequestMapping("/session")
    public R getCurrZuozhe(HttpServletRequest request){
        Integer id = (Integer)request.getSession().getAttribute("userId");
        ZuozheEntity zuozhe = zuozheService.selectById(id);
        if(zuozhe !=null){
            //entity转view
            ZuozheView view = new ZuozheView();
            BeanUtils.copyProperties( zuozhe , view );//把实体数据重构到view中

            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }
    }


    /**
    * 退出
    */
    @GetMapping(value = "logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }




    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        // 没有指定排序字段就默认id倒序
        if(StringUtil.isEmpty(String.valueOf(params.get("orderBy")))){
            params.put("orderBy","id");
        }
        PageUtils page = zuozheService.queryPage(params);

        //字典表数据转换
        List<ZuozheView> list =(List<ZuozheView>)page.getList();
        for(ZuozheView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段
        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ZuozheEntity zuozhe = zuozheService.selectById(id);
            if(zuozhe !=null){


                //entity转view
                ZuozheView view = new ZuozheView();
                BeanUtils.copyProperties( zuozhe , view );//把实体数据重构到view中

                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody ZuozheEntity zuozhe, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,zuozhe:{}",this.getClass().getName(),zuozhe.toString());
        Wrapper<ZuozheEntity> queryWrapper = new EntityWrapper<ZuozheEntity>()
            .eq("username", zuozhe.getUsername())
            .or()
            .eq("zuozhe_phone", zuozhe.getZuozhePhone())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ZuozheEntity zuozheEntity = zuozheService.selectOne(queryWrapper);
        if(zuozheEntity==null){
            zuozhe.setCreateTime(new Date());
        zuozhe.setPassword("123456");
        zuozheService.insert(zuozhe);
            return R.ok();
        }else {
            return R.error(511,"账户或者作者手机号已经被使用");
        }
    }


}
