package com.yt.ytojbackendserviceclient.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.yt.ytojbackendcommon.common.ErrorCode;
import com.yt.ytojbackendcommon.constant.UserConstant;
import com.yt.ytojbackendcommon.exception.BusinessException;
import com.yt.ytojbackendmodel.model.dto.user.UserQueryRequest;
import com.yt.ytojbackendmodel.model.entity.User;
import com.yt.ytojbackendmodel.model.enums.UserRoleEnum;
import com.yt.ytojbackendmodel.model.vo.LoginUserVO;
import com.yt.ytojbackendmodel.model.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/DTNightWatchman">YTbaiduren</a>
 * @from
 */
@FeignClient(name = "ytoj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 根据id获取用户列表
     * @param idList
     * @return
     */
    @PostMapping("/get/ids")
    List<User> listByIds(@RequestBody Collection<Long> idList);

    /**
     * 根据id获取用户
     * @param userId
     * @return
     */
    @GetMapping("/get")
    User getById(@RequestParam("userId") long userId);


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObject;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }



    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }


    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }



}
