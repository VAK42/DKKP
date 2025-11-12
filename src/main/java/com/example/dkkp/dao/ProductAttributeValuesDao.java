package com.example.dkkp.dao;
import com.example.dkkp.model.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;
public class ProductAttributeValuesDao {
  private final EntityManager entityManager;
  public ProductAttributeValuesDao(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  public EntityManager getEntityManager() {
    return this.entityManager;
  }
  public void createProductAttributeValues(Product_Attribute_Values_Entity attributeValue) {
    try {
      entityManager.persist(attributeValue);
    } catch (RuntimeException e) {
      throw new RuntimeException("Error creating attribute value", e);
    }
  }
  public Product_Attribute_Values_Entity getProductAttributeValuesById(Integer ID) {
    String jpql = "SELECT u FROM Product_Attribute_Values_Entity u WHERE u.ID = :ID";
    TypedQuery < Product_Attribute_Values_Entity > query = entityManager.createQuery(jpql, Product_Attribute_Values_Entity.class);
    query.setParameter("ID", ID);
    return query.getSingleResult();
  }
  public List < Product_Attribute_Values_Entity > getFilteredProductAttributeValues(Integer ID,
    String VALUE,
    Integer ID_ATTRIBUTE,
    String NAME_ATTRIBUTE,
    Integer ID_BASE_PRODUCT,
    String NAME_PRODUCT,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Product_Attribute_Values_Entity > query = cb.createQuery(Product_Attribute_Values_Entity.class);
    Root < Product_Attribute_Values_Entity > root = query.from(Product_Attribute_Values_Entity.class);
    Join < Product_Attribute_Values_Entity, Product_Attribute_Entity > productAttributeJoin = root.join("product_attribute_entity", JoinType.LEFT);
    Join < Product_Attribute_Values_Entity, Product_Base_Entity > productBasejoin = root.join("product_base_entity", JoinType.LEFT);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (ID != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID"), ID));
      hasConditions = true;
    }
    if (VALUE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("VALUE"), VALUE));
      hasConditions = true;
    }
    if (ID_ATTRIBUTE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_ATTRIBUTE"), ID_ATTRIBUTE));
      hasConditions = true;
    }
    if (NAME_ATTRIBUTE != null) {
      conditions = cb.and(conditions, cb.like(productAttributeJoin.get("NAME_ATTRIBUTE"), "%" + NAME_ATTRIBUTE + "%"));
      hasConditions = true;
    }
    if (ID_BASE_PRODUCT != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_BASE_PRODUCT"), ID_BASE_PRODUCT));
      hasConditions = true;
    }
    if (NAME_PRODUCT != null) {
      conditions = cb.and(conditions, cb.like(productBasejoin.get("NAME_PRODUCT"), "%" + NAME_PRODUCT + "%"));
      hasConditions = true;
    }
    if (hasConditions) {
      query.where(conditions);
    } else {
      query.select(root);
    }
    if (sortField != null && sortOrder != null) {
      Path < ? > sortPath;
      if ("NAME_ATTRIBUTE".equalsIgnoreCase(sortField)) {
        sortPath = productAttributeJoin.get("NAME_ATTRIBUTE");
      } else if ("NAME_PRODUCT".equalsIgnoreCase(sortField)) {
        sortPath = productBasejoin.get("NAME_PRODUCT");
      } else {
        sortPath = root.get(sortField.toUpperCase());
      }
      if ("desc".equalsIgnoreCase(sortOrder)) {
        query.orderBy(cb.desc(sortPath));
      } else {
        query.orderBy(cb.asc(sortPath));
      }
    }
    query.select(cb.construct(
      Product_Attribute_Values_Entity.class,
      root.get("ID"),
      root.get("ID_BASE_PRODUCT"),
      root.get("ID_ATTRIBUTE"),
      root.get("VALUE"),
      productAttributeJoin.get("NAME_ATTRIBUTE"),
      productBasejoin.get("NAME_PRODUCT")
    ));
    TypedQuery < Product_Attribute_Values_Entity > typedQuery = entityManager.createQuery(query);
    if (offset != null) typedQuery.setFirstResult(offset);
    if (setOff != null) typedQuery.setMaxResults(setOff);
    return typedQuery.getResultList();
  }
  public Integer getFilteredProductAttributeValuesCount(Integer ID, String VALUE, Integer ID_ATTRIBUTE, String NAME_ATTRIBUTE, Integer ID_BASE_PRODUCT, String NAME_PRODUCT) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Long > query = cb.createQuery(Long.class);
    Root < Product_Attribute_Values_Entity > root = query.from(Product_Attribute_Values_Entity.class);
    Join < Product_Attribute_Values_Entity, Product_Attribute_Entity > productAttributeJoin = root.join("product_attribute_entity", JoinType.LEFT);
    Join < Product_Attribute_Values_Entity, Product_Base_Entity > productBasejoin = root.join("product_base_entity", JoinType.LEFT);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (ID != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID"), ID));
      hasConditions = true;
    }
    if (VALUE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("VALUE"), VALUE));
      hasConditions = true;
    }
    if (ID_ATTRIBUTE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_ATTRIBUTE"), ID_ATTRIBUTE));
      hasConditions = true;
    }
    if (NAME_ATTRIBUTE != null) {
      conditions = cb.and(conditions, cb.like(productAttributeJoin.get("NAME_ATTRIBUTE"), "%" + NAME_ATTRIBUTE + "%"));
      hasConditions = true;
    }
    if (ID_BASE_PRODUCT != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_BASE_PRODUCT"), ID_BASE_PRODUCT));
      hasConditions = true;
    }
    if (NAME_PRODUCT != null) {
      conditions = cb.and(conditions, cb.like(productBasejoin.get("NAME_PRODUCT"), "%" + NAME_PRODUCT + "%"));
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
  public void deleteAttributeValues(Integer ID, Integer ID_ATTRIBUTE, Integer ID_BASE_PRODUCT) {
    Product_Attribute_Values_Entity productAttributeValues = entityManager.find(Product_Attribute_Values_Entity.class, ID);
    if (productAttributeValues != null) {
      try {
        entityManager.remove(productAttributeValues);
        return;
      } catch (RuntimeException e) {
        throw new RuntimeException("please delete all attribute value belong this product attribute:" + ID_ATTRIBUTE, e);
      }
    }
    throw new RuntimeException("Cant find id - attribute to delete");
  }
  public void updateProductAttributeValues(Integer ID,
    String VALUE,
    Integer ID_BASE_PRODUCT,
    Integer ID_ATTRIBUTE
  ) {
    Product_Attribute_Values_Entity attributeValue = entityManager.find(Product_Attribute_Values_Entity.class, ID);
    if (attributeValue == null) {
      throw new RuntimeException("Cannot find Product Attribute Value with ID: " + ID);
    }
    if (VALUE != null) attributeValue.setVALUE(VALUE);
    if (ID_ATTRIBUTE != null) attributeValue.setID_ATTRIBUTE(ID_ATTRIBUTE);
    if (ID_BASE_PRODUCT != null) attributeValue.setID_BASE_PRODUCT(ID_BASE_PRODUCT);
    entityManager.merge(attributeValue);
  }
}