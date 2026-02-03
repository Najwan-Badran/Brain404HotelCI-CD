package com.Service;


import com.Entity.Room;
import com.Repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomService {
    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Room> findAll() {
        return repository.findAll();
    }
}
