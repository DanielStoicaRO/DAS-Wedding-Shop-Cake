package com.sda.project.repositories;

import com.sda.project.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUsername(String username);
    //    4cart count
    UserEntity findUserEntityByUsername(String username);

}
