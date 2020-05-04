package com.example.demo.controllers.thymeleaf;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorHandlerController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest httpServletRequest, Model model) {

        String errorMessage = "";
        int httpErrorCode = getErrorCode(httpServletRequest);

        switch(httpErrorCode) {
            case 400: {
                errorMessage = "Bad Request";
                break;
            }
            case 401: {
                errorMessage = "Unauthorized request";
                break;
            }
            case 404: {
                errorMessage = "Resource not found";
                break;
            }
            case 500: {
                errorMessage = "Initial server error";
                break;
            }
        }
        model.addAttribute("errorCode",httpErrorCode);
        model.addAttribute("errorMessage",errorMessage);
        return "error";
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
    private int getErrorCode(HttpServletRequest httpServletRequest) {
        return (Integer) httpServletRequest.getAttribute("javax.servlet.error.status_code");
    }
}
