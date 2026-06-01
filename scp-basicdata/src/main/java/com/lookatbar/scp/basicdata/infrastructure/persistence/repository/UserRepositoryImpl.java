package com.lookatbar.scp.basicdata.infrastructure.persistence.repository;

import cn.hutool.core.bean.BeanUtil;
import com.lookatbar.scp.basicdata.domain.model.user.User;
import com.lookatbar.scp.basicdata.domain.model.user.UserStatus;
import com.lookatbar.scp.basicdata.domain.repository.UserRepository;
import com.lookatbar.scp.basicdata.infrastructure.persistence.mapper.UserMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.UserPO;
import com.lookatbar.scp.basicdata.infrastructure.util.BeanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户仓储实现类
 * 基于MyBatis Plus实现用户数据访问，负责领域模型与持久化对象的转换
 * 使用 BeanConverter 简化对象转换逻辑
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    /**
     * 用户数据访问Mapper
     */
    private final UserMapper userMapper;

    /**
     * 保存用户实体
     *
     * @param user 用户实体
     * @return 保存后的用户实体
     */
    @Override
    public User save(User user) {
        UserPO po = toPO(user);
        if (user.getId() == null) {
            userMapper.insert(po);
        } else {
            userMapper.updateById(po);
        }
        return toDomain(po);
    }

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户实体（可选）
     */
    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(this.toDomain(userMapper.selectById(id)));
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体（可选）
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userMapper.findByUsername(username).map(this::toDomain);
    }

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱地址
     * @return 用户实体（可选）
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email).map(this::toDomain);
    }

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号码
     * @return 用户实体（可选）
     */
    @Override
    public Optional<User> findByPhone(String phone) {
        return userMapper.findByPhone(phone).map(this::toDomain);
    }

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    @Override
    public List<User> findAll() {
        return userMapper.selectList(null).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 根据状态查询用户
     *
     * @param status 用户状态
     * @return 用户列表
     */
    @Override
    public List<User> findByStatus(Integer status) {
        return userMapper.findByStatus(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     */
    @Override
    public void deleteById(String id) {
        userMapper.deleteById(id);
    }

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    @Override
    public boolean existsByUsername(String username) {
        return userMapper.countByUsername(username) > 0;
    }

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱地址
     * @return 是否存在
     */
    @Override
    public boolean existsByEmail(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    /**
     * 检查手机号是否已存在
     *
     * @param phone 手机号码
     * @return 是否存在
     */
    @Override
    public boolean existsByPhone(String phone) {
        return userMapper.countByPhone(phone) > 0;
    }

    /**
     * 将领域模型转换为持久化对象
     * 使用 BeanConverter 进行属性复制，简化代码
     *
     * @param user 用户领域模型
     * @return 用户持久化对象
     */
    private UserPO toPO(User user) {
        UserPO po = BeanConverter.convert(user, UserPO.class);
        // 处理枚举类型转换
        if (user.getStatus() != null) {
            po.setStatus(user.getStatus().getCode());
        }
        return po;
    }

    /**
     * 将持久化对象转换为领域模型
     * 使用 BeanConverter 进行属性复制，简化代码
     *
     * @param po 用户持久化对象
     * @return 用户领域模型
     */
    private User toDomain(UserPO po) {
        if (po == null) {
            return null;
        }
        User user = BeanConverter.convert(po, User.class);
        // 处理枚举类型转换
        if (po.getStatus() != null) {
            user.setStatus(UserStatus.fromCode(po.getStatus()));
        }
        return user;
    }
}
