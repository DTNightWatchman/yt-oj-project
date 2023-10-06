package com.yt.ytojbackenduserservice.controller.inner;

import com.yt.ytojbackendmodel.model.entity.User;
import com.yt.ytojbackendserviceclient.service.UserFeignClient;
import com.yt.ytojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * @Author: YT
 * @Description: 用户服务内部调用
 * @DateTime: 2023/9/26$ - 10:45
 */

@RestController
@RequestMapping(("/inner"))
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    /**
     * 根据id获取用户列表
     * @param idList
     * @return
     */
    @PostMapping("/get/ids")
    @Override
    public List<User> listByIds(@RequestBody Collection<Long> idList) {
        return userService.listByIds(idList);
    }

    /**
     * 根据id获取用户
     * @param userId
     * @return
     */
    @GetMapping("/get")
    @Override
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

}
