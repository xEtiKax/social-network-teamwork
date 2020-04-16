package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Request;
import com.example.demo.models.User;
import com.example.demo.repositories.RequestRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Request getRequestById(long id) {
        return requestRepository.getRequestById(id);
    }

    @Override
    public Request createRequest(RequestDTO requestDTO) {
        throwIfRequestExists(requestDTO.getSender(),requestDTO.getReceiver());
        Request request = new Request();
        requestMerge(request, requestDTO);
        User receiver = request.getReceiver();
        if (receiver.getRequests().contains(request)){
            throw new DuplicateEntityException("You are already sent a request");
        }
        receiver.addRequest(request);
        return requestRepository.save(request);
    }

    @Override
    public void deleteRequest(long id) {
        Request requestToDelete = requestRepository.getRequestById(id);
        throwIfRequestDoesNotExists(requestToDelete.getSender().getId(), requestToDelete.getReceiver().getId());
        User receiver = requestToDelete.getReceiver();
        receiver.deleteRequest(requestToDelete);
        requestRepository.delete(requestToDelete);
    }

    @Override
    public boolean checkIfRequestExist(long sender, long receiver) {
        return requestRepository.existsByUniquePair(sender, receiver) != null;
    }

    @Override
    public Request getRequestBySenderAndReciever(long sender, long receiver) {
        return requestRepository.findAllByReceiverAndSender(sender, receiver);
    }

    @Override
    public List<Request> getUserRequests(User userId) {
        return requestRepository.findAllByReceiver(userId);
    }

    private Request requestMerge(Request request, RequestDTO requestDTO) {
        request.setAccepted(0);
        User receiver = userRepository.getById(requestDTO.getReceiver());
        request.setReceiver(receiver);
        User sender = userRepository.getById(requestDTO.getSender());
        request.setSender(sender);
        return request;
//        TODO set principal
    }

    private void throwIfRequestDoesNotExists(long sender, long receiver) {
        if (!checkIfRequestExist(sender, receiver)) {
            throw new EntityNotFoundException(REQUEST_DOES_NOT_EXISTS);
        }
    }

    private void throwIfRequestExists(long sender, long receiver) {
        if (checkIfRequestExist(sender, receiver)) {
            throw new DuplicateEntityException(REQUEST_ALREADY_WAS_SEND);
        }
    }
}
