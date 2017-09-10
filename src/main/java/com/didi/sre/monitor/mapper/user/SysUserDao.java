package com.didi.sre.monitor.mapper.user;

import com.didi.sre.monitor.annotation.Mapper;
import com.didi.sre.monitor.model.user.SysUserEntity;

/**
 * Created by soarpenguin on 17-9-9.
 */
@Mapper
public interface SysUserDao {
    SysUserEntity findByUserName(String username);
}

