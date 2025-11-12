package com.example.dkkp.model;
import jakarta.persistence.*;
@Entity
@Table(name = "import_detail")
public class Import_Detail_Entity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"ID_IMPD\"")
  private Integer ID_IMPD;
  @Column(name = "\"ID_IMPORT\"")
  private String ID_IMPORT;
  @Column(name = "\"IS_AVAILABLE\"")
  private Boolean IS_AVAILABLE;
  @Column(name = "\"ID_BASE_PRODUCT\"")
  private Integer ID_BASE_PRODUCT;
  @Column(name = "\"ID_FINAL_PRODUCT\"")
  private Integer ID_FINAL_PRODUCT;
  @Column(name = "\"QUANTITY\"")
  private Integer QUANTITY;
  @Column(name = "\"UNIT_PRICE\"")
  private Double UNIT_PRICE;
  @Column(name = "\"TOTAL_PRICE\"")
  private Double TOTAL_PRICE;
  @Column(name = "\"DESCRIPTION\"")
  private String DESCRIPTION;
  @Transient
  private String NAME_PRODUCT_BASE;
  @Transient
  private String NAME_PRODUCT_FINAL;
  @ManyToOne
  @JoinColumn(name = "\"ID_IMPORT\"", referencedColumnName = "\"ID_IMP\"", insertable = false, updatable = false)
  private Import_Entity import_Entity;
  @ManyToOne
  @JoinColumn(name = "\"ID_BASE_PRODUCT\"", referencedColumnName = "\"ID_BASE_PRODUCT\"", insertable = false, updatable = false)
  private Product_Base_Entity product_Base_Entity;
  @ManyToOne
  @JoinColumn(name = "\"ID_FINAL_PRODUCT\"", referencedColumnName = "\"ID_FINAL_PRODUCT\"", insertable = false, updatable = false)
  private Product_Final_Entity product_Final_Entity;
  public Import_Detail_Entity(Integer ID_IMPD, String ID_IMPORT, Boolean IS_AVAILABLE, Integer ID_BASE_PRODUCT, Integer ID_FINAL_PRODUCT, Integer QUANTITY, Double UNIT_PRICE, String DESCRIPTION, String NAME_PRODUCT_BASE, String NAME_PRODUCT_FINAL) {
    this.ID_IMPD = ID_IMPD;
    this.ID_IMPORT = ID_IMPORT;
    this.IS_AVAILABLE = IS_AVAILABLE;
    this.ID_FINAL_PRODUCT = ID_FINAL_PRODUCT;
    this.NAME_PRODUCT_FINAL = NAME_PRODUCT_FINAL;
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
    this.NAME_PRODUCT_BASE = NAME_PRODUCT_BASE;
    this.QUANTITY = QUANTITY;
    this.UNIT_PRICE = UNIT_PRICE;
    this.TOTAL_PRICE = UNIT_PRICE * QUANTITY;
    this.DESCRIPTION = DESCRIPTION;
  }
  public Boolean isIS_AVAILABLE() {
    return IS_AVAILABLE;
  }
  public String getNAME_PRODUCT_BASE() {
    return NAME_PRODUCT_BASE;
  }
  public void setNAME_PRODUCT_BASE(String NAME_PRODUCT_BASE) {
    this.NAME_PRODUCT_BASE = NAME_PRODUCT_BASE;
  }
  public String getNAME_PRODUCT_FINAL() {
    return NAME_PRODUCT_FINAL;
  }
  public void setNAME_PRODUCT_FINAL(String NAME_PRODUCT_FINAL) {
    this.NAME_PRODUCT_FINAL = NAME_PRODUCT_FINAL;
  }
  public Integer getID_IMPD() {
    return ID_IMPD;
  }
  public void setID_IMPD(Integer ID_IMPD) {
    this.ID_IMPD = ID_IMPD;
  }
  public String getID_IMPORT() {
    return ID_IMPORT;
  }
  public void setID_IMPORT(String ID_IMPORT) {
    this.ID_IMPORT = ID_IMPORT;
  }
  public Boolean getIS_AVAILABLE() {
    return IS_AVAILABLE;
  }
  public void setIS_AVAILABLE(Boolean IS_AVAILABLE) {
    this.IS_AVAILABLE = IS_AVAILABLE;
  }
  public Integer getID_BASE_PRODUCT() {
    return ID_BASE_PRODUCT;
  }
  public void setID_BASE_PRODUCT(Integer ID_BASE_PRODUCT) {
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
  }
  public Integer getID_FINAL_PRODUCT() {
    return ID_FINAL_PRODUCT;
  }
  public void setID_FINAL_PRODUCT(Integer ID_FINAL_PRODUCT) {
    this.ID_FINAL_PRODUCT = ID_FINAL_PRODUCT;
  }
  public Integer getQUANTITY() {
    return QUANTITY;
  }
  public void setQUANTITY(Integer QUANTITY) {
    this.QUANTITY = QUANTITY;
  }
  public Double getUNIT_PRICE() {
    return UNIT_PRICE;
  }
  public void setUNIT_PRICE(Double UNIT_PRICE) {
    this.UNIT_PRICE = UNIT_PRICE;
  }
  public Double getTOTAL_PRICE() {
    return TOTAL_PRICE;
  }
  public void setTOTAL_PRICE(Double TOTAL_PRICE) {
    this.TOTAL_PRICE = TOTAL_PRICE;
  }
  public String getDESCRIPTION() {
    return DESCRIPTION;
  }
  public void setDESCRIPTION(String DESCRIPTION) {
    this.DESCRIPTION = DESCRIPTION;
  }
  public Import_Detail_Entity(Integer ID_IMPD, String ID_IMPORT, Boolean IS_AVAILABLE, Integer ID_BASE_PRODUCT, Integer ID_FINAL_PRODUCT, Integer QUANTITY, Double UNIT_PRICE, Double TOTAL_PRICE, String DESCRIPTION) {
    this.ID_IMPD = ID_IMPD;
    this.ID_IMPORT = ID_IMPORT;
    this.IS_AVAILABLE = IS_AVAILABLE;
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
    this.ID_FINAL_PRODUCT = ID_FINAL_PRODUCT;
    this.QUANTITY = QUANTITY;
    this.UNIT_PRICE = UNIT_PRICE;
    this.TOTAL_PRICE = TOTAL_PRICE;
    this.DESCRIPTION = DESCRIPTION;
  }
  public Import_Detail_Entity(Integer ID_IMPD, String ID_IMPORT, Boolean IS_AVAILABLE, Integer ID_BASE_PRODUCT, Integer ID_FINAL_PRODUCT, Integer QUANTITY, Double UNIT_PRICE, String DESCRIPTION) {
    this.ID_IMPD = ID_IMPD;
    this.ID_IMPORT = ID_IMPORT;
    this.IS_AVAILABLE = IS_AVAILABLE;
    this.ID_BASE_PRODUCT = ID_BASE_PRODUCT;
    this.ID_FINAL_PRODUCT = ID_FINAL_PRODUCT;
    this.QUANTITY = QUANTITY;
    this.UNIT_PRICE = UNIT_PRICE;
    this.TOTAL_PRICE = UNIT_PRICE * QUANTITY;
    this.DESCRIPTION = DESCRIPTION;
  }
  public Import_Detail_Entity() {}
}