package cn.itzhouq.service.acl.service.impl;

import cn.itzhouq.security1.entity.SecurityUser;
import cn.itzhouq.service.acl.entity.User;
import cn.itzhouq.service.acl.service.PermissionService;
import cn.itzhouq.service.acl.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhouquan
 * @date 2021/2/21 09:12
 */
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据库
        User user = userService.selectByUsername(username);
        // 判断
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        cn.itzhouq.security1.entity.User curUser = new cn.itzhouq.security1.entity.User();
        BeanUtils.copyProperties(user, curUser);

        // 根据用户查询用户权限列表
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser = new SecurityUser();
        securityUser.setPermissionvalueList(permissionValueList);
        return securityUser;
    }
}
