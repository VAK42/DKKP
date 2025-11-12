package com.example.dkkp.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "category")
public class Category_Entity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"ID_CATEGORY\"")
  private Integer ID_CATEGORY;
  @Column(name = "\"NAME_CATEGORY\"")
  private String NAME_CATEGORY;
  public Integer getID_CATEGORY() {
    return ID_CATEGORY;
  }
  public void setID_CATEGORY(Integer ID_CATEGORY) {
    this.ID_CATEGORY = ID_CATEGORY;
  }
  public String getNAME_CATEGORY() {
    return NAME_CATEGORY;
  }
  public void setNAME_CATEGORY(String NAME_CATEGORY) {
    this.NAME_CATEGORY = NAME_CATEGORY;
  }
  public Category_Entity(String NAME_CATEGORY) {
    this.NAME_CATEGORY = NAME_CATEGORY;
  }
  public Category_Entity(Integer ID_CATEGORY, String NAME_CATEGORY) {
    this.ID_CATEGORY = ID_CATEGORY;
    this.NAME_CATEGORY = NAME_CATEGORY;
  }
  public Category_Entity() {}
  @Override
  public String toString() {
    return NAME_CATEGORY;
  }
}