package ru.salychev.expensiveprofit.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salychev.expensiveprofit.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    List<User> findByUsername(String username);

    @Transactional
    List<User> findByTgId(String tgId);

    @Transactional
    List<User> findByEmail(String email);

    @Transactional
    List<User> findByPhoneNumber(String phoneNumber);
}
