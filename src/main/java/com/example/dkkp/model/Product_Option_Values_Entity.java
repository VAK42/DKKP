package com.example.dkkp.model;
import jakarta.persistence.*;
@Entity
@Table(name = "product_option_values")
public class Product_Option_Values_Entity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"ID\"")
  private Integer ID;
  @Column(name = "\"ID_OPTION\"")
  private Integer ID_OPTION;
  @Column(name = "\"VALUE\"")
  private String VALUE;
  @Column(name = "\"ID_FINAL_PRODUCT\"")
  private Integer ID_FINAL_PRODUCT;
  @ManyToOne
  @JoinColumn(name = "\"ID_FINAL_PRODUCT\"", referencedColumnName = "\"ID_FINAL_PRODUCT\"", insertable = false, updatable = false)
  private Product_Final_Entity product_final;
  @ManyToOne
  @JoinColumn(name = "\"ID_OPTION\"", referencedColumnName = "\"ID_OPTION\"", insertable = false, updatable = false)
  private Product_Option_Entity product_options;
  @Transient
  private String NAME_OPTION;
  @Transient
  private String NAME_PRODUCT;
  public String getNAME_PRODUCT() {
    return NAME_PRODUCT;
  }
  public void setNAME_PRODUCT(String NAME_PRODUCT) {
    this.NAME_PRODUCT = NAME_PRODUCT;
  }
  public String getNAME_OPTION() {
    return NAME_OPTION;
  }
  public Product_Option_Values_Entity(Integer ID, Integer ID_OPTION, String VALUE, Integer ID_FINAL_PRODUCT, String NAMEOPTION, String NAME_PRODUCT) {
    this.ID = ID;
    this.ID_OPTION = ID_OPTION;
    this.NAME_OPTION = NAMEOPTION;
    this.VALUE = VALUE;
    this.ID_FINAL_PRODUCT = ID_FINAL_PRODUCT;
    this.NAME_PRODUCT = NAME_PRODUCT;
  }
  public void setNAME_OPTION(String NAME_OPTION) {
    this.NAME_OPTION = NAME_OPTION;
  }
  public Integer getID() {
    return ID;
  }
  public void setID(Integer ID) {
    this.ID = ID;
  }
  public Integer getID_OPTION() {
    return ID_OPTION;
  }
  public void setID_OPTION(Integer ID_OPTION) {
    this.ID_OPTION = ID_OPTION;
  }
  public String getVALUE() {
    return VALUE;
  }
  public void setVALUE(String VALUE) {
    this.VALUE = VALUE;
  }
  public Product_Option_Values_Entity() {}
  public Integer getID_FINAL_PRODUCT() {
    return ID_FINAL_PRODUCT;
  }
  public void setID_FINAL_PRODUCT(Integer ID_FINAL_PRODUCT) {
    this.ID_FINAL_PRODUCT = ID_FINAL_PRODUCT;
  }
  public Product_Option_Values_Entity(Integer ID, Integer ID_OPTION, String VALUE, Integer ID_FINAL_PRODUCT) {
    this.ID = ID;
    this.ID_OPTION = ID_OPTION;
    this.VALUE = VALUE;
    this.ID_FINAL_PRODUCT = ID_FINAL_PRODUCT;
  }
}