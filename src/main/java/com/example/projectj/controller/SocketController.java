package com.example.projectj.controller;

import com.example.projectj.CommonUtil;
import com.example.projectj.domain.Room;
import com.example.projectj.dto.RoomDto;
import com.example.projectj.dto.VerificationDto;
import com.example.projectj.service.RoomService;
import com.example.projectj.vo.SocketVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log
@RestController
@EnableScheduling
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate template;

    private final RoomService roomService;

    List<Map<String, Object>> session = new ArrayList<>();

    @PostMapping("/verification/room")
    public Boolean verificationRoom(@RequestBody VerificationDto dto) {
        return roomService.verificationRoom(dto);
    }

    @PostMapping("/room")
    public void createRoom(@RequestBody RoomDto dto) throws JsonProcessingException {
        roomService.createRoom(dto);
        roomList();
    }

    @DeleteMapping("/{id}/room")
    public void removeRoom(@PathVariable Long id) throws JsonProcessingException {
        roomService.removeRoom(id);
        roomList();
    }

    @PostMapping("/chat/room/in")
    public void roomIn(@RequestBody SocketVo vo) throws JsonProcessingException {
        Room room = roomService.findRoom(vo.getRoomId());

        String userName = "[알림]";
        String content = vo.getUserName() + "님이 채팅방에 입장하였습니다.";
        SocketVo result = new SocketVo(userName, content, CommonUtil.getLocalTime(),"notice");
        String resultValue = CommonUtil.objectToJsonString(result);

        creatingSessionInformation(vo);

        roomUserSessionSynchronization(vo.getRoomId());

        template.convertAndSend("/sub/chat/room/" + room.getId(), resultValue);
    }

    @MessageMapping("/chat/room")
    public void chatting(SocketVo vo) throws JsonProcessingException {
        Room room = roomService.findRoom(vo.getRoomId());

	    String userName = vo.getUserName();
        String content = vo.getContent();
        SocketVo result = new SocketVo(userName, content, CommonUtil.getLocalTime(),"user");
        String resultValue = CommonUtil.objectToJsonString(result);

        template.convertAndSend("/sub/chat/room/" + room.getId(), resultValue);
    }

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

        String resultValue = CommonUtil.objectToJsonString(roomResult);

        template.convertAndSend("/sub/room/list", resultValue);
    }

    @EventListener
    public void SessionSubscribeEvent(SessionSubscribeEvent event) throws JsonProcessingException {
        String subscribeInfo = CommonUtil.extractDataFromEventMessages(event, "destination");
        roomList();

        if (subscribeInfo.contains("/sub/chat/room/")) {
            roomList();
        }
    }

    @EventListener
    public void SessionUnsubscribeEvent(SessionUnsubscribeEvent event) throws JsonProcessingException {
        String id = CommonUtil.extractDataFromEventMessages(event, "id");
        List<Map<String, Object>> userInfos = session.stream().filter(map -> map.get("userId").equals(id)).collect(Collectors.toList());
        roomList();

        if (userInfos.size() > 0) {
            session.removeIf(map -> map.get("userId").equals(id));
            roomOut(userInfos.stream().findFirst().get());
        }
    }

    public void roomOut(Map<String, Object> userInfo) throws JsonProcessingException {
        Room room = roomService.findRoom((Long) userInfo.get("roomId"));

        String userName = "[알림]";
        String content = userInfo.get("userName") + "님이 채팅방에서 퇴장하였습니다.";
        SocketVo result = new SocketVo(userName, content, CommonUtil.getLocalTime(),"notice");
        String resultValue = CommonUtil.objectToJsonString(result);

        roomUserSessionSynchronization(room.getId());
        template.convertAndSend("/sub/chat/room/" + room.getId(), resultValue);
    }

    public void roomUserSessionSynchronization(Long roomId) {
        List<Map<String, Object>> userInfos = new ArrayList<>();
        session.stream().filter(map -> map.get("roomId").equals(roomId)).forEach(map -> {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", map.get("userId"));
                userInfo.put("userName", map.get("userName"));
                userInfos.add(userInfo);
            });
        template.convertAndSend("/sub/chat/user/room/" + roomId, userInfos);
    }

    public void creatingSessionInformation(SocketVo vo) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("roomId", vo.getRoomId());
        userInfo.put("userId", vo.getUserId());
        userInfo.put("userName", vo.getUserName());
        session.add(userInfo);
    }

}
