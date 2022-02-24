package com.sihun.jpa.bookmanager.repository;

import com.sihun.jpa.bookmanager.domain.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHistoryRepository extends JpaRepository<UserHistory, String> {
}
