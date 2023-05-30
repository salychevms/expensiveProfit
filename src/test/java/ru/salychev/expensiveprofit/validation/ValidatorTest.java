package ru.salychev.expensiveprofit.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.repository.CrudRepository;
import ru.salychev.expensiveprofit.controllers.OperationController;
import ru.salychev.expensiveprofit.models.Currency;
import ru.salychev.expensiveprofit.models.Operation;
import ru.salychev.expensiveprofit.repo.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    @Test
    void validationOnCreation() {
    }

    @Test
    void validationOnUpdate() {
    }
}