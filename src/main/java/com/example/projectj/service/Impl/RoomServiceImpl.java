package com.example.projectj.service.Impl;

import com.example.projectj.domain.Room;
import com.example.projectj.dto.RoomDto;
import com.example.projectj.dto.VerificationDto;
import com.example.projectj.repository.RoomRepository;
import com.example.projectj.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<Room> getRoomList() {
        return roomRepository.findAll();
    }

    @Override
    public Room findRoom(Long id) {
        return roomRepository.getById(id);
    }

    @Transactional
    @Override
    public void createRoom(RoomDto dto) {
        Room room = new Room();
        room.setRoomNm(dto.getRoomName());
        room.setPassword(dto.getPassword());
        roomRepository.save(room);
    }

    @Transactional
    @Override
    public void removeRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public Boolean verificationRoom(VerificationDto dto) {
        Room room = roomRepository.getById(dto.getRoomId());
        return room.getPassword().equals(dto.getPassword());
    }
}
