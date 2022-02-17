package com.sihun.jpa.bookmanager.repository;

import com.sihun.jpa.bookmanager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
