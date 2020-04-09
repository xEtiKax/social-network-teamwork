package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Request;
import com.example.demo.models.User;
import com.example.demo.repositories.RequestRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestServiceImpl implements RequestService {
    public static final String REQUEST_DOES_NOT_EXISTS = "Request does not exists.";
    public static final String REQUEST_ALREADY_WAS_SEND = "Request already was send";
    private RequestRepository requestRepository;
    private UserRepository userRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Request getRequestById(int id) {
        return requestRepository.getRequestById(id);
    }

    @Override
    public Request createRequest(RequestDTO requestDTO) {
        throwIfRequestExists(requestDTO.getSender(),requestDTO.getReceiver());
        Request request = new Request();
        requestMerge(request, requestDTO);
        return requestRepository.save(request);
    }

    @Override
    public void deleteRequest(int id) {
        Request requestToDelete = requestRepository.getRequestById(id);
        throwIfRequestDoesNotExists(requestToDelete.getSender().getId(), requestToDelete.getReceiver().getId());
        requestRepository.delete(requestToDelete);
    }

    @Override
    public boolean checkIfRequestExist(int sender, int receiver) {
        return requestRepository.existsByUniquePair(sender, receiver) != null;
    }

    private Request requestMerge(Request request, RequestDTO requestDTO) {
        request.setAccepted(false);
        User receiver = userRepository.getById(requestDTO.getReceiver());
        request.setReceiver(receiver);
        User sender = userRepository.getById(requestDTO.getSender());
        request.setSender(sender);
        return request;
//        TODO set principal
    }

    private void throwIfRequestDoesNotExists(int sender, int receiver) {
        if (!checkIfRequestExist(sender, receiver)) {
            throw new EntityNotFoundException(REQUEST_DOES_NOT_EXISTS);
        }
    }

    private void throwIfRequestExists(int sender, int receiver) {
        if (checkIfRequestExist(sender, receiver)) {
            throw new DuplicateEntityException(REQUEST_ALREADY_WAS_SEND);
        }
    }
}
