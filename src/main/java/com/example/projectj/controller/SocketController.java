package com.example.projectj.controller;

import com.example.projectj.domain.Room;
import com.example.projectj.dto.RoomDto;
import com.example.projectj.dto.VerificationDto;
import com.example.projectj.service.RoomService;
import com.example.projectj.vo.SocketVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Log
@RestController
@EnableScheduling
@RequiredArgsConstructor
public class SocketController {

    private SimpUserRegistry simpUserRegistry;

    private final SimpMessagingTemplate template;

    private final RoomService roomService;

    List<Map<String, Object>> session = new ArrayList<>();

    @PostMapping("/verification/room")
    public Boolean verificationRoom(@RequestBody VerificationDto dto) {
        return roomService.verificationRoom(dto);
    }

    @PostMapping("/room")
    public void createRoom(@RequestBody RoomDto dto) {
        roomService.createRoom(dto);
    }

    @DeleteMapping("/{id}/room")
    public void removeRoom(@PathVariable Long id) {
        roomService.removeRoom(id);
    }

    @PostMapping("/chat/room/out")
    public void roomOut(@RequestBody SocketVo vo) throws JsonProcessingException {
        Room room = roomService.findRoom(vo.getRoomId());
        List<Map<String, Object>> userInfos = new ArrayList<>();

        String userName = "[알림]";
        String content = vo.getUserName() + "님이 채팅방에서 퇴장하였습니다.";
        session.stream().filter(map -> map.get("roomId").equals(vo.getRoomId())).forEach(map -> {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userName", map.get("userName"));
            userInfos.add(userInfo);
        });
        SocketVo result = new SocketVo(userName, content, getLocalTime(), userInfos,"notice");
        removeingSessionInformation(vo);

        String resultValue = objectToJsonString(result);

        template.convertAndSend("/sub/chat/room/" + room.getId(), resultValue);
    }

    @PostMapping("/chat/room/in")
    public void roomIn(@RequestBody SocketVo vo) throws JsonProcessingException {
        Room room = roomService.findRoom(vo.getRoomId());
        List<Map<String, Object>> userInfos = new ArrayList<>();

        String userName = "[알림]";
        String content = vo.getUserName() + "님이 채팅방에 입장하였습니다.";
        session.stream().filter(map -> map.get("roomId").equals(vo.getRoomId())).forEach(map -> {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userName", map.get("userName"));
            userInfos.add(userInfo);
        });
        SocketVo result = new SocketVo(userName, content, getLocalTime(), userInfos,"notice");

        creatingSessionInformation(vo);

        String resultValue = objectToJsonString(result);

        template.convertAndSend("/sub/chat/room/" + room.getId(), resultValue);
    }

    @MessageMapping("/chat/room")
    public void enter(SocketVo vo) throws JsonProcessingException {
        Room room = roomService.findRoom(vo.getRoomId());
        List<Map<String, Object>> userInfos = new ArrayList<>();

	    String userName = vo.getUserName();
        String content = vo.getContent();
        session.stream().filter(map -> map.get("roomId").equals(vo.getRoomId())).forEach(map -> {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userName", map.get("userName"));
            userInfos.add(userInfo);
        });
        SocketVo result = new SocketVo(userName, content, getLocalTime(), userInfos,"user");

        String resultValue = objectToJsonString(result);

        template.convertAndSend("/sub/chat/room/" + room.getId(), resultValue);
    }

    @Scheduled(fixedDelay = 1000)
    @SendTo("/sub/room/list")
    public void roomList() throws JsonProcessingException {
        List<Room> roomList = roomService.getRoomList();
        List<Map<String, Object>> roomResult = new ArrayList<>();

        for (Room room : roomList) {
            Map<String, Object> result = new HashMap<>();
            result.put("id", room.getId());
            result.put("name", room.getRoomNm());
            result.put("userCount", session.stream().filter(map -> map.get("roomId").equals(room.getId())).count());
            result.put("private", room.getPassword() != null ? true : false);
            roomResult.add(result);
        }

        String resultValue = objectToJsonString(roomResult);

        template.convertAndSend("/sub/room/list", resultValue);
    }

    public String getLocalTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public void creatingSessionInformation(SocketVo vo) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("roomId", vo.getRoomId());
        userInfo.put("userName", vo.getUserName());
        session.add(userInfo);
    }

    public void removeingSessionInformation(SocketVo vo) {
        session.removeIf(map -> map.get("roomId").equals(vo.getRoomId()) && map.get("userName").equals(vo.getUserName()));
    }

    public String objectToJsonString(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(o);
        return jsonInString;
    }
}
