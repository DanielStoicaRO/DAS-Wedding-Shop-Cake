package com.sda.project.repositories;

import com.sda.project.entities.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Integer> {

}
