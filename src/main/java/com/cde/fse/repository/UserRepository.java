package com.cde.fse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cde.fse.model.User;
import com.cde.fse.model.Users;
//
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByUsername(String username);

	Boolean existsByUsername(String username);
	
	@Query("select r from Users r where r.username like %?1")
    public Users getRoleOfUser(String username);
}
