package edu.cuz.mamv2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.cuz.mamv2.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 媒资用户表 Mapper 接口
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
public interface UserMapper extends BaseMapper<SysUser> {

    /**
     * 插入用户，如果存在则忽略
     * @param sysUser 待插入的用户
     * @return 插入结果
     */
    boolean saveIgnoreUser(@Param("user") SysUser sysUser);
}
