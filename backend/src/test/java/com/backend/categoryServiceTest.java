package com.backend;

import com.backend.Entity.category;
import com.backend.dto.categoryDto;
import com.backend.repository.categoryRepo;
import com.backend.repository.productRepo;
import com.backend.services.categoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class categoryServiceTest {
    @Mock
    private categoryRepo categoryRepository;
    @Mock
    private productRepo productRepository;
    @InjectMocks
    private categoryService service;

    category cat;
    categoryDto catDto;

    @BeforeEach
    public void setUp() {
        cat = category.builder().id(1L).name("cat1").build();
        catDto = categoryDto.builder().id(1L).name("cat1").productCount(0L).build();
    }

    @Test
    public void testCreateCategory_Success() {
        when(categoryRepository.existsByNameIgnoreCase(catDto.getName())).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(cat);
        when(productRepository.countByCategoryId(any())).thenReturn(0L);
        categoryDto result = service.createCategory(catDto);
        assertEquals(catDto.getName(), result.getName());
    }

    @Test
    public void testCreateCategory_DuplicateName() {
        when(categoryRepository.existsByNameIgnoreCase(catDto.getName())).thenReturn(true);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createCategory(catDto));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    public void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat));
        when(productRepository.countByCategoryId(any())).thenReturn(0L);
        List<categoryDto> result = service.getAllCategories();
        assertEquals(1, result.size());
        assertEquals(cat.getName(), result.get(0).getName());
    }

    @Test
    public void testGetCategoryById_Success() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(cat));
        when(productRepository.countByCategoryId(any())).thenReturn(0L);
        categoryDto result = service.getCategoryById(1L);
        assertEquals(cat.getName(), result.getName());
    }

    @Test
    public void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getCategoryById(1L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testUpdateCategory_Success() {
        categoryDto updateDto = categoryDto.builder().id(1L).name("cat2").build();
        category updatedCat = category.builder().id(1L).name("cat2").build();
        when(categoryRepository.findById(any())).thenReturn(Optional.of(cat));
        when(categoryRepository.existsByNameIgnoreCase(updateDto.getName())).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(updatedCat);
        when(productRepository.countByCategoryId(any())).thenReturn(0L);
        categoryDto result = service.updateCategory(1L, updateDto);
        assertEquals("cat2", result.getName());
    }

    @Test
    public void testUpdateCategory_DuplicateName() {
        categoryDto updateDto = categoryDto.builder().id(1L).name("cat2").build();
        when(categoryRepository.findById(any())).thenReturn(Optional.of(cat));
        when(categoryRepository.existsByNameIgnoreCase(updateDto.getName())).thenReturn(true);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateCategory(1L, updateDto));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    public void testUpdateCategory_NotFound() {
        categoryDto updateDto = categoryDto.builder().id(1L).name("cat2").build();
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateCategory(1L, updateDto));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testDeleteCategory_Success() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(cat));
        when(productRepository.countByCategoryId(any())).thenReturn(0L);
        doNothing().when(categoryRepository).deleteById(any());
        assertDoesNotThrow(() -> service.deleteCategory(1L));
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    public void testDeleteCategory_HasProducts() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(cat));
        when(productRepository.countByCategoryId(any())).thenReturn(5L);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteCategory(1L));
        assertTrue(ex.getMessage().contains("Cannot delete category"));
    }

    @Test
    public void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteCategory(1L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    public void testConvertToDto() {
        when(productRepository.countByCategoryId(cat.getId())).thenReturn(3L);
        categoryDto dto = service.convertToDto(cat);
        assertEquals(cat.getId(), dto.getId());
        assertEquals(cat.getName(), dto.getName());
        assertEquals(3L, dto.getProductCount());
    }
}

