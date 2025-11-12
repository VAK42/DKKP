package com.example.dkkp.dao;
import com.example.dkkp.model.Category_Entity;
import com.example.dkkp.model.Product_Attribute_Entity;
import com.example.dkkp.model.Product_Attribute_Values_Entity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;
public class ProductAttributeDao {
  private final EntityManager entityManager;
  public ProductAttributeDao(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  public EntityManager getEntityManager() {
    return this.entityManager;
  }
  public void createProductAttribute(Product_Attribute_Entity productAttribute) {
    entityManager.persist(productAttribute);
  }
  public List < Product_Attribute_Entity > getFilteredProductAttribute(Integer ID_ATTRIBUTE, String NAME_ATTRIBUTE, Integer ID_CATEGORY, String NAME_CATEGORY, String sortField, String sortOrder, Integer setOff, Integer offSet) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Product_Attribute_Entity > query = cb.createQuery(Product_Attribute_Entity.class);
    Root < Product_Attribute_Entity > root = query.from(Product_Attribute_Entity.class);
    Join < Product_Attribute_Entity, Category_Entity > categoryJoin = root.join("category", JoinType.INNER);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (NAME_CATEGORY != null) {
      conditions = cb.and(conditions, cb.like(categoryJoin.get("NAME_CATEGORY"), "%" + NAME_CATEGORY + "%"));
      hasConditions = true;
    }
    if (ID_ATTRIBUTE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_ATTRIBUTE"), ID_ATTRIBUTE));
      hasConditions = true;
    }
    if (NAME_ATTRIBUTE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("NAME_ATTRIBUTE"), NAME_ATTRIBUTE));
      hasConditions = true;
    }
    if (ID_CATEGORY != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_CATEGORY"), ID_CATEGORY));
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
    query.select(cb.construct(
      Product_Attribute_Entity.class,
      root.get("ID_ATTRIBUTE"),
      root.get("NAME_ATTRIBUTE"),
      root.get("ID_CATEGORY"),
      categoryJoin.get("NAME_CATEGORY")
    ));
    TypedQuery < Product_Attribute_Entity > typedQuery = entityManager.createQuery(query);
    if (offSet != null) typedQuery.setFirstResult(offSet);
    if (setOff != null) typedQuery.setMaxResults(setOff);
    return typedQuery.getResultList();
  }
  public Integer getFilteredProductAttributeCount(Integer ID_ATTRIBUTE, String NAME_ATTRIBUTE, Integer ID_CATEGORY, String NAME_CATEGORY) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Long > query = cb.createQuery(Long.class);
    Root < Product_Attribute_Entity > root = query.from(Product_Attribute_Entity.class);
    Join < Product_Attribute_Entity, Category_Entity > categoryJoin = root.join("category", JoinType.INNER);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (NAME_CATEGORY != null) {
      conditions = cb.and(conditions, cb.like(categoryJoin.get("NAME_CATEGORY"), "%" + NAME_CATEGORY + "%"));
      hasConditions = true;
    }
    if (ID_ATTRIBUTE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_ATTRIBUTE"), ID_ATTRIBUTE));
      hasConditions = true;
    }
    if (NAME_ATTRIBUTE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("NAME_ATTRIBUTE"), NAME_ATTRIBUTE));
      hasConditions = true;
    }
    if (ID_CATEGORY != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_CATEGORY"), ID_CATEGORY));
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
  public void deleteProductAttributeById(Integer ID_ATTRIBUTE) {
    Product_Attribute_Entity productAttribute = entityManager.find(Product_Attribute_Entity.class, ID_ATTRIBUTE);
    if (productAttribute != null) {
      try {
        entityManager.remove(productAttribute);
        return;
      } catch (RuntimeException e) {
        throw new RuntimeException("please delete all attribute value belong this product attribute:" + ID_ATTRIBUTE, e);
      }
    }
    throw new RuntimeException("Cant find id - attribute to delete");
  }
  public void updateProductAttribute(Integer ID_ATTRIBUTE, Integer ID_CATEGORY, String NAME_ATTRIBUTE) {
    Product_Attribute_Entity productAttribute = entityManager.find(Product_Attribute_Entity.class, ID_ATTRIBUTE);
    if (productAttribute == null) {
      throw new RuntimeException("Cant not find Product Option to update with ID: " + ID_ATTRIBUTE);
    }
    if (ID_CATEGORY != null) productAttribute.setID_CATEGORY(ID_CATEGORY);
    if (NAME_ATTRIBUTE != null) productAttribute.setNAME_ATTRIBUTE(NAME_ATTRIBUTE);
    try {
      entityManager.merge(productAttribute);
    } catch (RuntimeException e) {
      throw new RuntimeException("Error occurred while updating Product Option with ID_BASEPRODUCT: " + ID_ATTRIBUTE, e);
    }
  }
}