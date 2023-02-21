package com.example.datn.service;

import com.example.datn.domain.entity.User;
import com.example.datn.domain.repository.UserRepository;
import com.example.datn.domain.utility.Hashing;
import com.example.datn.domain.utility.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public int signup(User user) {
        if(userRepository.checkExistByName(user.getName()) == 1) {
            return 0;
        }
        user.setUserID(SequenceGenerator.getInstance().nextId());
        user.setRole(0);
        user.setPassword(Hashing.hash(user.getPassword()));

        userRepository.save(user);
        return 1;
    }

    public User login(User user) {
        String password = Hashing.hash(user.getPassword());
        User user1 = userRepository.login(user.getName(), password);
        user1.setPassword(null);
        return user1;
    }

    public int updateUser(User user) {
        return userRepository.updateUser(user.getName(), user.getPhone(), user.getAddress(), user.getUserID());
    }

}
