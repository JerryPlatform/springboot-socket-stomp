package com.example.projectj.controller;


import com.example.projectj.domain.Room;
import com.example.projectj.service.RoomService;
import com.example.projectj.vo.Response;
import com.example.projectj.vo.Result;
import com.example.projectj.vo.RoomVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
@RestController
@RequiredArgsConstructor
public class TestController {
    private final RoomService roomService;

    private Function<Room, RoomVo> mapRoom = room -> RoomVo.builder()
            .id(room.getId())
            .roomName(room.getRoomNm())
            .build();

    @GetMapping("/test/{text}")
    public void test123(@PathVariable String text) {
    }

    @GetMapping("/tt")
    public ResponseEntity<Response> getRoomList() {
        List<Room> roomList = roomService.getRoomList();

        List<RoomVo> result = roomList.stream().map(mapRoom).collect(Collectors.toList());

        return new ResponseEntity<>(Response.builder().response(Result.builder().build()).contents(result).build(), HttpStatus.OK);
    }

    @GetMapping("/t")
    public Map<String, String> getRoomList1() throws JsonProcessingException {
        List<Room> roomList = roomService.getRoomList();

        List<RoomVo> result = roomList.stream().map(mapRoom).collect(Collectors.toList());

        Map<String, String> results = new HashMap<>();

        results.put("response", objectToJson(Result.builder().build()));
        results.put("contents", objectToJson(result));

        log.info("★" + results);

        return results;
    }

    public String objectToJson(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(o);
        return jsonInString;
    }
}
