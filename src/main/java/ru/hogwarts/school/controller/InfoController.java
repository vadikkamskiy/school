package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/info")
public class InfoController {
    @Value("${server.port}")
    private int port;

    @GetMapping("")
    public int getMethodName() {
        return port;
    }
    
}
