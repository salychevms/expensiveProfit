package ru.salychev.expensiveprofit.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salychev.expensiveprofit.models.Unit;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Transactional
    List<Unit> findAllByName(String name);
    @Transactional
    Unit findByName(String name);
}
