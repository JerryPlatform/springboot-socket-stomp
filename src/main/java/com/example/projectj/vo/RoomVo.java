package com.example.projectj.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomVo {
    private Long id;
    private String roomName;
}
