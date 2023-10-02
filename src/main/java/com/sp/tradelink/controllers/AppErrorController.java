package com.sp.tradelink.controllers;


import com.sp.tradelink.models.DefaultErrorResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AppErrorController implements ErrorController {
    static final String ERROR_PATH = "/error";

    @RequestMapping({AppErrorController.ERROR_PATH})
    public ResponseEntity<?> handleError(HttpServletRequest request) {
        String errorPage = "error"; // default

        var status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        var message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // handle HTTP 404 Not Found error
                errorPage = "STATUS(404)";

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // handle HTTP 403 Forbidden error
                errorPage = "STATUS(403)";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // handle HTTP 500 Internal Server error
                errorPage = "STATUS(500)";

            }
        }

        DefaultErrorResponse root = new DefaultErrorResponse();
        root.setResultCode("-00001");
        root.setResultMsg(String.valueOf(message));
        root.setResultTxt(errorPage);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(root);
    }
}
