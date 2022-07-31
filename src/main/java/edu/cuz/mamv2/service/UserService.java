package edu.cuz.mamv2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuz.mamv2.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * <p>
 * 媒资用户表 服务类
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
public interface UserService extends IService<SysUser>, UserDetailsService {

    /**
     * 批量添加用户
     * @param sysUsers 待添加的用户列表，已验证
     */
    void saveIgnoreBatch(List<SysUser> sysUsers);

    /**
     * 根据指定条件查询用户列表
     * @param status 用户状态，null：可用账户，1：全部账户，0：禁用账户
     * @param order 查询结果排序，默认为根据account排序
     * @param isAsc 排序方式，默认为desc降序，（aes升序）
     * @param current 当前页
     * @param pageSize 分页大小
     * @return 分页后的查询结果
     */
    Page<SysUser> queryUserList(Integer status, String order, Integer isAsc, Integer current, Integer pageSize);
}
