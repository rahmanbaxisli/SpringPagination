package com.example.spring.pagination.service.concrete;

import com.example.spring.pagination.dao.entity.UserEntity;
import com.example.spring.pagination.dao.repository.UserRepository;
import com.example.spring.pagination.exception.NotFoundException;
import com.example.spring.pagination.model.criteria.PageCriteria;
import com.example.spring.pagination.model.criteria.UserCriteria;
import com.example.spring.pagination.model.request.CreateUserRequest;
import com.example.spring.pagination.model.response.PageableUserResponse;
import com.example.spring.pagination.model.response.UserResponse;
import com.example.spring.pagination.service.abstraction.UserService;
import com.example.spring.pagination.service.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static com.example.spring.pagination.mapper.UserMapper.USER_MAPPER;
import static com.example.spring.pagination.model.enums.ExceptionConstant.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceHandler implements UserService {
    private final UserRepository userRepository;

    @Override
    public void addUser(CreateUserRequest request) {
        log.info("ActionLog.addUser.start");
        userRepository.save(USER_MAPPER.buildUserEntity(request));
        log.info("ActionLog.addUser.success");
    }

    @Override
    public UserResponse getUser(Long id) {
        log.info("ActionLog.getUser.start with id: {}", id);
        var user = fetchUserIfExist(id);
        log.info("ActionLog.getUser.success with id: {}", id);
        return USER_MAPPER.buildUserResponse(user);
    }

    @Override
    public void setBirthPlace(Long id, String birthPlace) {
        log.info("ActionLog.setBirthPlace.start with id: {}", id);
        var user = fetchUserIfExist(id);
        USER_MAPPER.updateBirthPlace(user, birthPlace);
        userRepository.save(user);
        log.info("ActionLog.setBirthPlace.success with id: {}", id);
    }

    @Override
    public PageableUserResponse getUsers(PageCriteria pageCriteria,
                                         UserCriteria userCriteria) {
        log.info("ActionLog.getUsers.start");
        var users = userRepository.findAll(
                new UserSpecification(userCriteria),
                PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount(), Sort.by("id").descending()));
        log.info("ActionLog.getUsers.success");

        return USER_MAPPER.mapPageableUserResponse(users);
    }

    private UserEntity fetchUserIfExist(Long id) {
        return userRepository.findById(id).orElseThrow(()->
                new NotFoundException(USER_NOT_FOUND.getCode(), USER_NOT_FOUND.getMessage()));
    }

}
