package com.example.dkkp.dao;
import com.example.dkkp.model.*;
import com.example.dkkp.model.EnumType;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;
public class ProductFinalDao {
  private final EntityManager entityManager;
  public ProductFinalDao(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  public EntityManager getEntityManager() {
    return this.entityManager;
  }
  public void createProductFinal(Product_Final_Entity product) {
    entityManager.persist(product);
  }
  public Product_Final_Entity getProductFinalById(Integer ID_FINAL_PRODUCT) {
    String jpql = "SELECT u FROM Product_Final_Entity u WHERE u.ID_SP = :ID_FINAL_PRODUCT";
    TypedQuery < Product_Final_Entity > query = entityManager.createQuery(jpql, Product_Final_Entity.class);
    query.setParameter("ID_FINAL_PRODUCT", ID_FINAL_PRODUCT);
    return query.getSingleResult();
  }
  public List < Product_Final_Entity > getFilteredProductFinal(Integer ID_SP,
    Integer ID_BASE_PRODUCT,
    String NAME_PRODUCT_BASE,
    String NAME_PRODUCT,
    Double PRICE_SP,
    String typePrice,
    Integer QUANTITY,
    Double DISCOUNT,
    String typeDiscount,
    String typeQuantity,
    String sortField,
    String sortOrder,
    Integer offset,
    Integer setOff) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Product_Final_Entity > query = cb.createQuery(Product_Final_Entity.class);
    Root < Product_Final_Entity > root = query.from(Product_Final_Entity.class);
    Join < Product_Final_Entity, Product_Base_Entity > baseProductJoin = root.join("product_base", JoinType.LEFT);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (PRICE_SP != null) {
      conditions =
        switch (typePrice) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("PRICE_SP"), PRICE_SP));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("PRICE_SP"), PRICE_SP));
        case "=" -> cb.and(conditions, cb.equal(root.get("PRICE_SP"), PRICE_SP));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("PRICE_SP"), PRICE_SP));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("PRICE_SP"), PRICE_SP));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (DISCOUNT != null) {
      conditions =
        switch (typeDiscount) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("DISCOUNT"), DISCOUNT));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("DISCOUNT"), DISCOUNT));
        case "=" -> cb.and(conditions, cb.equal(root.get("DISCOUNT"), DISCOUNT));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("DISCOUNT"), DISCOUNT));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("DISCOUNT"), DISCOUNT));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (ID_SP != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_SP"), ID_SP));
      hasConditions = true;
    }
    if (NAME_PRODUCT != null) {
      conditions = cb.and(conditions, cb.like(root.get("NAME_PRODUCT"), "%" + NAME_PRODUCT + "%"));
      hasConditions = true;
    }
    if (ID_BASE_PRODUCT != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_BASE_PRODUCT"), ID_BASE_PRODUCT));
      hasConditions = true;
    }
    if (NAME_PRODUCT_BASE != null) {
      conditions = cb.and(conditions, cb.like(baseProductJoin.get("NAME_PRODUCT"), "%" + NAME_PRODUCT_BASE + "%"));
      hasConditions = true;
    }
    if (typeQuantity != null) {
      conditions =
        switch (typeQuantity) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("QUANTITY"), QUANTITY));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("QUANTITY"), QUANTITY));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("QUANTITY"), QUANTITY));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("QUANTITY"), QUANTITY));
        case "=" -> cb.and(conditions, cb.equal(root.get("QUANTITY"), QUANTITY));
        default -> conditions;
        };
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
    if (sortField != null && sortOrder != null) {
      Path < ? > sortPath;
      if ("NAME_BASE_PRODUCT".equalsIgnoreCase(sortField)) {
        sortPath = baseProductJoin.get("NAME_PRODUCT");
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
      Product_Final_Entity.class,
      root.get("ID_SP"),
      root.get("ID_BASE_PRODUCT"),
      root.get("NAME_PRODUCT"),
      root.get("QUANTITY"),
      root.get("PRICE_SP"),
      root.get("DISCOUNT"),
      root.get("IMAGE_SP"),
      baseProductJoin.get("NAME_PRODUCT"),
      root.get("DES_PRODUCT")
    ));
    TypedQuery < Product_Final_Entity > typedQuery = entityManager.createQuery(query);
    if (offset != null) typedQuery.setFirstResult(offset);
    if (setOff != null) typedQuery.setMaxResults(setOff);
    return typedQuery.getResultList();
  }
  public List < Product_Final_Entity > getProductFinalForDashBoard(LocalDateTime startDate, LocalDateTime endDate) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Product_Final_Entity > query = cb.createQuery(Product_Final_Entity.class);
    Root < Bill_Detail_Entity > billDetailRoot = query.from(Bill_Detail_Entity.class);
    Join < Bill_Detail_Entity, Product_Final_Entity > productFinalJoin = billDetailRoot.join("product_Final_Entity", JoinType.LEFT);
    Join < Bill_Detail_Entity, Bill_Entity > billJoin = billDetailRoot.join("bill_Entity", JoinType.LEFT);
    Predicate conditions = cb.conjunction();
    if (startDate != null) {
      conditions = cb.and(conditions, cb.greaterThanOrEqualTo(billJoin.get("DATE_EXP"), startDate));
    }
    if (endDate != null) {
      conditions = cb.and(conditions, cb.lessThanOrEqualTo(billJoin.get("DATE_EXP"), endDate));
    }
    conditions = cb.and(conditions, cb.notEqual(billJoin.get("BILL_STATUS"), EnumType.Status_Bill.PEN));
    conditions = cb.and(conditions, cb.notEqual(billJoin.get("BILL_STATUS"), EnumType.Status_Bill.CANC));
    query.where(conditions);
    Expression < Double > sumTotalPrice = cb.sum(billDetailRoot.get("TOTAL_DETAIL_PRICE"));
    Expression < Long > sumQuantity = cb.sum(billDetailRoot.get("QUANTITY_SP"));
    query.select(cb.construct(
      Product_Final_Entity.class,
      productFinalJoin.get("ID_SP"),
      productFinalJoin.get("NAME_PRODUCT"),
      sumTotalPrice,
      sumQuantity.as(Integer.class)
    ));
    query.groupBy(
      productFinalJoin.get("ID_SP"),
      productFinalJoin.get("NAME_PRODUCT")
    );
    TypedQuery < Product_Final_Entity > typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultList();
  }
  public Integer getFilteredProductFinalCount(Integer ID_SP,
    Integer ID_BASE_PRODUCT,
    String NAME_PRODUCT_BASE,
    String NAME_PRODUCT,
    Double PRICE_SP,
    String typePrice,
    Integer QUANTITY,
    Double DISCOUNT,
    String typeDiscount,
    String typeQuantity) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Long > query = cb.createQuery(Long.class);
    Root < Product_Final_Entity > root = query.from(Product_Final_Entity.class);
    Join < Product_Final_Entity, Product_Base_Entity > baseProductJoin = root.join("product_base", JoinType.LEFT);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (PRICE_SP != null) {
      conditions =
        switch (typePrice) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("PRICE_SP"), PRICE_SP));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("PRICE_SP"), PRICE_SP));
        case "=" -> cb.and(conditions, cb.equal(root.get("PRICE_SP"), PRICE_SP));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("PRICE_SP"), PRICE_SP));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("PRICE_SP"), PRICE_SP));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (DISCOUNT != null) {
      conditions =
        switch (typeDiscount) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("DISCOUNT"), DISCOUNT));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("DISCOUNT"), DISCOUNT));
        case "=" -> cb.and(conditions, cb.equal(root.get("DISCOUNT"), DISCOUNT));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("DISCOUNT"), DISCOUNT));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("DISCOUNT"), DISCOUNT));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (ID_SP != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_SP"), ID_SP));
      hasConditions = true;
    }
    if (NAME_PRODUCT != null) {
      conditions = cb.and(conditions, cb.like(root.get("NAME_PRODUCT"), "%" + NAME_PRODUCT + "%"));
      hasConditions = true;
    }
    if (ID_BASE_PRODUCT != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_BASE_PRODUCT"), ID_BASE_PRODUCT));
      hasConditions = true;
    }
    if (NAME_PRODUCT_BASE != null) {
      conditions = cb.and(conditions, cb.like(baseProductJoin.get("NAME_PRODUCT"), "%" + NAME_PRODUCT_BASE + "%"));
      hasConditions = true;
    }
    if (QUANTITY != null && typeQuantity != null) {
      conditions =
        switch (typeQuantity) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("QUANTITY"), QUANTITY));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("QUANTITY"), QUANTITY));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("QUANTITY"), QUANTITY));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("QUANTITY"), QUANTITY));
        case "=" -> cb.and(conditions, cb.equal(root.get("QUANTITY"), QUANTITY));
        default -> conditions;
        };
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
  public void deleteProductFinal(Integer ID_SP) {
    Product_Final_Entity product = entityManager.find(Product_Final_Entity.class, ID_SP);
    if (product != null) {
      entityManager.remove(product);
      return;
    }
    throw new RuntimeException("Cant find ID_SP to delete");
  }
  public void updateProductFinal(Integer ID_SP,
    Integer ID_BASE_PRODUCT,
    String NAME_PRODUCT,
    String DES_PRODUCT,
    Integer QUANTITY,
    Double DISCOUNT,
    String IMAGE_SP,
    Double Price
  ) {
    Product_Final_Entity product = entityManager.find(Product_Final_Entity.class, ID_SP);
    if (product == null) {
      throw new RuntimeException("Error occurred while updating product can not find product final to delete");
    }
    if (NAME_PRODUCT != null) product.setNAME_PRODUCT(NAME_PRODUCT);
    if (DES_PRODUCT != null) product.setDES_PRODUCT(DES_PRODUCT);
    if (QUANTITY != null) product.setQUANTITY(QUANTITY);
    if (DISCOUNT != null) product.setDISCOUNT(DISCOUNT);
    if (IMAGE_SP != null) product.setIMAGE_SP(IMAGE_SP);
    if (Price != null) product.setPRICE_SP(Price);
    if (ID_BASE_PRODUCT != null) product.setID_BASE_PRODUCT(ID_BASE_PRODUCT);
    try {
      entityManager.merge(product);
    } catch (RuntimeException e) {
      throw new RuntimeException("Error occurred while updating product", e);
    }
  }
}