package com.example.demo.repositories;

import com.example.demo.models.Request;
import com.example.demo.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;


public interface RequestRepository extends CrudRepository<Request, Serializable> {
    @Query(value = "SELECT * FROM social_network.requests WHERE id = ?1", nativeQuery = true)
    Request getRequestById(long id);

    @Query(value = "SELECT * FROM social_network.requests WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
    Request existsByUniquePair(long sender, long receiver);


//    @Query(value = "SELECT id, sender_id, receiver_id from social_network.requests where receiver_id = ?1", nativeQuery = true)

    List<Request> findAllByReceiver(User userId);

    @Query(value = "SELECT * FROM social_network.requests WHERE sender_id = ?1 AND receiver_id = ?2", nativeQuery = true)
    Request findAllByReceiverAndSender(long sender, long receiver);
}
