package com.example.dkkp.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "product_final")
public class Product_Final_Entity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"ID_FINAL_PRODUCT\"")
  private Integer ID_SP;
  @Column(name = "\"ID_BASE_PRODUCT\"")
  private Integer ID_BASE_PRODUCT;
  @Column(name = "\"NAME_PRODUCT\"")
  private String NAME_PRODUCT;
  @Column(name = "\"QUANTITY\"")
  private Integer QUANTITY;
  @Column(name = "\"DES_PRODUCT\"")
  private String DES_PRODUCT;
  @Column(name = "\"PRICE_SP\"")
  private Double PRICE_SP;
  @Column(name = "\"DISCOUNT\"")
  private Double DISCOUNT;
  @Column(name = "\"IMAGE_SP\"")
  private String IMAGE_SP;
  @ManyToOne
  @JoinColumn(name = "\"ID_BASE_PRODUCT\"", referencedColumnName = "\"ID_BASE_PRODUCT\"", insertable = false, updatable = false)
  private Product_Base_Entity product_base;
  @Transient
  private String NAME_PRODUCT_BASE;
  @Transient
  private Double sum_total_price;
  @Transient
  private Integer sum_quantity;
  public Double getSum_total_price() {
    return sum_total_price;
  }
  public void setSum_total_price(Double sum_total_price) {
    this.sum_total_price = sum_total_price;
  }
  public Integer getSum_quantity() {
    return sum_quantity;
  }
  public void setSum_quantity(Integer sum_quantity) {
    this.sum_quantity = sum_quantity;
  }
  public String getNAME_PRODUCT_BASE() {
    return NAME_PRODUCT_BASE;
  }
  public void setNAME_PRODUCT_BASE(String NAME_PRODUCT_BASE) {
    this.NAME_PRODUCT_BASE = NAME_PRODUCT_BASE;
  }
  public Integer getID_SP() {
    return ID_SP;
  }
  public void setID_SP(Integer ID_SP) {
    this.ID_SP = ID_SP;
  }
  public String getNAME_PRODUCT() {
    return NAME_PRODUCT;
  }
  public void setNAME_PRODUCT(String NAME_PRODUCT) {
    this.NAME_PRODUCT = NAME_PRODUCT;
  }
  public Integer getQUANTITY() {
    return QUANTITY;
  }
  public void setQUANTITY(Integer QUANTITY) {
    this.QUANTITY = QUANTITY;
  }
  public Integer getID_BASE_PRODUCT() {
    return ID_BASE_PRODUCT;
  }
  public void setID_BASE_PRODUCT(Integer ID_BASE_PRODUCT) {
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
  }
  public String getDES_PRODUCT() {
    return DES_PRODUCT;
  }
  public void setDES_PRODUCT(String DES_PRODUCT) {
    this.DES_PRODUCT = DES_PRODUCT;
  }
  public Double getPRICE_SP() {
    return PRICE_SP;
  }
  public void setPRICE_SP(Double PRICE_SP) {
    this.PRICE_SP = PRICE_SP;
  }
  public Double getDISCOUNT() {
    return DISCOUNT;
  }
  public void setDISCOUNT(Double DISCOUNT) {
    this.DISCOUNT = DISCOUNT;
  }
  public String getIMAGE_SP() {
    return IMAGE_SP;
  }
  public void setIMAGE_SP(String IMAGE_SP) {
    this.IMAGE_SP = IMAGE_SP;
  }
  public Product_Final_Entity(Integer ID_SP, Integer ID_BASE_PRODUCT, String NAME_PRODUCT, Integer QUANTITY, Double PRICE_SP, Double DISCOUNT, String IMAGE_SP, String NAME_PRODUCT_BASE, String DES_PRODUCT) {
    this.ID_SP = ID_SP;
    this.NAME_PRODUCT = NAME_PRODUCT;
    this.QUANTITY = QUANTITY;
    this.PRICE_SP = PRICE_SP;
    this.DISCOUNT = DISCOUNT;
    this.DES_PRODUCT = DES_PRODUCT;
    this.IMAGE_SP = IMAGE_SP;
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
    this.NAME_PRODUCT_BASE = NAME_PRODUCT_BASE;
  }
  public Product_Final_Entity(Integer ID_SP, Integer ID_BASE_PRODUCT, String NAME_PRODUCT, Integer QUANTITY, String DES_PRODUCT, Double PRICE_SP, Double DISCOUNT, String IMAGE_SP) {
    this.ID_SP = ID_SP;
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
    this.NAME_PRODUCT = NAME_PRODUCT;
    this.QUANTITY = QUANTITY;
    this.DES_PRODUCT = DES_PRODUCT;
    this.PRICE_SP = PRICE_SP;
    this.DISCOUNT = DISCOUNT;
    this.IMAGE_SP = IMAGE_SP;
  }
  public Product_Final_Entity() {}
  @Override
  public String toString() {
    return "ID " + ID_SP + " - NAME " + NAME_PRODUCT;
  }
  public Product_Final_Entity(Integer ID_SP, Integer ID_BASE_PRODUCT, String NAME_PRODUCT, Integer QUANTITY, Double PRICE_SP, Double DISCOUNT, String IMAGE_SP, String NAME_PRODUCT_BASE, String DES_PRODUCT, Double sum_total_price, Integer sum_quantity) {
    this.ID_SP = ID_SP;
    this.NAME_PRODUCT = NAME_PRODUCT;
    this.QUANTITY = QUANTITY;
    this.PRICE_SP = PRICE_SP;
    this.DISCOUNT = DISCOUNT;
    this.IMAGE_SP = IMAGE_SP;
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
    this.NAME_PRODUCT_BASE = NAME_PRODUCT_BASE;
    this.DES_PRODUCT = DES_PRODUCT;
    this.sum_total_price = sum_total_price;
    this.sum_quantity = sum_quantity;
  }
  public Product_Final_Entity(Integer ID_SP, String NAME_PRODUCT, Double sum_total_price, Integer sum_quantity) {
    this.ID_SP = ID_SP;
    this.NAME_PRODUCT = NAME_PRODUCT;
    this.sum_total_price = sum_total_price;
    this.sum_quantity = sum_quantity;
  }
}