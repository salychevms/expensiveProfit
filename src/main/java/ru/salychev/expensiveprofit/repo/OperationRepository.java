package ru.salychev.expensiveprofit.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salychev.expensiveprofit.models.Operation;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Transactional
    List<Operation> findAllByUserId(Long id);

    @Transactional
    void deleteByUserId(Long userId);

    @Transactional
    List<Operation> findAllByUserIdAndOperationContaining(Long userId,String operation);

    @Transactional
    List<Operation> findAllByOperationContaining(String operation);
}
