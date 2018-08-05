package com.vm.service.mapper;

import com.vm.domain.*;
import com.vm.service.dto.CategoryDTO;

import org.mapstruct.*;

import java.util.List;

/**
 * Mapper for the entity Category and its DTO CategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {

    @Mapping(source = "parentCategory.id", target = "parentCategoryId")
    CategoryDTO toDto(Category category);

    @Mapping(source = "parentCategoryId", target = "parentCategory")
    @Mapping(target = "subCategories", ignore = true)
    Category toEntity(CategoryDTO categoryDTO);

    default Category fromId(Long id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.setId(id);
        return category;
    }
}
