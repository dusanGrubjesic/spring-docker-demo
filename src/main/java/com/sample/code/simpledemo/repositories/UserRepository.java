package com.sample.code.simpledemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dusan.grubjesic
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity getByUser(String userName);
}
