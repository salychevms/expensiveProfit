package ru.salychev.expensiveprofit.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salychev.expensiveprofit.models.Type;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {
    @Transactional
    List<Type> findAllByName(String name);

    @Transactional
    Type findByName(String name);
}
