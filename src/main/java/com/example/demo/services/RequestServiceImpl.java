package com.example.demo.services;

import com.example.demo.models.Request;
import com.example.demo.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {
    private RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Request getRequestById(int id) {
        return requestRepository.getRequestById(id);
    }

    @Override
    public Request createRequest(Request request) {
        return null;
    }

    @Override
    public void deleteRequest(int id) {

    }
}
