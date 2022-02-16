package com.example.projectj.service;

import com.example.projectj.domain.Room;
import com.example.projectj.dto.RoomDto;
import com.example.projectj.dto.VerificationDto;

import java.util.List;

public interface RoomService {
    List<Room> getRoomList();
    Room findRoom(Long id);
    void createRoom(RoomDto dto);
    void removeRoom(Long id);
    Boolean verificationRoom(VerificationDto dto);
}
