package edu.cuz.mamv2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuz.mamv2.entity.SysUser;
import edu.cuz.mamv2.mapper.UserMapper;
import edu.cuz.mamv2.service.UserService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 媒资用户表 服务实现类
 * </p>
 * @author VM
 * @since 2022/01/17 10:56
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    SqlSessionFactory sqlSessionFactory;


    @Override
    public void saveIgnoreBatch(List<SysUser> sysUsers) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        for (SysUser sysUser: sysUsers) {
            mapper.saveIgnoreUser(sysUser);
        }
        sqlSession.commit();
    }

    @Override
    public Page<SysUser> queryUserList(Integer status, String order, Integer isAsc, Integer current, Integer pageSize) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("account", "createTime", "password", "role", "username")
                .eq("deleted", status)
                .orderBy(true, isAsc > 0 ? true : false, order);
        Page<SysUser> page = userMapper.selectPage(new Page<SysUser>(current, pageSize, true),
                queryWrapper);
        return page;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询到指定用户名的用户信息
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, username);
        SysUser sysUser = userMapper.selectOne(queryWrapper);
        if (sysUser == null) {
            throw new BadCredentialsException("账户或密码错误");
        }
        // 获取用户权限，并将其添加到GrantedAuthority中
        ArrayList<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysUser.getRole());
        grantedAuthorities.add(grantedAuthority);
        return new org.springframework.security.core.userdetails.User(username, sysUser.getPassword(), grantedAuthorities);
    }
}
