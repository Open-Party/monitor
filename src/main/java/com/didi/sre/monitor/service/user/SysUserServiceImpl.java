package com.didi.sre.monitor.service.user;

import com.didi.sre.monitor.mapper.user.SysUserDao;
import com.didi.sre.monitor.model.common.JsonResult;
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
 * @author soarpenguin on 17-9-9.
 */
@Service
public class SysUserServiceImpl implements UserDetailsService {
    private static Logger logger = Logger.getLogger(SysUserServiceImpl.class);

    @Autowired
    SysUserDao sysUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("User: " + username + " is checking auth now.");

        SysUserEntity user = sysUserDao.findByUserName(username);
        if(user == null) {
            logger.warn("Username: " + username + " is not existed;");
            throw new UsernameNotFoundException("UserName " + username + " not found.");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(SysRoleEntity role:user.getRoles()) {
            logger.debug("User role is: " + role.getName());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public JsonResult insertSysUserEntity(SysUserEntity sysUserEntity) {
        JsonResult jsonResult = new JsonResult(false);

        if(sysUserEntity != null) {
            SysUserEntity user = sysUserDao.findByUserName(sysUserEntity.getUsername());
            if (user != null) {
                jsonResult.setMessage("User " + sysUserEntity.getUsername() + " is existed.");
                jsonResult.setSuccess(false);
            } else {
                sysUserDao.insertSysUserEntity(sysUserEntity);
                jsonResult.setSuccess(true);
            }
        } else {
            jsonResult.setMessage("User object is null!");
        }

        return jsonResult;
    }

    public List<SysUserEntity> getSysUserList() {
        return sysUserDao.getSysUserList();
    }

    public void deleteSysUserById(long id) {
        sysUserDao.deleteSysUserById(id);
    }

    public void updateSysUser(SysUserEntity sysUserEntity) {
        sysUserDao.updateSysUserEntity(sysUserEntity.getUsername(),
                sysUserEntity.getEmail(), sysUserEntity.getPassword());
    }
}
