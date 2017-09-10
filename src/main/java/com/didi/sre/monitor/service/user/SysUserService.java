package com.didi.sre.monitor.service.user;

import com.didi.sre.monitor.mapper.user.SysUserDao;
import com.didi.sre.monitor.model.user.SysRoleEntity;
import com.didi.sre.monitor.model.user.SysUserEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soarpenguin on 17-9-9.
 */
@Service
public class SysUserService implements UserDetailsService {
    private static Logger logger = Logger.getLogger(SysUserService.class);

    @Autowired
    SysUserDao sysUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info("User: " + username + "is checking auth.");
        SysUserEntity user = sysUserDao.findByUserName(username);
        if(user == null){
            logger.warn("Username: " + username + " is not existed;");
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 用于添加用户的权限。只要把用户权限添加到authorities就万事大吉。
        for(SysRoleEntity role:user.getRoles())
        {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            System.out.println(role.getName());
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
