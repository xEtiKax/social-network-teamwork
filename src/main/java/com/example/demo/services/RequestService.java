package com.example.demo.services;

import com.example.demo.models.Request;

public interface RequestService {
    Request getRequestById(int id);

    Request createRequest(Request request);

    void deleteRequest(int id);
}
