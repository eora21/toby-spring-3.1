package com.example.spring_mvc.controller;

import com.example.spring_mvc.level.Level;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConvertController {
    @GetMapping("/search")
    public String search(@RequestParam("level") Level level) {
        return level.name();
    }
}
