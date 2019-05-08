package com.jastao.oauthservice.repository;

import com.jastao.oauthservice.domain.AuthClientDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by JT on 4/21/2019
 */
@Repository
public interface AuthClientRepository extends MongoRepository<AuthClientDetails, Long>{
	
	Optional<AuthClientDetails> findByClientId(String clientId);
}
