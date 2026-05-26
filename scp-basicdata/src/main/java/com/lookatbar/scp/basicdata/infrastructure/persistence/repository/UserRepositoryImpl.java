package com.lookatbar.scp.basicdata.infrastructure.persistence.repository;

import com.lookatbar.scp.basicdata.domain.model.user.User;
import com.lookatbar.scp.basicdata.domain.model.user.UserStatus;
import com.lookatbar.scp.basicdata.domain.repository.UserRepository;
import com.lookatbar.scp.basicdata.infrastructure.persistence.mapper.UserMapper;
import com.lookatbar.scp.basicdata.infrastructure.persistence.po.UserPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

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

    @Override
    public Optional<User> findById(Long id) {
        return userMapper.selectById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userMapper.findByUsername(username).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userMapper.findByPhone(phone).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByStatus(Integer status) {
        return userMapper.findByStatus(status).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMapper.countByUsername(username) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userMapper.countByPhone(phone) > 0;
    }

    private UserPO toPO(User user) {
        return UserPO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .phone(user.getPhone())
                .realName(user.getRealName())
                .status(user.getStatus() != null ? user.getStatus().getCode() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private User toDomain(UserPO po) {
        return User.builder()
                .id(po.getId())
                .username(po.getUsername())
                .password(po.getPassword())
                .email(po.getEmail())
                .phone(po.getPhone())
                .realName(po.getRealName())
                .status(po.getStatus() != null ? UserStatus.fromCode(po.getStatus()) : null)
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }
}