package com.vm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vm.service.CategoryService;
import com.vm.web.rest.errors.BadRequestAlertException;
import com.vm.web.rest.util.HeaderUtil;
import com.vm.web.rest.util.PaginationUtil;
import com.vm.service.dto.CategoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.List;
import java.util.Optional;

import com.vm.service.mapper.CategoryMapper;
import javax.inject.Inject;


import java.util.ArrayList;
import java.util.List;
/**
 * REST controller for managing Category.
 */
@RestController
@RequestMapping("/api")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private static final String ENTITY_NAME = "category";

    private final CategoryService categoryService;

    @Inject
    private CategoryMapper categoryMapper;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * POST  /categories : Create a new category.
     *
     * @param categoryDTO the categoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categoryDTO, or with status 400 (Bad Request) if the category has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/categories")
    @Timed
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) throws URISyntaxException {
        log.debug("REST request to save Category : {}", categoryDTO);
        if (categoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoryDTO result = categoryService.save(categoryDTO);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categories : Updates an existing category.
     *
     * @param categoryDTO the categoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categoryDTO,
     * or with status 400 (Bad Request) if the categoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the categoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/categories")
    @Timed
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO) throws URISyntaxException {
        log.debug("REST request to update Category : {}", categoryDTO);
        if (categoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CategoryDTO result = categoryService.save(categoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, categoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categories : get all the categories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of categories in body
     */
    @GetMapping("/categories")
    @Timed
    public ResponseEntity<List<CategoryDTO>> getAllCategories(Pageable pageable) {
        log.debug("REST request to get a page of Categories");
        Page<CategoryDTO> page = categoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categories/:id : get the "id" category.
     *
     * @param id the id of the categoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/categories/{id}")
    @Timed
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        log.debug("REST request to get Category : {}", id);
        Optional<CategoryDTO> categoryDTO = categoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryDTO);
    }

    /**
     * DELETE  /categories/:id : delete the "id" category.
     *
     * @param id the id of the categoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/categories/{id}")
    @Timed
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.debug("REST request to delete Category : {}", id);
        categoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /get_sub_categories/:parent : get the "parent" category.
     *
     * @param parent the parent of the categoryDTO to retrieve all sub-categories of the parent
     * @return the ResponseEntity with status 200 (OK) and with body the categoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/get_sub_categories")
    @Timed
    public ResponseEntity<CategoryDTO> getAllSubCategoriesOfParentCategory(@RequestParam("parent") String parent) {
        log.debug("REST request to get All SubCategories of a parent : {}", parent);
        Optional<CategoryDTO> categoryDTO = categoryService.findByName(parent);
        return ResponseUtil.wrapOrNotFound(categoryDTO);
    }

    /**
     * GET  /search/:query : get the "query" category.
     *
     * @param query the query of the categoryDTO to retrieve all categories of the query
     * @return the ResponseEntity with status 200 (OK) and with body the categoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/search")
    @Timed
    public List<CategoryDTO> getAllCategoriesByQuery(@RequestParam("query") String query) {
        log.debug("REST request to get All Categories by a query : {}", query);
        List<CategoryDTO> categoryDTO = categoryService.findByQuery(query);
        return categoryDTO;
    }

    /**
     * POST  /store : Create a new category.
     *
     * @param url the categoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categoryDTO, or with status 400 (Bad Request) if the category has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/store")
    @Timed
    public void storeCategory(@RequestParam("url") String url) throws URISyntaxException {
        log.debug("REST request to store Category : {}", url);
        FileInputStream in = null;
        File file = null;
        List<String> list = new ArrayList<String>();

        // create new file object
        file = new File(url);

        List<String> lines = null;
        try {
            lines = FileUtils.readLines(file, "UTF-8");

            for (String line : lines) {
                String arr[]=line.split(",");
                int length = arr.length;
                Long tempId = null;
                System.out.println(line);
                for (int i=1;i<length;i++){
                    if(arr[i]!="" && arr[i]!=null){
                        Optional<CategoryDTO> obj = categoryService.findByName(arr[i].trim());
                        if(obj.equals(Optional.empty())){
                            CategoryDTO catObj=new CategoryDTO();
                            catObj.setName(arr[i].trim());
                            catObj.setParentCategoryId(tempId);
                            CategoryDTO result = categoryService.save(catObj);
                            tempId=result.getId();
                        }else{
                            tempId=obj.get().getId();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
