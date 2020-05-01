package com.example.demo.services;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Post;
import com.example.demo.models.Request;
import com.example.demo.models.User;
import com.example.demo.repositories.RequestRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

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

//    @Test(expected = EntityNotFoundException.class)
//    public void getRequestByIdShouldThrow_WhenRequestDoesNotExist() {
//        mockRequestServiceImpl.getRequestById(anyLong());
//    }

//    @Test
//    public void createRequest_Should_CallRepository() {
//        RequestDTO requestDTO = createRequestDTO();
//
//        mockRequestServiceImpl.createRequest(requestDTO);
//
//        Mockito.verify(mockRequestRepository, times(1)).save(any(Request.class));
//
//    }

//    @Test
//    public void deletePostShould_CallRepository() {
//        Request request = createRequest();
//        User receiver = createUser();
//        Mockito.when(mockRequestRepository.getRequestById(request.getId())).thenReturn(request);
//        Mockito.when(mockRequestRepository.existsById(anyLong())).thenReturn(true);
//        Mockito.when(mockRequestRepository.save(any(Request.class))).thenReturn(request);
//        request.setReceiver(receiver);
//
//        mockRequestServiceImpl.deleteRequest(request.getId());
//
//        Mockito.verify(mockRequestRepository, times(1)).save(any(Request.class));
//    }



    @Test
    public void getRequestBySenderAndReceiverShould_CallRepository() {
        User sender = createUser();
        User receiver = createUser();

        mockRequestServiceImpl.getRequestBySenderAndReceiver(sender.getId(), receiver.getId());
    }

    @Test
    public void getUserRequestsShould_CallRepository() {
        List<Request> requests = new ArrayList<>();
        User user = createUser();
        requests.add(createRequest());

        mockRequestServiceImpl.getUserRequests(user.getId());
    }
}
