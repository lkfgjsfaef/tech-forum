package com.example.forum.service.user.service;

public interface UserTransferService {
    void transfer(String target);
    boolean transferUser(String username, String password);
}
