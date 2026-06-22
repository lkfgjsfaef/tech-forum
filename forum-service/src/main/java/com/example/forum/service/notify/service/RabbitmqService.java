package com.example.forum.service.notify.service;

import org.springframework.stereotype.Service;

@Service
public class RabbitmqService {
    public void send(String queue, Object message) {
    }
    
    public Object receive(String queue) {
        return null;
    }
}
