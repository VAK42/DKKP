package com.example.dkkp.service;
import com.example.dkkp.dao.ProductOptionDao;
import com.example.dkkp.dao.ProductOptionValuesDao;
import com.example.dkkp.dao.ProductFinalDao;
import com.example.dkkp.model.*;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
public class ProductFinalService {
  private final ProductFinalDao productFinalDao;
  private final ProductOptionDao productOptionDao;
  private final ProductOptionValuesDao productOptionValuesDao;
  public ProductFinalService(EntityManager entityManager) {
    this.productFinalDao = new ProductFinalDao(entityManager);
    this.productOptionDao = new ProductOptionDao(entityManager);
    this.productOptionValuesDao = new ProductOptionValuesDao(entityManager);
  }
  public List < Product_Final_Entity > getProductFinalByCombinedCondition(
    Product_Final_Entity product_Final_Entity,
    String typePrice,
    String typeDiscount,
    String typeQuantity,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset
  ) {
    Integer ID_SP = product_Final_Entity.getID_SP();
    String NAME_PRODUCT = product_Final_Entity.getNAME_PRODUCT();
    Double PRICE_SP = product_Final_Entity.getPRICE_SP();
    Integer QUANTITY = product_Final_Entity.getQUANTITY();
    Double DISCOUNT = product_Final_Entity.getDISCOUNT();
    Integer ID_BASE_PRODUCT = product_Final_Entity.getID_BASE_PRODUCT();
    String NAME_PRODUCT_BASE = product_Final_Entity.getNAME_PRODUCT_BASE();
    return productFinalDao.getFilteredProductFinal(
      ID_SP, ID_BASE_PRODUCT, NAME_PRODUCT_BASE, NAME_PRODUCT, PRICE_SP, typePrice, QUANTITY, DISCOUNT, typeDiscount, typeQuantity, sortField, sortOrder, offset, setOff
    );
  }
  public List < Product_Final_Entity > getProductDashBoard(LocalDateTime startDate, LocalDateTime endDate) {
    if (startDate == null) {
      startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
    } else {
      startDate = startDate.toLocalDate().atStartOfDay();
    }
    if (endDate == null) {
      endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59, 999999999);
    } else {
      endDate = endDate.toLocalDate().atTime(23, 59, 59, 999999999);
    }
    return productFinalDao.getProductFinalForDashBoard(startDate, endDate);
  }
  public Integer getCountProductFinalByCombinedCondition(
    Product_Final_Entity product_Final_Entity,
    String typePrice,
    String typeDiscount,
    String typeQuantity
  ) {
    Integer ID_SP = product_Final_Entity.getID_SP();
    String NAME_PRODUCT = product_Final_Entity.getNAME_PRODUCT();
    Double PRICE_SP = product_Final_Entity.getPRICE_SP();
    Integer QUANTITY = product_Final_Entity.getQUANTITY();
    Double DISCOUNT = product_Final_Entity.getDISCOUNT();
    Integer ID_BASE_PRODUCT = product_Final_Entity.getID_BASE_PRODUCT();
    String NAME_PRODUCT_BASE = product_Final_Entity.getNAME_PRODUCT_BASE();
    return productFinalDao.getFilteredProductFinalCount(
      ID_SP, ID_BASE_PRODUCT, NAME_PRODUCT_BASE, NAME_PRODUCT, PRICE_SP, typePrice, QUANTITY, DISCOUNT, typeDiscount, typeQuantity);
  }
  public Product_Final_Entity getProductByID(Integer id) {
    return productFinalDao.getProductFinalById(id);
  }
  public void deleteProductFinal(Integer id) {
    productFinalDao.deleteProductFinal(id);
  }
  public void updateProductFinal(Product_Final_Entity product_Final_Entity) {
    Integer ID_SP = product_Final_Entity.getID_SP();
    String NAME_PRODUCT = product_Final_Entity.getNAME_PRODUCT();
    String DES_PRODUCT = product_Final_Entity.getDES_PRODUCT();
    Double PRICE_SP = product_Final_Entity.getPRICE_SP();
    String IMAGE_SP = product_Final_Entity.getIMAGE_SP();
    Integer QUANTITY = product_Final_Entity.getQUANTITY();
    Double DISCOUNT = product_Final_Entity.getDISCOUNT();
    Integer ID_BASE_PRODUCT = product_Final_Entity.getID_BASE_PRODUCT();
    productFinalDao.updateProductFinal(ID_SP, ID_BASE_PRODUCT, NAME_PRODUCT, DES_PRODUCT, QUANTITY, DISCOUNT, IMAGE_SP, PRICE_SP);
  }
  public void createProductFinal(Product_Final_Entity product_Final_Entity) {
    Integer ID_SP = product_Final_Entity.getID_SP();
    String NAME_PRODUCT = product_Final_Entity.getNAME_PRODUCT();
    String DES_PRODUCT = product_Final_Entity.getDES_PRODUCT();
    Double PRICE_SP = product_Final_Entity.getPRICE_SP();
    String IMAGE_SP = product_Final_Entity.getIMAGE_SP();
    product_Final_Entity.setQUANTITY(0);
    Double DISCOUNT = product_Final_Entity.getDISCOUNT();
    productFinalDao.createProductFinal(product_Final_Entity);
  }
  public List < Product_Option_Entity > getProductOptionCombinedCondition(
    Product_Option_Entity productOptionEntity,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset
  ) {
    Integer ID = productOptionEntity.getID_OPTION();
    String NAME = productOptionEntity.getNAME_OPTION();
    return productOptionDao.getFilteredProductOption(ID, NAME, sortField, sortOrder, setOff, offset);
  }
  public Integer getCountProductOptionCombinedCondition(
    Product_Option_Entity productOptionEntity
  ) {
    Integer ID = productOptionEntity.getID_OPTION();
    String NAME = productOptionEntity.getNAME_OPTION();
    return productOptionDao.getFilteredProductOptionCount(ID, NAME);
  }
  public void deleteProductOption(Integer id) {
    productOptionDao.deleteProductOptionById(id);
  }
  public void updateProductOption(Product_Option_Entity productOption) {
    Integer idOption = productOption.getID_OPTION();
    String nameOption = productOption.getNAME_OPTION();
    productOptionDao.updateProductOption(idOption, nameOption);
  }
  public void createProductOption(Product_Option_Entity productOption) {
    Integer ID = productOption.getID_OPTION();
    String NAME = productOption.getNAME_OPTION();
    productOptionDao.createProductOption(productOption);
  }
  public List < Product_Option_Values_Entity > getProductOptionValuesCombinedCondition(
    Product_Option_Values_Entity productOptionValues,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset
  ) {
    Integer ID = productOptionValues.getID();
    Integer ID_OPTION = productOptionValues.getID_OPTION();
    String VALUE = productOptionValues.getVALUE();
    Integer ID_FINAL_PRODUCT = productOptionValues.getID_FINAL_PRODUCT();
    String NAME_OPTION = productOptionValues.getNAME_OPTION();
    String NAME_PRODUCT = productOptionValues.getNAME_PRODUCT();
    return productOptionValuesDao.getFilteredProductOptionValue(ID, ID_OPTION, NAME_OPTION, VALUE, ID_FINAL_PRODUCT, NAME_PRODUCT, sortField, sortOrder, setOff, offset);
  }
  public Integer getCountProductOptionValuesCombinedCondition(
    Product_Option_Values_Entity productOptionValues
  ) {
    Integer ID = productOptionValues.getID();
    Integer ID_OPTION = productOptionValues.getID_OPTION();
    String VALUE = productOptionValues.getVALUE();
    Integer ID_FINAL_PRODUCT = productOptionValues.getID_FINAL_PRODUCT();
    String NAME_OPTION = productOptionValues.getNAME_OPTION();
    String NAME_PRODUCT = productOptionValues.getNAME_PRODUCT();
    return productOptionValuesDao.getFilteredProductOptionValueCount(ID, ID_OPTION, NAME_OPTION, VALUE, ID_FINAL_PRODUCT, NAME_PRODUCT);
  }
  public void deleteProductOptionValues(Integer id, Integer idOption, Integer idFinalProduct) {
    productOptionValuesDao.deleteOptionValues(id, idOption, idFinalProduct);
  }
  public void updateProductOptionValues(Product_Option_Values_Entity productOptionValues) {
    Integer ID = productOptionValues.getID();
    Integer ID_OPTION = productOptionValues.getID_OPTION();
    String VALUE = productOptionValues.getVALUE();
    Integer ID_FINAL_PRODUCT = productOptionValues.getID_FINAL_PRODUCT();
    productOptionValuesDao.updateProductOptionValues(ID, VALUE, ID_OPTION, ID_FINAL_PRODUCT);
  }
  public void createProductOptionValues(Product_Option_Values_Entity productOptionValues) {
    Integer ID = productOptionValues.getID();
    Integer ID_OPTION = productOptionValues.getID_OPTION();
    String VALUE = productOptionValues.getVALUE();
    Integer ID_FINAL_PRODUCT = productOptionValues.getID_FINAL_PRODUCT();
    productOptionValuesDao.createProductOptionValues(productOptionValues);
  }
}