package com.example.projectj.service;

import com.example.projectj.domain.Room;

import java.util.List;

public interface RoomService {
    List<Room> getRoomList();
    Room findRoom(Long id);
    void createRoom(String roomName);
}
