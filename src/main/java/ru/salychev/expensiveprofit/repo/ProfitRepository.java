package ru.salychev.expensiveprofit.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salychev.expensiveprofit.models.Profit;

import java.util.List;


@Repository
public interface ProfitRepository extends JpaRepository<Profit, Long> {
    @Transactional
    List<Profit> findAllProfitsByUserId(Long userId);

    @Transactional
    void deleteAllProfitsByUserId(Long userId);
}
