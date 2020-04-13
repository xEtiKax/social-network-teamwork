package com.example.demo.services;

import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Request;

import java.util.List;

public interface RequestService {

    Request getRequestById(long id);

    Request createRequest(RequestDTO requestDTO);

    void deleteRequest(long id);

    boolean checkIfRequestExist(long sender, long receiver);

    List<Request> getUserRequests(long userId);
}
