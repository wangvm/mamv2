package edu.cuz.mamv2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuz.mamv2.entity.User;
import edu.cuz.mamv2.mapper.UserMapper;
import edu.cuz.mamv2.service.UserService;
import edu.cuz.mamv2.utils.BackEnum;
import edu.cuz.mamv2.utils.CustomException;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    SqlSessionFactory sqlSessionFactory;


    @Override
    public void saveIgnoreBatch(List<User> users) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        for (User user: users) {
            mapper.saveIgnoreUser(user);
        }
        sqlSession.commit();
    }

    @Override
    public Page<User> queryUserList(Integer status, String order, Integer isAsc, Integer current, Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("account", "createTime", "password", "role", "username")
                .eq("deleted", status)
                .orderBy(true, isAsc > 0 ? true : false, order);
        Page<User> page = userMapper.selectPage(new Page<User>(current, pageSize, true),
                queryWrapper);
        return page;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询到指定用户名的用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new CustomException(BackEnum.LOGIN_FAILED);
        }

        // 获取用户权限，并将其添加到GrantedAuthority中
        ArrayList<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole());
        grantedAuthorities.add(grantedAuthority);
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthorities);
    }
}
