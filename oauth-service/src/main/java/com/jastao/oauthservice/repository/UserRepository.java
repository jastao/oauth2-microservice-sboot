package com.jastao.oauthservice.repository;

import com.jastao.oauthservice.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by JT on 4/21/2019
 */
@Repository
public interface UserRepository extends MongoRepository<User, Long> {
	
	Optional<User> findByUsername(String username);
}
