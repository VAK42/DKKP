package com.example.dkkp.service;
import com.example.dkkp.dao.*;
import com.example.dkkp.model.Product_Attribute_Entity;
import com.example.dkkp.model.Product_Attribute_Values_Entity;
import com.example.dkkp.model.Product_Base_Entity;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
public class ProductBaseService {
  private final ProductBaseDao productBaseDao;
  private final ProductAttributeDao productAttributeDao;
  private final ProductAttributeValuesDao productAttributeValuesDao;
  public ProductBaseService(EntityManager entityManager) {
    this.productBaseDao = new ProductBaseDao(entityManager);
    this.productAttributeDao = new ProductAttributeDao(entityManager);
    this.productAttributeValuesDao = new ProductAttributeValuesDao(entityManager);
  }
  public List < Product_Base_Entity > getProductBaseByCombinedCondition(
    Product_Base_Entity product_BASE_entity,
    String sortField,
    String sortOrder,
    String typeDate,
    String typeQuantity,
    String typeView,
    Integer setOff,
    Integer offset
  ) {
    Integer idBaseProduct = product_BASE_entity.getID_BASE_PRODUCT();
    String NAME_PRODUCT = product_BASE_entity.getNAME_PRODUCT();
    Integer TOTAL_QUANTITY = product_BASE_entity.getTOTAL_QUANTITY();
    LocalDateTime DATE_RELEASE = product_BASE_entity.getDATE_RELEASE();
    Integer VIEW_COUNT = product_BASE_entity.getVIEW_COUNT();
    Integer ID_CATEGORY = product_BASE_entity.getID_CATEGORY();
    Integer ID_BRAND = product_BASE_entity.getID_BRAND();
    String NAME_BRAND = product_BASE_entity.getNAME_BRAND();
    String NAME_CATEGORY = product_BASE_entity.getNAME_CATEGORY();
    return productBaseDao.getFilteredProductBase(
      idBaseProduct, NAME_PRODUCT, ID_CATEGORY, NAME_CATEGORY, ID_BRAND, NAME_BRAND, TOTAL_QUANTITY, typeQuantity, DATE_RELEASE, typeDate, VIEW_COUNT, typeView, sortField, sortOrder, offset, setOff
    );
  }
  public Integer getCountProductBase(
    Product_Base_Entity product_BASE_entity,
    String typeDate,
    String typeQuantity,
    String typeView
  ) {
    Integer idBaseProduct = product_BASE_entity.getID_BASE_PRODUCT();
    String NAME_PRODUCT = product_BASE_entity.getNAME_PRODUCT();
    Integer TOTAL_QUANTITY = product_BASE_entity.getTOTAL_QUANTITY();
    LocalDateTime DATE_RELEASE = product_BASE_entity.getDATE_RELEASE();
    Integer VIEW_COUNT = product_BASE_entity.getVIEW_COUNT();
    Integer ID_CATEGORY = product_BASE_entity.getID_CATEGORY();
    Integer ID_BRAND = product_BASE_entity.getID_BRAND();
    String NAME_BRAND = product_BASE_entity.getNAME_BRAND();
    String NAME_CATEGORY = product_BASE_entity.getNAME_CATEGORY();
    return productBaseDao.getFilteredProductBaseCount(
      idBaseProduct, NAME_PRODUCT, ID_CATEGORY, NAME_CATEGORY, ID_BRAND, NAME_BRAND, TOTAL_QUANTITY, typeQuantity, DATE_RELEASE, typeDate, VIEW_COUNT, typeView);
  }
  public Product_Base_Entity getProductBaseByID(Integer id) {
    return productBaseDao.getProductBaseById(id);
  }
  public void deleteProductBase(Integer id) {
    productBaseDao.deleteProductBase(id);
  }
  public void updateProductBase(Product_Base_Entity product_BASE_entity) {
    Integer idBaseProduct = product_BASE_entity.getID_BASE_PRODUCT();
    String NAME_PRODUCT = product_BASE_entity.getNAME_PRODUCT();
    Integer TOTAL_QUANTITY = product_BASE_entity.getTOTAL_QUANTITY();
    LocalDateTime DATE_RELEASE = product_BASE_entity.getDATE_RELEASE();
    String DES_PRODUCT = product_BASE_entity.getDES_PRODUCT();
    Integer VIEW_COUNT = product_BASE_entity.getVIEW_COUNT();
    Integer ID_CATEGORY = product_BASE_entity.getID_CATEGORY();
    Integer ID_BRAND = product_BASE_entity.getID_BRAND();
    productBaseDao.updateProductBase(idBaseProduct, NAME_PRODUCT, DES_PRODUCT, ID_CATEGORY, VIEW_COUNT, TOTAL_QUANTITY, ID_BRAND, DATE_RELEASE);
  }
  public void createProductBase(Product_Base_Entity product_BASE_entity) {
    Integer idBaseProduct = product_BASE_entity.getID_BASE_PRODUCT();
    String NAME_PRODUCT = product_BASE_entity.getNAME_PRODUCT();
    product_BASE_entity.setTOTAL_QUANTITY(0);
    LocalDateTime DATE_RELEASE = product_BASE_entity.getDATE_RELEASE();
    String DES_PRODUCT = product_BASE_entity.getDES_PRODUCT();
    Integer VIEW_COUNT = product_BASE_entity.getVIEW_COUNT();
    Integer ID_CATEGORY = product_BASE_entity.getID_CATEGORY();
    Integer ID_BRAND = product_BASE_entity.getID_BRAND();
    productBaseDao.createProductBase(product_BASE_entity);
  }
  public List < Product_Attribute_Entity > getProductAttributeCombinedCondition(
    Product_Attribute_Entity productAttribute,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset
  ) {
    Integer ID_ATTRIBUTE = productAttribute.getID_ATTRIBUTE();
    String NAME_ATTRIBUTE = productAttribute.getNAME_ATTRIBUTE();
    Integer ID_CATEGORY = productAttribute.getID_CATEGORY();
    String NAME_CATEGORY = productAttribute.getNAME_CATEGORY();
    return productAttributeDao.getFilteredProductAttribute(ID_ATTRIBUTE, NAME_ATTRIBUTE, ID_CATEGORY, NAME_CATEGORY, sortField, sortOrder, setOff, offset);
  }
  public Integer getCountProductAttribute(Product_Attribute_Entity productAttribute) {
    Integer ID_ATTRIBUTE = productAttribute.getID_ATTRIBUTE();
    String NAME_ATTRIBUTE = productAttribute.getNAME_ATTRIBUTE();
    Integer ID_CATEGORY = productAttribute.getID_CATEGORY();
    String NAME_CATEGORY = productAttribute.getNAME_CATEGORY();
    return productAttributeDao.getFilteredProductAttributeCount(ID_ATTRIBUTE, NAME_ATTRIBUTE, ID_CATEGORY, NAME_CATEGORY);
  }
  public void deleteProductAttribute(Integer id) {
    productAttributeDao.deleteProductAttributeById(id);
  }
  public void updateProductAttribute(Product_Attribute_Entity productAttribute) {
    Integer idAttribute = productAttribute.getID_ATTRIBUTE();
    String nameAttribute = productAttribute.getNAME_ATTRIBUTE();
    Integer idCategory = productAttribute.getID_CATEGORY();
    productAttributeDao.updateProductAttribute(idAttribute, idCategory, nameAttribute);
  }
  public void createProductAttribute(Product_Attribute_Entity productAttribute) {
    Integer idAttribute = productAttribute.getID_ATTRIBUTE();
    String nameAttribute = productAttribute.getNAME_ATTRIBUTE();
    Integer idCategory = productAttribute.getID_CATEGORY();
    productAttributeDao.createProductAttribute(productAttribute);
  }
  public List < Product_Attribute_Values_Entity > getProductAttributeValuesCombinedCondition(
    Product_Attribute_Values_Entity productAttributeValues,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset
  ) {
    Integer ID = productAttributeValues.getID();
    Integer ID_ATTRIBUTE = productAttributeValues.getID_ATTRIBUTE();
    String VALUE = productAttributeValues.getVALUE();
    Integer ID_BASE_PRODUCT = productAttributeValues.getID_BASE_PRODUCT();
    String NAME_ATTRIBUTE = productAttributeValues.getNAME_ATTRIBUTE();
    String NAME_PRODUCT = productAttributeValues.getNAME_PRODUCT();
    return productAttributeValuesDao.getFilteredProductAttributeValues(ID, VALUE, ID_ATTRIBUTE, NAME_ATTRIBUTE, ID_BASE_PRODUCT, NAME_PRODUCT, sortField, sortOrder, setOff, offset);
  }
  public Integer getCountProductAttributeValue(Product_Attribute_Values_Entity productAttributeValues) {
    Integer ID = productAttributeValues.getID();
    Integer ID_ATTRIBUTE = productAttributeValues.getID_ATTRIBUTE();
    String VALUE = productAttributeValues.getVALUE();
    Integer ID_BASE_PRODUCT = productAttributeValues.getID_BASE_PRODUCT();
    String NAME_ATTRIBUTE = productAttributeValues.getNAME_ATTRIBUTE();
    String NAME_PRODUCT = productAttributeValues.getNAME_PRODUCT();
    return productAttributeValuesDao.getFilteredProductAttributeValuesCount(ID, VALUE, ID_ATTRIBUTE, NAME_ATTRIBUTE, ID_BASE_PRODUCT, NAME_PRODUCT);
  }
  public void deleteProductAttributeValues(Integer id, Integer idAttribute, Integer idBaseProduct) {
    productAttributeValuesDao.deleteAttributeValues(id, idAttribute, idBaseProduct);
  }
  public void updateProductAttributeValues(Product_Attribute_Values_Entity productAttributeValues) {
    Integer idAttribute = productAttributeValues.getID_ATTRIBUTE();
    String values = productAttributeValues.getVALUE();
    Integer id = productAttributeValues.getID();
    Integer idBaseProduct = productAttributeValues.getID_BASE_PRODUCT();
    productAttributeValuesDao.updateProductAttributeValues(id, values, idBaseProduct, idAttribute);
  }
  public void createProductAttributeValues(Product_Attribute_Values_Entity productAttributeValues) {
    Integer id = productAttributeValues.getID();
    Integer idAttribute = productAttributeValues.getID_ATTRIBUTE();
    String values = productAttributeValues.getVALUE();
    Integer idBaseProduct = productAttributeValues.getID_BASE_PRODUCT();
    productAttributeValuesDao.createProductAttributeValues(productAttributeValues);
  }
}