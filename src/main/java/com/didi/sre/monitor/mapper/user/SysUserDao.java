package com.didi.sre.monitor.mapper.user;

import com.didi.sre.monitor.annotation.Mapper;
import com.didi.sre.monitor.model.user.SysUserEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author  soarpenguin on 17-9-9.
 */
@Mapper
public interface SysUserDao {

    /**
     * Find user entity by username..
     *
     * @param username
     * @return SysUserEntity
     */
    SysUserEntity findByUserName(String username);

    /**
     * Insert user entity to table.
     *
     * @param sysUserEntity
     * @return
     */
    void insertSysUserEntity(SysUserEntity sysUserEntity);

    /**
     * Update user info by username..
     *
     * @param username user name.
     * @param email user email.
     * @param password user password.
     * @return
     */
    @Update("UPDATE SysUser SET email=#{email}, password=#{password} WHERE username=#{username}")
    void updateSysUserEntity( @Param("username") String username,
            @Param("email") String email, @Param("password") String password);

    /**
     * Get user list.
     *
     * @return List of SysUserEntity
     */
    @Select("SELECT * FROM SysUser WHERE deleted != 1;")
    List<SysUserEntity> getSysUserList();

    /**
     * Delete user by id.
     *
     * @param id user id.
     * @return
     */
    @Delete("UPDATE SysUser SET deleted = 1 WHERE id=#{id};")
    void deleteSysUserById(@Param("id") long id);
}

