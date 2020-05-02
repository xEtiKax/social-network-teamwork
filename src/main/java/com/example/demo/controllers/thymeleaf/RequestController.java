package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Request;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.RequestService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

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

    @RequestMapping("/send")
    public String sendRequest(@RequestParam(name = "userId") long userId, Principal principal) {
        User sender = userService.getByUsername(principal.getName());
        try {
            RequestDTO requestDTO = new RequestDTO();
            requestDTO.setSender(sender.getId());
            requestDTO.setReceiver(userId);
            requestService.createRequest(requestDTO);

        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
        return "redirect:/user/showUserProfile/" + userId;
    }

    @PostMapping("accept/{requestId}")
    public String acceptRequest(@PathVariable long requestId, Principal principal) {
        Request request = requestService.getRequestById(requestId);
        User sender = request.getSender();
        User receiver = userService.getByUsername(principal.getName());
        sender.addFriend(receiver);
        receiver.addFriend(sender);
        requestService.deleteRequest(requestId);

        return "redirect:/request/showRequests";
    }

    @GetMapping("/remove/{friendId}")
    public String removeFriend(@PathVariable long friendId, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        User friend = userService.getById(friendId);
        userService.removeFriend(user, friend);
        userService.removeFriend(friend, user);
        return "redirect:/user/showUserProfile/" + friend.getId();
    }

    @PostMapping("reject/{requestId}")
    public String rejectRequest(@PathVariable long requestId) {
        requestService.deleteRequest(requestId);
        return "redirect:/request/showRequests";
    }

    @RequestMapping("/cancel/{userId}")
    public String cancelRequest(@PathVariable long userId, Principal principal) {
        User sender = userService.getByUsername(principal.getName());
        Request request = requestService.getRequestBySenderAndReceiver(sender.getId(), userId);
        User receiver = userService.getById(userId);
        requestService.deleteRequest(request.getId());
        return "redirect:/user/showUserProfile/" + receiver.getId();
    }

    @GetMapping("/showRequests")
    public String getFriendRequests(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        List<Request> friendRequests = requestService.getUserRequests(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("requests", friendRequests);
        return "requests";
    }
}
