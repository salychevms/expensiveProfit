package ru.salychev.expensiveprofit.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salychev.expensiveprofit.models.Expense;
import ru.salychev.expensiveprofit.models.User;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Transactional
    List<Expense> findAllByUser(User user);
}
