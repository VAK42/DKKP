package com.example.dkkp.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "product_base")
public class Product_Base_Entity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"ID_BASE_PRODUCT\"")
  private Integer ID_BASE_PRODUCT;
  @Column(name = "\"NAME_PRODUCT\"")
  private String NAME_PRODUCT;
  @Column(name = "\"TOTAL_QUANTITY\"")
  private Integer TOTAL_QUANTITY;
  @Column(name = "\"DATE_RELEASE\"")
  private LocalDateTime DATE_RELEASE;
  @Column(name = "\"DES_PRODUCT\"")
  private String DES_PRODUCT;
  @Column(name = "\"VIEW_COUNT\"")
  private Integer VIEW_COUNT;
  @Column(name = "\"ID_CATEGORY\"")
  private Integer ID_CATEGORY;
  @Column(name = "\"ID_BRAND\"")
  private Integer ID_BRAND;
  @ManyToOne
  @JoinColumn(name = "\"ID_CATEGORY\"", referencedColumnName = "\"ID_CATEGORY\"", insertable = false, updatable = false)
  private Category_Entity category;
  @ManyToOne
  @JoinColumn(name = "\"ID_BRAND\"", referencedColumnName = "\"ID_BRAND\"", insertable = false, updatable = false)
  private Brand_Entity brand;
  @Transient
  private String NAME_BRAND;
  @Transient
  private String NAME_CATEGORY;
  public Integer getID_BASE_PRODUCT() {
    return ID_BASE_PRODUCT;
  }
  public void setID_BASE_PRODUCT(Integer ID_BASE_PRODUCT) {
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
  }
  public String getNAME_PRODUCT() {
    return NAME_PRODUCT;
  }
  public void setNAME_PRODUCT(String NAME_PRODUCT) {
    this.NAME_PRODUCT = NAME_PRODUCT;
  }
  public Integer getTOTAL_QUANTITY() {
    return TOTAL_QUANTITY;
  }
  public void setTOTAL_QUANTITY(Integer TOTAL_QUANTITY) {
    this.TOTAL_QUANTITY = TOTAL_QUANTITY;
  }
  public LocalDateTime getDATE_RELEASE() {
    return DATE_RELEASE;
  }
  public void setDATE_RELEASE(LocalDateTime DATE_RELEASE) {
    this.DATE_RELEASE = DATE_RELEASE;
  }
  public String getDES_PRODUCT() {
    return DES_PRODUCT;
  }
  public void setDES_PRODUCT(String DES_PRODUCT) {
    this.DES_PRODUCT = DES_PRODUCT;
  }
  public Integer getVIEW_COUNT() {
    return VIEW_COUNT;
  }
  public void setVIEW_COUNT(Integer VIEW_COUNT) {
    this.VIEW_COUNT = VIEW_COUNT;
  }
  public Integer getID_CATEGORY() {
    return ID_CATEGORY;
  }
  public void setID_CATEGORY(Integer ID_CATEGORY) {
    this.ID_CATEGORY = ID_CATEGORY;
  }
  public Integer getID_BRAND() {
    return ID_BRAND;
  }
  public void setID_BRAND(Integer ID_BRAND) {
    this.ID_BRAND = ID_BRAND;
  }
  public String getNAME_BRAND() {
    return NAME_BRAND;
  }
  public void setNAME_BRAND(String NAME_BRAND) {
    this.NAME_BRAND = NAME_BRAND;
  }
  public String getNAME_CATEGORY() {
    return NAME_CATEGORY;
  }
  public void setNAME_CATEGORY(String NAME_CATEGORY) {
    this.NAME_CATEGORY = NAME_CATEGORY;
  }
  public Product_Base_Entity(Integer ID_BASE_PRODUCT, String NAME_PRODUCT, Integer TOTAL_QUANTITY, LocalDateTime DATE_RELEASE, String DES_PRODUCT, Integer VIEW_COUNT, Integer ID_CATEGORY, Integer ID_BRAND, String NAME_BRAND, String NAME_CATEGORY) {
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
    this.NAME_PRODUCT = NAME_PRODUCT;
    this.TOTAL_QUANTITY = TOTAL_QUANTITY;
    this.DATE_RELEASE = DATE_RELEASE;
    this.DES_PRODUCT = DES_PRODUCT;
    this.VIEW_COUNT = VIEW_COUNT;
    this.ID_CATEGORY = ID_CATEGORY;
    this.ID_BRAND = ID_BRAND;
    this.NAME_BRAND = NAME_BRAND;
    this.NAME_CATEGORY = NAME_CATEGORY;
  }
  public Product_Base_Entity(Integer ID_BASE_PRODUCT, String NAME_PRODUCT, Integer TOTAL_QUANTITY, LocalDateTime DATE_RELEASE, String DES_PRODUCT, Integer VIEW_COUNT, Integer ID_CATEGORY, Integer ID_BRAND) {
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
    this.NAME_PRODUCT = NAME_PRODUCT;
    this.TOTAL_QUANTITY = TOTAL_QUANTITY;
    this.DATE_RELEASE = DATE_RELEASE;
    this.DES_PRODUCT = DES_PRODUCT;
    this.VIEW_COUNT = VIEW_COUNT;
    this.ID_CATEGORY = ID_CATEGORY;
    this.ID_BRAND = ID_BRAND;
  }
  public Product_Base_Entity() {}
  @Override
  public String toString() {
    return "ID: " + ID_BASE_PRODUCT + " NAME " + NAME_PRODUCT;
  }
}