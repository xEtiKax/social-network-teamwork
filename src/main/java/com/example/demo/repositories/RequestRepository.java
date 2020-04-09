package com.example.demo.repositories;

import com.example.demo.models.Request;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;


public interface RequestRepository extends CrudRepository<Request, Serializable> {
    @Query(value = "SELECT * FROM social_network.requests WHERE id = ?1", nativeQuery = true)
    Request getRequestById(int id);

    @Query(value = "SELECT * FROM social_network.requests WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
    Request existsByUniquePair(int sender, int receiver);
}
