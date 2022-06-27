package edu.cuz.mamv2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.cuz.mamv2.entity.MamUser;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, MamUser> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    SqlSessionFactory sqlSessionFactory;


    @Override
    public void saveIgnoreBatch(List<MamUser> mamUsers) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        for (MamUser mamUser: mamUsers) {
            mapper.saveIgnoreUser(mamUser);
        }
        sqlSession.commit();
    }

    @Override
    public Page<MamUser> queryUserList(Integer status, String order, Integer isAsc, Integer current, Integer pageSize) {
        QueryWrapper<MamUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("account", "createTime", "password", "role", "username")
                .eq("deleted", status)
                .orderBy(true, isAsc > 0 ? true : false, order);
        Page<MamUser> page = userMapper.selectPage(new Page<MamUser>(current, pageSize, true),
                queryWrapper);
        return page;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询到指定用户名的用户信息
        LambdaQueryWrapper<MamUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MamUser::getAccount, username);
        MamUser mamUser = userMapper.selectOne(queryWrapper);
        if (mamUser == null) {
            throw new CustomException(BackEnum.LOGIN_FAILED);
        }
        // 获取用户权限，并将其添加到GrantedAuthority中
        ArrayList<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(mamUser.getRole());
        grantedAuthorities.add(grantedAuthority);
        return new org.springframework.security.core.userdetails.User(username, mamUser.getPassword(), grantedAuthorities);
    }
}
