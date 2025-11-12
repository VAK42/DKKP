package com.example.dkkp.model;
import jakarta.persistence.*;
@Entity
@Table(name = "brand")
public class Brand_Entity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"ID_BRAND\"")
  private Integer ID_BRAND;
  @Column(name = "\"NAME_BRAND\"")
  private String NAME_BRAND;
  @Column(name = "\"DETAIL\"")
  private String DETAIL;
  public Integer getID_BRAND() {
    return ID_BRAND;
  }
  public void setID_BRAND(Integer ID_CATEGORY) {
    this.ID_BRAND = ID_CATEGORY;
  }
  public String getNAME_BRAND() {
    return NAME_BRAND;
  }
  public void setNAME_BRAND(String NAME_BRAND) {
    this.NAME_BRAND = NAME_BRAND;
  }
  public String getDETAIL() {
    return DETAIL;
  }
  public void setDETAIL(String DETAIL) {
    this.DETAIL = DETAIL;
  }
  public Brand_Entity(Integer ID_BRAND, String NAME_BRAND, String DETAIL) {
    this.ID_BRAND = ID_BRAND;
    this.NAME_BRAND = NAME_BRAND;
    this.DETAIL = DETAIL;
  }
  public Brand_Entity(String NAME_BRAND, String DETAIL) {
    this.NAME_BRAND = NAME_BRAND;
    this.DETAIL = DETAIL;
  }
  public Brand_Entity() {}
  @Override
  public String toString() {
    return "Id: " + ID_BRAND + " - Name: " + NAME_BRAND;
  }
}