package com.Service;

import com.Entity.Room;
import java.util.List;

public interface RoomServiceInt {
    List<Room> findAll();
    Room create(Room employee);
    Room findById(Long id);
    Room findByName(String Name);
    Room replace(Long id, Room newRoom);
    void deleteById(Long id);
}
