package com.example.demo.services;

import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Request;

public interface RequestService {

    Request getRequestById(int id);

    Request createRequest(RequestDTO requestDTO);

    void deleteRequest(int id);

    boolean checkIfRequestExist(int sender, int receiver);
}
