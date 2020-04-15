package com.example.demo.services.interfaces;

import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Request;
import com.example.demo.models.User;

import java.util.List;

public interface RequestService {

    Request getRequestById(long id);

    Request createRequest(RequestDTO requestDTO);

    void deleteRequest(long id);

    boolean checkIfRequestExist(long sender, long receiver);

    List<Request> getUserRequests(User userId);
}
