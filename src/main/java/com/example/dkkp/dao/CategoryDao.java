package com.example.dkkp.dao;
import com.example.dkkp.model.Category_Entity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;
public class CategoryDao {
  private final EntityManager entityManager;
  public CategoryDao(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  public void createCategory(Category_Entity category) {
    entityManager.persist(category);
  }
  public List < Category_Entity > getFilteredCategories(
    Integer id,
    String name,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offSet
  ) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Category_Entity > query = cb.createQuery(Category_Entity.class);
    Root < Category_Entity > root = query.from(Category_Entity.class);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (id != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_CATEGORY"), id));
      hasConditions = true;
    }
    if (name != null && !name.trim().isEmpty()) {
      conditions = cb.and(conditions, cb.equal(root.get("NAME_CATEGORY"), name));
      hasConditions = true;
    }
    if (hasConditions) {
      query.where(conditions);
    } else {
      query.select(root);
    }
    if (sortField != null && sortOrder != null) {
      Path < ? > sortPath = root.get(sortField.toUpperCase());
      if ("desc".equalsIgnoreCase(sortOrder)) {
        query.orderBy(cb.desc(sortPath));
      } else {
        query.orderBy(cb.asc(sortPath));
      }
    }
    TypedQuery < Category_Entity > typedQuery = entityManager.createQuery(query);
    if (offSet != null) typedQuery.setFirstResult(offSet);
    if (setOff != null) typedQuery.setMaxResults(setOff);
    return typedQuery.getResultList();
  }
  public Integer getFilteredCategoriesCount(
    Integer id,
    String name
  ) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Long > query = cb.createQuery(Long.class);
    Root < Category_Entity > root = query.from(Category_Entity.class);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (id != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_CATEGORY"), id));
      hasConditions = true;
    }
    if (name != null && !name.trim().isEmpty()) {
      conditions = cb.and(conditions, cb.equal(root.get("NAME_CATEGORY"), name));
      hasConditions = true;
    }
    if (hasConditions) {
      query.where(conditions);
    }
    query.select(cb.count(root));
    TypedQuery < Long > typedQuery = entityManager.createQuery(query);
    Long result = typedQuery.getSingleResult();
    return result != null ? result.intValue() : 0;
  }
  public void updateCategory(Integer id, String name) {
    Category_Entity category = entityManager.find(Category_Entity.class, id);
    if (category == null) throw new RuntimeException("Category not found");
    category.setNAME_CATEGORY(name);
    entityManager.merge(category);
  }
  public void deleteCategoryById(Integer categoryId) {
    Category_Entity categoryToDelete = entityManager.find(Category_Entity.class, categoryId);
    if (categoryToDelete != null) {
      entityManager.remove(categoryToDelete);
      return;
    }
    throw new RuntimeException();
  }
}