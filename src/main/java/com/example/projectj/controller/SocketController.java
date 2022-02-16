package com.example.projectj.controller;

import com.example.projectj.domain.Room;
import com.example.projectj.dto.RoomDto;
import com.example.projectj.service.RoomService;
import com.example.projectj.vo.SocketVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@RestController
@EnableScheduling
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate template;

    private final RoomService roomService;

    @PostMapping("/create/room")
    public void createRoom(@RequestBody RoomDto dto) {
        roomService.createRoom(dto.getRoomName());
    }

    @PostMapping("/chat/room/in")
    public void roomIn(@RequestBody SocketVo vo) throws JsonProcessingException {
        Room room = roomService.findRoom(vo.getRoomId());
        String userName = "[알림]";
        String content = vo.getUserName() + "님이 채팅방에 입장하였습니다.";
        SocketVo result = new SocketVo(userName, content, "notice");

        String resultValue = objectToJsonString(result);

        template.convertAndSend("/sub/chat/room/" + room.getId(), resultValue);
    }

    @MessageMapping("/chat/room")
    public void enter(SocketVo vo) throws JsonProcessingException {
        Room room = roomService.findRoom(vo.getRoomId());
	    String userName = vo.getUserName();
        String content = vo.getContent();

        SocketVo result = new SocketVo(userName, content, "user");

        String resultValue = objectToJsonString(result);

        template.convertAndSend("/sub/chat/room/" + room.getId(), resultValue);
    }

    @Scheduled(fixedDelay = 6000)
    @SendTo("/sub/room/list")
    public void roomList() throws JsonProcessingException {
        List<Room> roomList = roomService.getRoomList();
        List<Map<String, Object>> roomResult = new ArrayList<>();

        for (Room room : roomList) {
            Map<String, Object> result = new HashMap<>();
            result.put("id", room.getId());
            result.put("name", room.getRoomNm());
            roomResult.add(result);
        }

        String resultValue = objectToJsonString(roomResult);

        template.convertAndSend("/sub/room/list", resultValue);
    }

    public String objectToJsonString(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(o);
        return jsonInString;
    }
}
