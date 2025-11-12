package com.example.dkkp.dao;
import com.example.dkkp.model.Product_Option_Entity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;
public class ProductOptionDao {
  private final EntityManager entityManager;
  public ProductOptionDao(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  public EntityManager getEntityManager() {
    return this.entityManager;
  }
  public void createProductOption(Product_Option_Entity productOption) {
    entityManager.persist(productOption);
  }
  public List < Product_Option_Entity > getFilteredProductOption(Integer id, String name, String sortField, String sortOrder, Integer setOff, Integer offSet) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Product_Option_Entity > query = cb.createQuery(Product_Option_Entity.class);
    Root < Product_Option_Entity > root = query.from(Product_Option_Entity.class);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (id != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_OPTION"), id));
      hasConditions = true;
    }
    if (name != null) {
      conditions = cb.and(conditions, cb.equal(root.get("NAME_OPTION"), name));
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
    TypedQuery < Product_Option_Entity > typedQuery = entityManager.createQuery(query);
    if (offSet != null) typedQuery.setFirstResult(offSet);
    if (setOff != null) typedQuery.setMaxResults(setOff);
    return typedQuery.getResultList();
  }
  public Integer getFilteredProductOptionCount(Integer id, String name) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Long > query = cb.createQuery(Long.class);
    Root < Product_Option_Entity > root = query.from(Product_Option_Entity.class);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (id != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_OPTION"), id));
      hasConditions = true;
    }
    if (name != null) {
      conditions = cb.and(conditions, cb.like(root.get("NAME_OPTION"), "%" + name + "%"));
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
  public void deleteProductOptionById(Integer id) {
    Product_Option_Entity productOption = entityManager.find(Product_Option_Entity.class, id);
    if (productOption != null) {
      try {
        entityManager.remove(productOption);
        return;
      } catch (RuntimeException e) {
        throw new RuntimeException("please delete all option value belong this product option:" + id, e);
      }
    }
    throw new RuntimeException("Can not find product option with id:" + id);
  }
  public void updateProductOption(Integer id, String name) {
    Product_Option_Entity productOption = entityManager.find(Product_Option_Entity.class, id);
    if (productOption == null) {
      throw new RuntimeException("Cant not find Product Option to update with ID: " + id);
    }
    productOption.setNAME_OPTION(name);
    entityManager.merge(productOption);
  }
}