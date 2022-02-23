package com.example.projectj.graphQL.Room;

import com.example.projectj.domain.Room;
import com.example.projectj.service.RoomService;
import com.example.projectj.vo.Response;
import com.example.projectj.vo.Result;
import com.example.projectj.vo.RoomVo;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log
@Component
@RequiredArgsConstructor
@Transactional
public class RoomQuery implements GraphQLQueryResolver {
    private final RoomService roomService;

    private Function<Room, RoomVo> mapRoom = room -> RoomVo.builder()
            .id(room.getId())
            .roomName(room.getRoomNm())
            .build();

    public Response<Object> getRoomList() {
        List<Room> roomList = roomService.getRoomList();

        List<RoomVo> result = roomList.stream().map(mapRoom).collect(Collectors.toList());

        return Response.builder().response(Result.builder().build()).contents(result).build();
    }

    public List<Map<String, String>> getMapData() {
        List<Map<String, String>> result = new ArrayList<>();
        Random random = new Random();
        for (int i=1; i<6; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(i));
            map.put("value", String.valueOf(random.nextInt()));
            result.add(map);
        }

        return result;
    }
}
