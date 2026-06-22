package com.example.forum.service.user.service.impl;

import com.example.forum.service.user.service.UserTransferService;
import org.springframework.stereotype.Service;

@Service
public class UserTransferServiceImpl implements UserTransferService {

    @Override
    public void transfer(String target) {
    }

    @Override
    public boolean transferUser(String username, String password) {
        return false;
    }
}
