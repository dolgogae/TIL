package com.sihun.jpa.bookmanager.domain.listener;

import com.sihun.jpa.bookmanager.domain.User;
import com.sihun.jpa.bookmanager.domain.UserHistory;
import com.sihun.jpa.bookmanager.repository.UserHistoryRepository;
import com.sihun.jpa.bookmanager.support.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class UserEntityListener {
    @PrePersist
    @PreUpdate
    public void prePersistAndPreUpdate(Object o){
        UserHistoryRepository userHistoryRepository = BeanUtils.getBean(UserHistoryRepository.class);

        User user = (User) o;
        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(user.getId());
        userHistory.setEmail(user.getEmail());
        userHistory.setName(user.getName());
        userHistory.setCreatedAt(user.getCreatedAt());
        userHistory.setUpdatedAt(user.getUpdatedAt());

        userHistoryRepository.save(userHistory);
    }
}
