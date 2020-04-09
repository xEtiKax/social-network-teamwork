package com.example.demo.controllers.rest;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Request;
import com.example.demo.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("api/requests")
public class RequestRestController {
    RequestService requestService;

    @Autowired
    public RequestRestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/{id}")
    public Request getPostById(@PathVariable int id) {
        return requestService.getRequestById(id);
    }

    @PostMapping("/create")
    public void createRequest(@RequestBody RequestDTO requestDTO, Principal principal) {
        try {
            requestService.createRequest(requestDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable int id) {
        requestService.deleteRequest(id);
    }
}
