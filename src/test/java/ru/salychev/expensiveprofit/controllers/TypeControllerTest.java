package ru.salychev.expensiveprofit.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.salychev.expensiveprofit.models.Type;
import ru.salychev.expensiveprofit.repo.TypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TypeControllerTest {
    @Mock
    TypeRepository typeRepository;
    @InjectMocks
    TypeController typeController;

    @Test
    void testCreateExpensiveProfitType() {
        Type testType = new Type();
        when(typeRepository.save(any(Type.class))).thenReturn(testType);

        ResponseEntity<Type> result = typeController.createExpensiveProfitType(testType);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(result.getBody(), testType);
    }

    @Test
    void testFindAll() {
        List<Type> typeList = new ArrayList<>();
        typeList.add(new Type("type1"));
        typeList.add(new Type("type2"));
        when(typeRepository.findAll()).thenReturn(typeList);

        ResponseEntity<List<Type>> result = typeController.findAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(result.getBody(), typeList);
    }

    @Test
    void testFindById() {
        Type type=new Type("test");
        Long id=type.getId();
        when(typeRepository.findById(id)).thenReturn(Optional.of(type));

        ResponseEntity<Type> result = typeController.findById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(result.getBody(), type);
    }

    @Test
    void testUpdateTypeById() {
        Type type=new Type("updatableType");
        Type updateType=new Type("update");

        when(typeRepository.findById(type.getId())).thenReturn(Optional.of(type));
        when(typeRepository.save(any(Type.class))).thenReturn(updateType);

        ResponseEntity<Type> result=typeController.updateTypeById(type.getId(), updateType);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(result.getBody(), updateType);
    }

    @Test
    void deleteById() {
        Type type=new Type("delete");

        ResponseEntity<HttpStatus> result=typeController.deleteById(type.getId());

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(typeRepository, times(1)).deleteById(type.getId());
    }

    @Test
    void deleteAll() {
        ResponseEntity<HttpStatus> result=typeController.deleteAll();

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(typeRepository, times(1)).deleteAll();
    }
}