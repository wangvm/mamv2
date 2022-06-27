package edu.cuz.mamv2.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.cuz.mamv2.entity.MamUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * <p>
 * 媒资用户表 服务类
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
public interface UserService extends IService<MamUser>, UserDetailsService {

    /**
     * 批量添加用户
     * @param mamUsers 待添加的用户列表，已验证
     */
    void saveIgnoreBatch(List<MamUser> mamUsers);

    /**
     * 根据指定条件查询用户列表
     * @param status 用户状态，null：可用账户，1：全部账户，0：禁用账户
     * @param order 查询结果排序，默认为根据account排序
     * @param isAsc 排序方式，默认为desc降序，（aes升序）
     * @param current 当前页
     * @param pageSize 分页大小
     * @return 分页后的查询结果
     */
    Page<MamUser> queryUserList(Integer status, String order, Integer isAsc, Integer current, Integer pageSize);
}
