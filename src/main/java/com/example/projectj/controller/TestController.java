package com.example.projectj.controller;


import com.example.projectj.domain.Room;
import com.example.projectj.service.RoomService;
import com.example.projectj.vo.Response;
import com.example.projectj.vo.Result;
import com.example.projectj.vo.RoomVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log
@RestController
@RequiredArgsConstructor
public class TestController {
    private final RoomService roomService;

    private Function<Room, RoomVo> mapRoom = room -> RoomVo.builder()
            .id(room.getId())
            .roomName(room.getRoomNm())
            .build();

    @GetMapping("/tt")
    public ResponseEntity<Response> getRoomList() {
        List<Room> roomList = roomService.getRoomList();

        List<RoomVo> result = roomList.stream().map(mapRoom).collect(Collectors.toList());

        return new ResponseEntity<>(Response.builder().response(Result.builder().build()).contents(result).build(), HttpStatus.OK);
    }

    @GetMapping("/t")
    public List<RoomVo> getRoomList1() {
        List<Room> roomList = roomService.getRoomList();

        List<RoomVo> result = roomList.stream().map(mapRoom).collect(Collectors.toList());

        return result;
    }
}
