package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Request;
import com.example.demo.models.User;
import com.example.demo.services.RequestService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequestMapping("/request")
public class RequestController {

    private UserService userService;
    private RequestService requestService;

    @Autowired
    public RequestController(UserService userService, RequestService requestService) {
        this.userService = userService;
        this.requestService = requestService;
    }

    @GetMapping("/{id}")
    public Request getRequestById(@PathVariable long id) {
        return requestService.getRequestById(id);
    }

    @PostMapping("/send/{userId}")
    public String sendRequest(@PathVariable long userId, Principal principal) {
        User sender = userService.getByUsername(principal.getName());
        try {
            RequestDTO requestDTO = new RequestDTO();
            requestDTO.setSender(sender.getId());
            requestDTO.setReceiver(userId);
            requestService.createRequest(requestDTO);

        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return "user-profile";
    }

    @PostMapping("accept/{requestId}")
    public String acceptRequest(@PathVariable long requestId, Principal principal) {
        Request request = requestService.getRequestById(requestId);
        User sender = request.getSender();
        User receiver = userService.getByUsername(principal.getName());
        sender.addFriend(receiver);
        receiver.addFriend(sender);
        requestService.deleteRequest(requestId);
        return "requests";
    }

    @DeleteMapping("reject/{requestId}")
    public String rejectRequest(@PathVariable long requestId) {
        requestService.deleteRequest(requestId);
        return "requests";
    }
}
