package com.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @GetMapping("/rooms")
    public String getRooms() {
        return "List of rooms";
    }
}
