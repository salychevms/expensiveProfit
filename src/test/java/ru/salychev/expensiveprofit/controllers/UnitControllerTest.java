package ru.salychev.expensiveprofit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.salychev.expensiveprofit.models.Unit;
import ru.salychev.expensiveprofit.repo.UnitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitControllerTest {
    @Mock
    UnitRepository unitRepository;
    @InjectMocks
    UnitController unitController;

    @Test
    void testCreateUnit() {
        Unit unit=new Unit("testUnit");

        when(unitRepository.save(any(Unit.class))).thenReturn(unit);

        ResponseEntity<Unit> result=unitController.createUnit(unit);

        assertNotNull(result.getBody());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(unit,result.getBody());
    }

    @Test
    void testGetAllUnits() {
        List<Unit> unitList=new ArrayList<>();
        unitList.add(new Unit("testUnit1"));
        unitList.add(new Unit("testUnit2"));

        when(unitRepository.findAll()).thenReturn(unitList);

        ResponseEntity<List<Unit>> result=unitController.getAllUnits();

        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(unitList,result.getBody());
    }

    @Test
    void testFindById() {
        Unit testUnit=new Unit("findById");
        Long id=123L;
        testUnit.setId(id);

        when(unitRepository.findById(id)).thenReturn(Optional.of(testUnit));

        ResponseEntity<Unit> result=unitController.findById(id);

        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testUnit,result.getBody());
    }

    @Test
    void testUpdateById() {
        Unit updatedUnit=new Unit("ffff");
        Unit update=new Unit("UPDATE");

        when(unitRepository.findById(updatedUnit.getId())).thenReturn(Optional.of(updatedUnit));
        when(unitRepository.save(any(Unit.class))).thenReturn(update);

        ResponseEntity<Unit> result=unitController.updateById(updatedUnit.getId(), update);

        assertNotNull(result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(update, result.getBody());
    }

    @Test
    void deleteById() {
        Unit delete=new Unit("delete");

        ResponseEntity<HttpStatus> result=unitController.deleteById(delete.getId());

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(unitRepository, times(1)).deleteById(delete.getId());
    }

    @Test
    void deleteAllUnits() {
        ResponseEntity<HttpStatus> result=unitController.deleteAllUnits();

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(unitRepository, times(1)).deleteAll();
    }
}