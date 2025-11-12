package com.example.dkkp.service;
import com.example.dkkp.dao.ImportDao;
import com.example.dkkp.dao.ImportDetailDao;
import com.example.dkkp.model.*;
import jakarta.persistence.EntityManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
public class ImportService {
  private final ImportDao importDao;
  private final ImportDetailDao importDetailDao;
  private final EntityManager entityManager;
  public ImportService(EntityManager entityManager) {
    this.importDao = new ImportDao(entityManager);
    this.importDetailDao = new ImportDetailDao(entityManager);
    this.entityManager = entityManager;
  }
  public List < Import_Entity > getImportByCombinedCondition(
    Import_Entity import_entity,
    String typeDate,
    String typePrice,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset
  ) {
    LocalDateTime dateImport = import_entity.getDATE_IMP();
    String id = import_entity.getID_IMP();
    Boolean status = import_entity.getIS_AVAILABLE();
    String idReplace = import_entity.getID_REPLACE();
    Double totalPrice = import_entity.getTOTAL_PRICE();
    return importDao.getFilteredImports(
      dateImport, typeDate, id, status, idReplace, totalPrice, typePrice, sortField, sortOrder, offset, setOff
    );
  }
  public Integer getCountImportByCombinedCondition(
    Import_Entity import_entity,
    String typeDate,
    String typePrice
  ) {
    LocalDateTime dateImport = import_entity.getDATE_IMP();
    String id = import_entity.getID_IMP();
    Boolean status = import_entity.getIS_AVAILABLE();
    String idReplace = import_entity.getID_REPLACE();
    Double totalPrice = import_entity.getTOTAL_PRICE();
    return importDao.getFilteredImportsCount(
      dateImport, typeDate, id, status, idReplace, totalPrice, typePrice);
  }
  public List < Import_Detail_Entity > getImportDetailByCombinedCondition(
    Import_Detail_Entity importQuery,
    String typeUPrice,
    String typeQuantity,
    String typePPrice,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset
  ) {
    Integer id = importQuery.getID_IMPD();
    String idImport = importQuery.getID_IMPORT();
    Integer idFinalProduct = importQuery.getID_FINAL_PRODUCT();
    Integer idBaseProduct = importQuery.getID_BASE_PRODUCT();
    Boolean isAvailable = importQuery.getIS_AVAILABLE();
    Integer quantity = importQuery.getQUANTITY();
    Double totalPrice = importQuery.getTOTAL_PRICE();
    Double unitPrice = importQuery.getUNIT_PRICE();
    String NAME_FINAL_PRODUCT = importQuery.getNAME_PRODUCT_FINAL();
    String NAME_BASE_PRODUCT = importQuery.getNAME_PRODUCT_BASE();
    return importDetailDao.getFilteredImportDetails(
      id, idImport, idFinalProduct, NAME_FINAL_PRODUCT, isAvailable, idBaseProduct, NAME_BASE_PRODUCT, quantity, typeQuantity, typeUPrice, typePPrice, unitPrice, totalPrice, sortField, sortOrder, setOff, offset
    );
  }
  public Integer getCountImportDetailByCombinedConditionCount(
    Import_Detail_Entity importQuery,
    String typeUPrice,
    String typeQuantity,
    String typePPrice
  ) {
    Integer id = importQuery.getID_IMPD();
    String idImport = importQuery.getID_IMPORT();
    Integer idFinalProduct = importQuery.getID_FINAL_PRODUCT();
    Integer idBaseProduct = importQuery.getID_BASE_PRODUCT();
    Boolean isAvailable = importQuery.getIS_AVAILABLE();
    Integer quantity = importQuery.getQUANTITY();
    Double totalPrice = importQuery.getTOTAL_PRICE();
    Double unitPrice = importQuery.getUNIT_PRICE();
    String NAME_FINAL_PRODUCT = importQuery.getNAME_PRODUCT_FINAL();
    String NAME_BASE_PRODUCT = importQuery.getNAME_PRODUCT_BASE();
    return importDetailDao.getFilteredImportDetailsCount(
      id, idImport, idFinalProduct, NAME_FINAL_PRODUCT, isAvailable, idBaseProduct, NAME_BASE_PRODUCT, quantity, typeQuantity, typeUPrice, typePPrice, unitPrice, totalPrice
    );
  }
  public void deleteImportAndDetail(String idParent) {
    if (idParent != null) {
      boolean isDeleted = importDetailDao.deleteImportDetailByIdImport(idParent);
      if (!isDeleted) {
        throw new RuntimeException("Failed to delete import detail.");
      }
      importDao.deleteImport(idParent);
      minusImportProduct(idParent);
    }
    throw new RuntimeException("Error with sort");
  }
  public void registerNewImport(Import_Entity importEntity) throws
  SQLException {
    importDao.createImport(importEntity);
  }
  public void registerNewImportDetail(List < Import_Detail_Entity > listImportDetail) throws SQLException {
    String id = listImportDetail.getFirst().getID_IMPORT();
    if (importDao.checkImport(id)) {
      System.out.println("vao roi");
      importDetailDao.createImportDetail(listImportDetail);
      Double sumPrice = 0.0;
      for (Import_Detail_Entity importDetail: listImportDetail) {
        sumPrice += importDetail.getTOTAL_PRICE();
      }
      plusImportProduct(id);
      importDao.addSumPrice(id, sumPrice);
      return;
    }
    throw new RuntimeException("Can not find id import general to add import detail");
  }
  private void plusImportProduct(String id) {
    if (id != null) {
      if (importDao.getFilteredImports(null, null, id, null, null, null, null, null, null, null, null) != null) {
        List < Import_Detail_Entity > listImportDetail = importDetailDao.getFilteredImportDetails(null, id, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        for (Import_Detail_Entity importDetail: listImportDetail) {
          Integer quantity = importDetail.getQUANTITY();
          if (importDetail.getID_BASE_PRODUCT() != null) {
            Integer idSp = importDetail.getID_BASE_PRODUCT();
            ProductBaseService productBaseService = new ProductBaseService(entityManager);
            Product_Base_Entity productE = productBaseService.getProductBaseByID(idSp);
            if (productE == null) throw new RuntimeException("Error cant find product base to plus quantity in plus product form import process");
            productE.setTOTAL_QUANTITY(productE.getTOTAL_QUANTITY() + quantity);
            productBaseService.updateProductBase(productE);
          } else {
            Integer idSp = importDetail.getID_FINAL_PRODUCT();
            ProductFinalService productFinalService = new ProductFinalService(entityManager);
            Product_Final_Entity productE = productFinalService.getProductByID(idSp);
            if (productE == null) throw new RuntimeException("Error cant find product final to plus quantity in plus product form import process");
            productE.setQUANTITY(productE.getQUANTITY() + quantity);
            productFinalService.updateProductFinal(productE);
          }
        }
        return;
      }
    }
    throw new RuntimeException("ID Import general to plus product from import is null");
  }
  private void minusImportProduct(String id) {
    if (id != null) {
      if (importDao.getFilteredImports(null, null, id, null, null, null, null, null, null, null, null) != null) {
        List < Import_Detail_Entity > listImportDetail = importDetailDao.getFilteredImportDetails(null, id, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        for (Import_Detail_Entity importDetail: listImportDetail) {
          Integer quantity = importDetail.getQUANTITY();
          if (importDetail.getID_BASE_PRODUCT() != null) {
            Integer idSp = importDetail.getID_BASE_PRODUCT();
            ProductBaseService productBaseService = new ProductBaseService(entityManager);
            Product_Base_Entity productE = productBaseService.getProductBaseByID(idSp);
            if (productE == null) throw new RuntimeException("Error cant find product base to plus quantity in plus product form import process");
            if (quantity > productE.getTOTAL_QUANTITY()) {
              throw new RuntimeException("Product base in storage is smaller to minus");
            }
            productE.setTOTAL_QUANTITY(productE.getTOTAL_QUANTITY() - quantity);
            productBaseService.updateProductBase(productE);
          } else {
            Integer idSp = importDetail.getID_FINAL_PRODUCT();
            ProductFinalService productFinalService = new ProductFinalService(entityManager);
            Product_Final_Entity productE = productFinalService.getProductByID(idSp);
            if (productE == null) throw new RuntimeException("Error cant find product final to plus quantity in plus product form import process");
            if (quantity > productE.getQUANTITY()) {
              throw new RuntimeException("Product base in storage is smaller to minus");
            }
            productE.setQUANTITY(productE.getQUANTITY() - quantity);
            productFinalService.updateProductFinal(productE);
          }
        }
        return;
      }
    }
    throw new RuntimeException("ID Import general to plus product from import is null");
  }
}