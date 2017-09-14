package com.didi.sre.monitor.mapper.user;

import com.didi.sre.monitor.annotation.Mapper;
import com.didi.sre.monitor.model.user.SysUserEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by soarpenguin on 17-9-9.
 */
@Mapper
public interface SysUserDao {

    SysUserEntity findByUserName(String username);

    //@Insert()
    void insertSysUserEntity(SysUserEntity sysUserEntity);

    @Update("UPDATE SysUser SET email=#{email}, password=#{password} WHERE username=#{username}")
    void updateSysUserEntity( @Param("username") String username,
            @Param("email") String email, @Param("password") String password);

    @Select("select * from SysUser;")
    List<SysUserEntity> getSysUserList();
}

