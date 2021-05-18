package com.example.server.controller;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.Contact;
import com.example.server.entity.ContactMsg;
import com.example.server.service.UserService;
import com.example.server.service.impl.UserServiceImpl;
import com.example.server.util.annotation.IsLogin;
import com.example.server.util.http.CookieUtils;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 全鸿润
 */
@Api
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService = SpringContextConfig.getBean(UserServiceImpl.class);

    @GetMapping("/get-contact")
    @IsLogin
    public JsonResult handleGetContact(HttpServletRequest request) {

        String username = CookieUtils.findCookie(request.getCookies(), "username").getValue();
        List<ContactMsg> contactList = userService.getContactList(username);
        if (contactList != null) {
            if (contactList.size() > 0) {
                return JsonResultFactory.buildJsonResult(
                        JsonResultStateCode.SUCCESS,
                        JsonResultStateCode.SUCCESS_DESC,
                        contactList
                );
            } else {
                return JsonResultFactory.buildJsonResult(
                        JsonResultStateCode.NOT_FOUND,
                        JsonResultStateCode.NOT_FOUND_DESC,
                        null
                );
            }
        }
        return JsonResultFactory.buildFailureResult();
    }

    @PostMapping("/add-contact")
    public JsonResult handleAddContact(@RequestBody Contact contact) {

        Integer row = userService.addContact(contact);
        if (row != null && row == 1) {
            return JsonResultFactory.buildSuccessResult();
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("/del-contact")
    public JsonResult handleDelContact(@RequestBody Contact contact) {
        Integer row = userService.deleteContact(contact);
        if (row != null && row == 1) {
            return JsonResultFactory.buildSuccessResult();
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }
}
