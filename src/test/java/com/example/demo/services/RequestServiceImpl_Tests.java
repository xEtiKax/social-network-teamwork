package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Request;
import com.example.demo.models.User;
import com.example.demo.repositories.RequestRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.demo.Factory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class RequestServiceImpl_Tests {

    @Mock
    RequestRepository mockRequestRepository;

    @Mock
    UserRepository mockUserRepository;

    @InjectMocks
    private RequestServiceImpl mockRequestServiceImpl;

    @Test
    public void getRequestByIdShould_CallRepository() {
        Request request = createRequest();
        Mockito.when(mockRequestRepository.existsById(request.getId())).thenReturn(true);
        Mockito.when(mockRequestRepository.getRequestById(request.getId())).thenReturn(request);
        Assert.assertSame(mockRequestServiceImpl.getRequestById(request.getId()), request);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getRequestByIdShouldThrow_WhenRequestDoesNotExist() {
        mockRequestServiceImpl.getRequestById(anyLong());
    }

    @Test
    public void createRequest_Should_CallRepository() {
        RequestDTO requestDTO = createRequestDTO();
        User receiver = createUser();
        User sender = createUser();
        Request request = new Request();
        request.setReceiver(receiver);
        request.setSender(sender);
        Mockito.when(mockUserRepository.getById(requestDTO.getReceiver())).thenReturn(receiver);
        Mockito.when(mockUserRepository.getById(requestDTO.getSender())).thenReturn(sender);

        mockRequestServiceImpl.createRequest(requestDTO);

        Mockito.verify(mockRequestRepository, times(1)).save(any(Request.class));

    }

    @Test(expected = DuplicateEntityException.class)
    public void createRequest_ShouldThrow_WhenRequestExists() {
        RequestDTO requestDTO = createRequestDTO();
        User receiver = createUser();
        User sender = createUser();
        Request request = new Request();
        request.setReceiver(receiver);
        request.setSender(sender);
        Set<Request> requests = new HashSet<>();
        requests.add(request);
        receiver.setRequests(requests);
        Mockito.when(mockRequestRepository.findRequestByReceiverAndSender(anyLong(),anyLong())).thenReturn(request);

        mockRequestServiceImpl.createRequest(requestDTO);
    }

    @Test
    public void deleteRequestShould_CallRepository() {
        Request request = createRequest();
        Mockito.when(mockRequestRepository.getRequestById(request.getId())).thenReturn(request);
        Mockito.when(mockRequestRepository.findRequestByReceiverAndSender(request.getSender().getId(), request.getReceiver().getId())).thenReturn(request);
        mockRequestServiceImpl.deleteRequest(request.getId());
        Mockito.verify(mockRequestRepository, times(1)).delete(request);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteRequestShouldTrow_WhenRequestExists() {
        Request request = createRequest();
        Mockito.when(mockRequestRepository.getRequestById(request.getId())).thenReturn(request);
        Mockito.when(mockRequestRepository.findRequestByReceiverAndSender(request.getSender().getId(), request.getReceiver().getId())).thenReturn(null);
        mockRequestServiceImpl.deleteRequest(request.getId());
    }

    @Test
    public void checkIfRequestExist_ShouldCallRepository(){
        mockRequestServiceImpl.checkIfRequestExist(anyLong(),anyLong());
        Mockito.verify(mockRequestRepository,times(1)).findRequestByReceiverAndSender(anyLong(),anyLong());
    }

    @Test
    public void getRequestBySenderAndReceiverShould_CallRepository() {
        User sender = createUser();
        User receiver = createUser();

        mockRequestServiceImpl.getRequestBySenderAndReceiver(sender.getId(), receiver.getId());
        Mockito.verify(mockRequestRepository,times(1)).findRequestByReceiverAndSender(anyLong(),anyLong());
    }

    @Test
    public void getUserRequestsShould_CallRepository() {
        List<Request> requests = new ArrayList<>();
        User user = createUser();
        requests.add(createRequest());
        Mockito.when(mockUserRepository.getById(user.getId())).thenReturn(user);
        Mockito.when(mockRequestRepository.findAllByReceiver(user)).thenReturn(requests);
        mockRequestServiceImpl.getUserRequests(user.getId());
        Mockito.verify(mockRequestRepository, times(1)).findAllByReceiver(user);

    }


}

