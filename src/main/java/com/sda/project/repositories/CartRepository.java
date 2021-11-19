package com.sda.project.repositories;

import com.sda.project.entities.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    List<CartEntity> findAllByUser_Username(String username);

    //    4cart count
    Long countAllByUserId(Integer userId);

    //add quantity
    CartEntity findByProductIdAndUserId(Integer prodId, Integer userId);


}
