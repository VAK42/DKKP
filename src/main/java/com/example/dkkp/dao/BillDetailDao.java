package com.example.dkkp.dao;
import com.example.dkkp.model.Bill_Detail_Entity;
import com.example.dkkp.model.Product_Final_Entity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;
public class BillDetailDao {
  private final EntityManager entityManager;
  public BillDetailDao(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  public EntityManager getEntityManager() {
    return this.entityManager;
  }
  public void createBillDetail(List < Bill_Detail_Entity > listBillDetail) {
    int batchSize = 10;
    for (int i = 0; i < listBillDetail.size(); i++) {
      Bill_Detail_Entity billDetail = listBillDetail.get(i);
      entityManager.persist(billDetail);
      if (i % batchSize == 0 && i > 0) {
        entityManager.flush();
        entityManager.clear();
      }
    }
  }
  public List < Bill_Detail_Entity > getFilteredBillDetails(Integer ID_BILL_DETAIL,
    Double TOTAL_DETAIL_PRICE,
    String typePPrice,
    Double PRICE_SP,
    String typeUPrice,
    Integer ID_FINAL_PRODUCT,
    String NAME_FINAL_PRODUCT,
    Integer QUANTITY_SP,
    String typeQuantity,
    String ID_BILL,
    Boolean AVAILABLE,
    String sortField,
    String sortOrder,
    Integer offset,
    Integer setOff) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Bill_Detail_Entity > query = cb.createQuery(Bill_Detail_Entity.class);
    Root < Bill_Detail_Entity > root = query.from(Bill_Detail_Entity.class);
    Join < Bill_Detail_Entity, Product_Final_Entity > productFinalJoin = root.join("product_Final_Entity", JoinType.LEFT);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (ID_BILL_DETAIL != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_BILL_DETAIL"), ID_BILL_DETAIL));
      hasConditions = true;
    }
    if (TOTAL_DETAIL_PRICE != null) {
      conditions =
        switch (typePPrice) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        case "=" -> cb.and(conditions, cb.equal(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (PRICE_SP != null) {
      conditions =
        switch (typeUPrice) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("UNIT_PRICE"), PRICE_SP));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("UNIT_PRICE"), PRICE_SP));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("UNIT_PRICE"), PRICE_SP));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("UNIT_PRICE"), PRICE_SP));
        case "=" -> cb.and(conditions, cb.equal(root.get("UNIT_PRICE"), PRICE_SP));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (typeQuantity != null) {
      conditions =
        switch (typeQuantity) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("QUANTITY_SP"), QUANTITY_SP));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("QUANTITY_SP"), QUANTITY_SP));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("QUANTITY_SP"), QUANTITY_SP));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("QUANTITY_SP"), QUANTITY_SP));
        case "=" -> cb.and(conditions, cb.equal(root.get("QUANTITY_SP"), QUANTITY_SP));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (ID_BILL != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_BILL"), ID_BILL));
      hasConditions = true;
    }
    if (ID_FINAL_PRODUCT != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_FINAL_PRODUCT"), ID_FINAL_PRODUCT));
      hasConditions = true;
    }
    if (NAME_FINAL_PRODUCT != null) {
      conditions = cb.and(conditions, cb.like(productFinalJoin.get("NAME_PRODUCT"), "%" + NAME_FINAL_PRODUCT + "%"));
      hasConditions = true;
    }
    if (AVAILABLE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("AVAILABLE"), AVAILABLE));
      hasConditions = true;
    }
    if (hasConditions) {
      System.out.println("day co dieu kien");
      query.where(conditions);
    } else {
      System.out.println("day ko dieu kien");
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
      Bill_Detail_Entity.class,
      root.get("ID_BILL_DETAIL"),
      root.get("ID_BILL"),
      root.get("QUANTITY_SP"),
      root.get("TOTAL_DETAIL_PRICE"),
      root.get("UNIT_PRICE"),
      root.get("ID_FINAL_PRODUCT"),
      root.get("AVAILABLE"),
      productFinalJoin.get("NAME_PRODUCT")
    ));
    TypedQuery < Bill_Detail_Entity > typedQuery = entityManager.createQuery(query);
    if (offset != null) typedQuery.setFirstResult(offset);
    if (setOff != null) typedQuery.setMaxResults(setOff);
    return typedQuery.getResultList();
  }
  public Integer getFilteredBillDetailsCount(Integer ID_BILL_DETAIL,
    Double TOTAL_DETAIL_PRICE,
    String typePPrice,
    Double PRICE_SP,
    String typeUPrice,
    Integer ID_FINAL_PRODUCT,
    String NAME_FINAL_PRODUCT,
    Integer QUANTITY_SP,
    String typeQuantity,
    String ID_BILL,
    Boolean AVAILABLE) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery < Long > query = cb.createQuery(Long.class);
    Root < Bill_Detail_Entity > root = query.from(Bill_Detail_Entity.class);
    Join < Bill_Detail_Entity, Product_Final_Entity > productFinalJoin = root.join("product_Final_Entity", JoinType.LEFT);
    Predicate conditions = cb.conjunction();
    boolean hasConditions = false;
    if (ID_BILL_DETAIL != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_BILL_DETAIL"), ID_BILL_DETAIL));
      hasConditions = true;
    }
    if (typeQuantity != null) {
      conditions =
        switch (typeQuantity) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("QUANTITY_SP"), QUANTITY_SP));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("QUANTITY_SP"), QUANTITY_SP));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("QUANTITY_SP"), QUANTITY_SP));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("QUANTITY_SP"), QUANTITY_SP));
        case "=" -> cb.and(conditions, cb.equal(root.get("QUANTITY_SP"), QUANTITY_SP));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (TOTAL_DETAIL_PRICE != null) {
      conditions =
        switch (typePPrice) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        case "=" -> cb.and(conditions, cb.equal(root.get("TOTAL_DETAIL_PRICE"), TOTAL_DETAIL_PRICE));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (PRICE_SP != null) {
      conditions =
        switch (typeUPrice) {
        case "<" -> cb.and(conditions, cb.lessThan(root.get("UNIT_PRICE"), PRICE_SP));
        case "=>" -> cb.and(conditions, cb.greaterThanOrEqualTo(root.get("UNIT_PRICE"), PRICE_SP));
        case "<=" -> cb.and(conditions, cb.lessThanOrEqualTo(root.get("UNIT_PRICE"), PRICE_SP));
        case ">" -> cb.and(conditions, cb.greaterThan(root.get("UNIT_PRICE"), PRICE_SP));
        case "=" -> cb.and(conditions, cb.equal(root.get("UNIT_PRICE"), PRICE_SP));
        default -> conditions;
        };
      hasConditions = true;
    }
    if (ID_BILL != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_BILL"), ID_BILL));
      hasConditions = true;
    }
    if (ID_FINAL_PRODUCT != null) {
      conditions = cb.and(conditions, cb.equal(root.get("ID_FINAL_PRODUCT"), ID_FINAL_PRODUCT));
      hasConditions = true;
    }
    if (NAME_FINAL_PRODUCT != null) {
      conditions = cb.and(conditions, cb.like(productFinalJoin.get("NAME_PRODUCT"), "%" + NAME_FINAL_PRODUCT + "%"));
      hasConditions = true;
    }
    if (AVAILABLE != null) {
      conditions = cb.and(conditions, cb.equal(root.get("AVAILABLE"), AVAILABLE));
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
  public void cancelBillDetail(String ID_BILL) {
    List < Bill_Detail_Entity > billDetails = entityManager.createQuery(
        "SELECT bd FROM Bill_Detail_Entity bd WHERE bd.ID_BILL = :String", Bill_Detail_Entity.class)
      .setParameter("String", ID_BILL)
      .getResultList();
    if (billDetails.isEmpty()) {
      throw new RuntimeException("No Bill Details found for the given ID_PARENT");
    }
    for (Bill_Detail_Entity billDetail: billDetails) {
      billDetail.setAVAILABLE(false);
    }
  }
  public void updateBillDetail(Integer ID_BILL_DETAIL, Integer QUANTITY_SP, Double TOTAL_DETAIL_PRICE, Integer ID_FINAL_PRODUCT) {
    Bill_Detail_Entity billDetail = entityManager.find(Bill_Detail_Entity.class, ID_BILL_DETAIL);
    if (billDetail == null) {
      return;
    }
    if (QUANTITY_SP != null) billDetail.setQUANTITY_BILL(QUANTITY_SP);
    if (TOTAL_DETAIL_PRICE != null) billDetail.setTOTAL_DETAIL_PRICE(TOTAL_DETAIL_PRICE);
    if (ID_FINAL_PRODUCT != null) billDetail.setID_FINAL_PRODUCT(ID_FINAL_PRODUCT);
    entityManager.merge(billDetail);
  }
  public void deleteBillDetail(Integer ID_BILL_DETAIL) {
    Bill_Detail_Entity brandToDelete = entityManager.find(Bill_Detail_Entity.class, ID_BILL_DETAIL);
    if (brandToDelete != null) {
      entityManager.remove(brandToDelete);
      return;
    }
    throw new RuntimeException("No Bill Details found to delete");
  }
}