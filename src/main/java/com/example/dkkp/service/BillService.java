package com.example.dkkp.service;
import com.example.dkkp.dao.BillDao;
import com.example.dkkp.dao.BillDetailDao;
import com.example.dkkp.model.*;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
public class BillService {
  private final BillDao billDao;
  private final BillDetailDao billDetailDao;
  private final EntityManager entityManager;
  public BillService(EntityManager entityManager) {
    this.billDao = new BillDao(entityManager);
    this.billDetailDao = new BillDetailDao(entityManager);
    this.entityManager = entityManager;
  }
  public List < Bill_Entity > getBillByCombinedCondition(
    Bill_Entity billEntity,
    String typeDate,
    String typePrice,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset
  ) throws Exception {
    LocalDateTime dateExport = billEntity.getDATE_EXP();
    String id = billEntity.getID_BILL();
    String phone = billEntity.getPHONE_BILL();
    String add = billEntity.getADD_BILL();
    String idUser = billEntity.getID_USER();
    Double totalPrice = billEntity.getTOTAL_PRICE();
    String EMAIL_ACC = billEntity.getEMAIL_ACC();
    EnumType.Status_Bill statusBill = billEntity.getBILL_STATUS();
    List < Bill_Entity > results = billDao.getFilteredBills(
      dateExport, typeDate, id, phone, idUser, EMAIL_ACC, statusBill, add, totalPrice, typePrice, sortField, sortOrder, setOff, offset);
    return results;
  }
  public Integer getCountBillByCombinedCondition(
    Bill_Entity billEntity,
    String typeDate,
    String typePrice
  ) {
    LocalDateTime dateExport = billEntity.getDATE_EXP();
    String id = billEntity.getID_BILL();
    String phone = billEntity.getPHONE_BILL();
    String add = billEntity.getADD_BILL();
    String idUser = billEntity.getID_USER();
    Double totalPrice = billEntity.getTOTAL_PRICE();
    String EMAIL_ACC = billEntity.getEMAIL_ACC();
    EnumType.Status_Bill statusBill = billEntity.getBILL_STATUS();
    return billDao.getFilteredBillCount(
      dateExport, typeDate, id, phone, idUser, EMAIL_ACC, statusBill, add, totalPrice, typePrice);
  }
  public List < Bill_Detail_Entity > getBillDetailByCombinedCondition(
    Bill_Detail_Entity billDetailEntity,
    String typeUPrice,
    String typeQuantity,
    String typePPrice,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offset
  ) {
    Integer idBillDetail = billDetailEntity.getID_BILL_DETAIL();
    String idBill = billDetailEntity.getID_BILL();
    Double totalPrice = billDetailEntity.getTOTAL_DETAIL_PRICE();
    Double unitPrice = billDetailEntity.getUNIT_PRICE();
    Boolean available = billDetailEntity.getAVAILABLE();
    Integer idFinalProduct = billDetailEntity.getID_FINAL_PRODUCT();
    Integer quantityBill = billDetailEntity.getQUANTITY_BILL();
    String NAME_FINAL_PRODUCT = billDetailEntity.getNAME_FINAL_PRODUCT();
    return billDetailDao.getFilteredBillDetails(
      idBillDetail, totalPrice, typePPrice, unitPrice, typeUPrice, idFinalProduct, NAME_FINAL_PRODUCT, quantityBill, typeQuantity, idBill, available, sortField, sortOrder, offset, setOff);
  }
  public Integer getCountBillDetailByCombinedCondition(
    Bill_Detail_Entity billDetailEntity,
    String typeUPrice,
    String typeQuantity,
    String typePPrice
  ) {
    Integer idBillDetail = billDetailEntity.getID_BILL_DETAIL();
    String idBill = billDetailEntity.getID_BILL();
    Double totalPrice = billDetailEntity.getTOTAL_DETAIL_PRICE();
    Double unitPrice = billDetailEntity.getUNIT_PRICE();
    Boolean available = billDetailEntity.getAVAILABLE();
    Integer idFinalProduct = billDetailEntity.getID_FINAL_PRODUCT();
    Integer quantityBill = billDetailEntity.getQUANTITY_BILL();
    String NAME_FINAL_PRODUCT = billDetailEntity.getNAME_FINAL_PRODUCT();
    return billDetailDao.getFilteredBillDetailsCount(
      idBillDetail, totalPrice, typePPrice, unitPrice, typeUPrice, idFinalProduct, NAME_FINAL_PRODUCT, quantityBill, typeQuantity, idBill, available);
  }
  public void changeBillStatus(String id, EnumType.Status_Bill statusBill) throws Exception {
    if (statusBill == EnumType.Status_Bill.CANC) {
      billDetailDao.cancelBillDetail(id);
      plusBillProduct(id);
    }
    if (billDao.getBillByID(id).getBILL_STATUS() == EnumType.Status_Bill.PEN && statusBill != EnumType.Status_Bill.CANC) {
      System.out.println("cong tien");
      minusProduct(id);
    }
    billDao.changeBillStatus(id, statusBill);
  }
  public void minusProduct(String id) throws Exception {
    if (billDao.getFilteredBills(null, null, id, null, null, null, null, null, null, null, null, null, null, null) != null) {
      List < Bill_Detail_Entity > listBillDetail = billDetailDao.getFilteredBillDetails(null, null, null, null, null, null, null, null, null, id, null, null, null, null, null);
      for (Bill_Detail_Entity billDetail: listBillDetail) {
        ProductFinalService productFinalService = new ProductFinalService(entityManager);
        Product_Final_Entity productE = productFinalService.getProductByID(billDetail.getID_FINAL_PRODUCT());
        if (productE == null) throw new Exception("Cant find product final to minus quantity");
        if (productE.getQUANTITY() < billDetail.getQUANTITY_BILL())
          throw new Exception("Quantity product in final storage is smaller to minus");
        Integer newQuantity = productE.getQUANTITY() - billDetail.getQUANTITY_BILL();
        productE.setQUANTITY(newQuantity);
        productFinalService.updateProductFinal(productE);
      }
    }
  }
  public void plusBillProduct(String id) {
    List < Bill_Detail_Entity > listBillDetail = billDetailDao.getFilteredBillDetails(null, null, null, null, null, null, null, null, null, id, null, null, null, null, null);
    for (Bill_Detail_Entity billDetail: listBillDetail) {
      ProductFinalService productFinalService = new ProductFinalService(entityManager);
      Product_Final_Entity productE = productFinalService.getProductByID(billDetail.getID_FINAL_PRODUCT());
      if (productE == null) throw new RuntimeException("Cant find product final to plus quantity");
      Integer newQuantity = productE.getQUANTITY() + billDetail.getQUANTITY_BILL();
      productE.setQUANTITY(newQuantity);
      productFinalService.updateProductFinal(productE);
    }
  }
  public void registerNewBill(Bill_Entity billEntity, String phone, String add) throws Exception {
    if (billEntity.getID_USER() != null) {
      String idUser = billEntity.getID_USER();
      UserService userService = new UserService(entityManager);
      User_Entity user = userService.getUsersByID(idUser);
      if (phone == null) {
        phone = SecurityFunction.decrypt(user.getPHONE_ACC());
      }
      if (add == null) {
        add = SecurityFunction.decrypt(user.getADDRESS());
      }
    }
    billEntity.setADD_BILL(add);
    billEntity.setPHONE_BILL(phone);
    billDao.createBill(billEntity);
  }
  public void registerNewBillDetail(List < Bill_Detail_Entity > listBillDetail) {
    if (listBillDetail != null) {
      System.out.println();
      billDetailDao.createBillDetail(listBillDetail);
      Double sumPrice = 0.0;
      String id = listBillDetail.getFirst().getID_BILL();
      if (id == null) throw new RuntimeException("Error cant find bill general to create bill detail");
      for (Bill_Detail_Entity billDetail: listBillDetail) {
        sumPrice += billDetail.getTOTAL_DETAIL_PRICE();
      }
      billDao.addSumPrice(id, sumPrice);
    }
  }
}