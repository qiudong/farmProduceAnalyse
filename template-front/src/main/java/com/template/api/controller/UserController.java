package com.template.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.template.api.common.ResponseMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.ArrayList;

/***
 * 用户类
 */
@Controller
@RequestMapping("/")
class UserController {

    @GetMapping
    public String getView() {
        return "login";
    }

    @PostMapping("login")
    public String login(String username, String password) {

        String jsonStr = getFileContent();

        JSONArray jsonArray = (JSONArray)JSONArray.parse(jsonStr);
        long count = new ArrayList(jsonArray).stream().filter(json -> {
            JSONObject jsonObject = (JSONObject) json;

            if (jsonObject.get("username").equals(username) && jsonObject.get("password").equals(password)) {
                return true;
            } else {
                return false;
            }
        }).count();
        if (count > 0) {
            // 登陆成功
            return "index";
        } else {
            return "login";
        }

    }


    /***
     * 读取文件
     * @param
     * @return
     */
    private String getFileContent() {
        ClassPathResource resource = new ClassPathResource("login.json");
        File file = null;
        BufferedReader read = null;
        try {
            file = resource.getFile();
            read = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String s;
            StringBuffer sb = new StringBuffer();
            while ((s = read.readLine()) != null) {
                sb.append(s + "\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (null != read)
                    read.close();
            } catch (IOException e) {

            }
        }
        return "";
    }


}
