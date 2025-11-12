package com.example.dkkp.dao;
import com.example.dkkp.model.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;
public class ProductOptionValuesDao {
  private final EntityManager entityManager;
  public ProductOptionValuesDao(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  public EntityManager getEntityManager() {
    return this.entityManager;
  }
  public void createProductOptionValues(Product_Option_Values_Entity optionValue) {
    entityManager.persist(optionValue);
  }
  public boolean updateOptionValue(Integer id,
    String value,
    Integer idOption,
    Integer idFinalProduct) {
    EntityTransaction transaction = entityManager.getTransaction();
    Product_Option_Values_Entity productOption_values_entity = entityManager.find(Product_Option_Values_Entity.class, id);
    if (productOption_values_entity == null) {
      return false;
    }
    if (value != null) productOption_values_entity.setVALUE(value);
    if (idOption != null) productOption_values_entity.setID_OPTION(idOption);
    if (idFinalProduct != null) productOption_values_entity.setID_FINAL_PRODUCT(idFinalProduct);
    entityManager.merge(productOption_values_entity);
    transaction.commit();
    return true;
  }
  public List < Product_Option_Values_Entity > getFilteredProductOptionValue(Integer id, Integer idOption, String NAME_OPTION, String value, Integer idFinalProduct, String NAME_PRODUCT, String sortField, String sortOrder, Integer setOff, Integer offset) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Product_Option_Values_Entity > query = cb.createQuery(Product_Option_Values_Entity.class);
    Root < Product_Option_Values_Entity > root = query.from(Product_Option_Values_Entity.class);
    Join < Product_Option_Values_Entity, Product_Option_Entity > productOptionjoin = root.join("product_options", JoinType.LEFT);
    Join < Product_Option_Values_Entity, Product_Final_Entity > productFinaljoin = root.join("product_final", JoinType.LEFT);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (id != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID"), id));
      hasConditions = true;
    }
    if (idOption != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_OPTION"), idOption));
      hasConditions = true;
    }
    if (NAME_OPTION != null) {
      conditions = cb.and(conditions, cb.like(productOptionjoin.get("NAME_OPTION"), "%" + NAME_OPTION + "%"));
      hasConditions = true;
    }
    if (value != null) {
      conditions = cb.and(conditions, cb.equal(root.get("VALUE"), value));
      hasConditions = true;
    }
    if (idFinalProduct != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_FINAL_PRODUCT"), idFinalProduct));
      hasConditions = true;
    }
    if (NAME_PRODUCT != null) {
      conditions = cb.and(conditions, cb.like(productFinaljoin.get("NAME_PRODUCT"), "%" + NAME_PRODUCT + "%"));
      hasConditions = true;
    }
    if (hasConditions) {
      query.where(conditions);
    } else {
      query.select(root);
    }
    if (sortField != null && sortOrder != null) {
      Path < ? > sortPath;
      if ("NAME_PRODUCT".equalsIgnoreCase(sortField)) {
        sortPath = productFinaljoin.get("NAME_PRODUCT");
      } else if ("NAME_OPTION".equalsIgnoreCase(sortField)) {
        sortPath = productOptionjoin.get("NAME_OPTION");
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
      Product_Option_Values_Entity.class,
      root.get("ID"),
      root.get("ID_OPTION"),
      root.get("VALUE"),
      root.get("ID_FINAL_PRODUCT"),
      productOptionjoin.get("NAME_OPTION"),
      productFinaljoin.get("NAME_PRODUCT")
    ));
    TypedQuery < Product_Option_Values_Entity > typedQuery = entityManager.createQuery(query);
    if (offset != null) typedQuery.setFirstResult(offset);
    if (setOff != null) typedQuery.setMaxResults(setOff);
    return typedQuery.getResultList();
  }
  public Integer getFilteredProductOptionValueCount(Integer id, Integer idOption, String NAME_OPTION, String value, Integer idFinalProduct, String NAME_PRODUCT) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Long > query = cb.createQuery(Long.class);
    Root < Product_Option_Values_Entity > root = query.from(Product_Option_Values_Entity.class);
    Join < Product_Option_Values_Entity, Product_Option_Entity > productOptionjoin = root.join("product_options", JoinType.INNER);
    Join < Product_Option_Values_Entity, Product_Final_Entity > productFinaljoin = root.join("product_final", JoinType.INNER);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (id != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID"), id));
      hasConditions = true;
    }
    if (idOption != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_OPTION"), idOption));
      hasConditions = true;
    }
    if (NAME_OPTION != null) {
      conditions = cb.and(conditions, cb.like(productOptionjoin.get("NAME_OPTION"), "%" + NAME_OPTION + "%"));
      hasConditions = true;
    }
    if (value != null) {
      conditions = cb.and(conditions, cb.equal(root.get("VALUE"), value));
      hasConditions = true;
    }
    if (idFinalProduct != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_FINAL_PRODUCT"), idFinalProduct));
      hasConditions = true;
    }
    if (NAME_PRODUCT != null) {
      conditions = cb.and(conditions, cb.like(productFinaljoin.get("NAME_PRODUCT"), "%" + NAME_PRODUCT + "%"));
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
  public void deleteOptionValues(Integer ID, Integer ID_OPTION, Integer ID_FINAL_PRODUCT) {
    Product_Option_Values_Entity productOptionValues = entityManager.find(Product_Option_Values_Entity.class, ID);
    if (productOptionValues != null) {
      try {
        entityManager.remove(productOptionValues);
        return;
      } catch (RuntimeException e) {
        throw new RuntimeException("please delete all attribute value belong this product opton value:" + ID, e);
      }
    }
    throw new RuntimeException("Cant find id - option value to delete");
  }
  public void updateProductOptionValues(Integer id, String value, Integer idOption, Integer idFinalProduct) {
    Product_Option_Values_Entity optionValue = entityManager.find(Product_Option_Values_Entity.class, id);
    if (optionValue == null) {
      throw new RuntimeException("Can not find option value to delete");
    }
    if (value != null) optionValue.setVALUE(value);
    if (idOption != null) optionValue.setID_OPTION(idOption);
    if (idFinalProduct != null) optionValue.setID_FINAL_PRODUCT(idFinalProduct);
    entityManager.merge(optionValue);
  }
}