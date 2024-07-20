package com.sky.yb.user.service.repository;

import com.sky.yb.user.service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query(value = "{'_id' : ?0}", fields = "{ 'userExternalProjects' : 1 }")
    User findByIdWithExternalProjects(String userId);

}
