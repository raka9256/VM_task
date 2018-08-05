package com.vm.service;

import com.vm.domain.Category;
import com.vm.repository.CategoryRepository;
import com.vm.service.dto.CategoryDTO;
import com.vm.service.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Category.
 */
@Service
@Transactional
public class CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save
     * @return the persisted entity
     */
    public CategoryDTO save(CategoryDTO categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
        Category category = categoryMapper.toEntity(categoryDTO);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categoryRepository.findAll(pageable)
            .map(categoryMapper::toDto);
    }


    /**
     * Get one category by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CategoryDTO> findOne(Long id) {
        log.debug("Request to get Category : {}", id);
        return categoryRepository.findById(id)
            .map(categoryMapper::toDto);
    }

    /**
     * Delete the category by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.deleteById(id);
    }

    /**
     * Get one category by name and all its subcategories.
     *
     * @param name the name of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CategoryDTO> findByName(String name) {
        log.debug("Request to get Category : {}", name);
        return categoryRepository.findFirstByNameIgnoreCase(name)
            .map(categoryMapper::toDto);
    }

    /**
     * Get all category by query.
     *
     * @param query the query of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<CategoryDTO> findByQuery(String query) {
        log.debug("Request to get Category by query : {}", query);
        return categoryRepository.findTop10ByNameContainingIgnoreCase(query).stream()
            .map(categoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
