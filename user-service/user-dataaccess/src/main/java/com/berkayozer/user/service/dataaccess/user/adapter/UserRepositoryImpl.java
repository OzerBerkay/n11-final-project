package com.berkayozer.user.service.dataaccess.user.adapter;

import com.berkayozer.user.service.dataaccess.user.mapper.UserDataAccessMapper;
import com.berkayozer.user.service.dataaccess.user.repository.UserJpaRepository;
import com.berkayozer.user.service.domain.entity.User;
import com.berkayozer.user.service.ports.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserDataAccessMapper userDataAccessMapper;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository, UserDataAccessMapper userDataAccessMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userDataAccessMapper = userDataAccessMapper;
    }

    @Override
    public User save(User user) {
        return userDataAccessMapper.userEntityToUser(
                userJpaRepository.save(userDataAccessMapper.userToUserEntity(user))
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userDataAccessMapper::userEntityToUser);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(userDataAccessMapper::userEntityToUser);
    }
}