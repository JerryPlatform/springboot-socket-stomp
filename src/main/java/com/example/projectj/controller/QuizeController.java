package com.example.projectj.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@Log
@RestController
@RequiredArgsConstructor
@SessionAttributes("id")
public class QuizeController {

//import java.util.*;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
    @GetMapping("/quize")
    public float ace() {
        int x = 7, y = 2, z = 0;
        float result = 0.0F;

        log.info("☆ 1번 커밋");


        log.info("☆" + (x - y));
        log.info("☆" + (x - y) / x);

        result = (x - y) / x * 100;

        log.info("☆" + result);
        return result;
    }
}
