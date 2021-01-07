package lk.example.springsecurity.repository;

import lk.example.springsecurity.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByUserName(String userName);
}
