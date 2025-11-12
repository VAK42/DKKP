package com.example.dkkp.model;
import com.example.dkkp.service.SecurityFunction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
@Entity
@Table(name = "uzer")
public class User_Entity {
  @Id
  @Column(name = "\"ID_USER\"")
  private String ID_USER;
  @Column(name = "\"EMAIL_ACC\"")
  private String EMAIL_ACC;
  @Column(name = "\"PHONE_ACC\"")
  private String PHONE_ACC;
  @Column(name = "\"PASSWORD_ACC\"")
  private String PASSWORD_ACC;
  @Column(name = "\"ROLE_ACC\"")
  private String ROLE_ACC;
  @Column(name = "\"NAME_USER\"")
  private String NAME_USER;
  @Column(name = "\"DATE_JOIN\"")
  private LocalDateTime DATE_JOIN;
  @Column(name = "\"SALT\"")
  private String SALT;
  @Column(name = "\"ADDRESS\"")
  private String ADDRESS;
  public String getSALT() {
    return SALT;
  }
  public void setSALT(String SALT) {
    this.SALT = SALT;
  }
  public String getADDRESS() {
    return ADDRESS;
  }
  public void setADDRESS(String ADDRESS) {
    this.ADDRESS = ADDRESS;
  }
  public User_Entity(String ID_USER, String EMAIL_ACC, String PHONE_ACC, String PASSWORD_ACC, String ROLE_ACC, String NAME_USER, String ADDRESS) {
    this.ID_USER = ID_USER;
    this.EMAIL_ACC = EMAIL_ACC;
    this.PHONE_ACC = PHONE_ACC;
    this.PASSWORD_ACC = PASSWORD_ACC;
    this.ROLE_ACC = ROLE_ACC;
    this.NAME_USER = NAME_USER;
    this.ADDRESS = ADDRESS;
  }
  public User_Entity(String ADDRESS, String NAME_USER, String ROLE_ACC, String PASSWORD_ACC, String PHONE_ACC, String EMAIL_ACC) {
    this.ADDRESS = ADDRESS;
    this.NAME_USER = NAME_USER;
    this.ROLE_ACC = ROLE_ACC;
    this.PASSWORD_ACC = PASSWORD_ACC;
    this.PHONE_ACC = PHONE_ACC;
    this.EMAIL_ACC = EMAIL_ACC;
  }
  public String getID_USER() {
    return ID_USER;
  }
  public void setID_USER(String ID_USER) {
    this.ID_USER = ID_USER;
  }
  public String getEMAIL_ACC() {
    return EMAIL_ACC;
  }
  public void setEMAIL_ACC(String EMAIL_ACC) {
    this.EMAIL_ACC = EMAIL_ACC;
  }
  public String getPHONE_ACC() {
    return PHONE_ACC;
  }
  public void setPHONE_ACC(String PHONE_ACC) {
    this.PHONE_ACC = PHONE_ACC;
  }
  public String getPASSWORD_ACC() {
    return PASSWORD_ACC;
  }
  public void setPASSWORD_ACC(String PASSWORD_ACC) {
    this.PASSWORD_ACC = PASSWORD_ACC;
  }
  public String getROLE_ACC() {
    return ROLE_ACC;
  }
  public void setROLE_ACC(String ROLE_ACC) {
    this.ROLE_ACC = ROLE_ACC;
  }
  public String getNAME_USER() {
    return NAME_USER;
  }
  public void setNAME_USER(String NAME_USER) {
    this.NAME_USER = NAME_USER;
  }
  public LocalDateTime getDATE_JOIN() {
    return DATE_JOIN;
  }
  public void setDATE_JOIN(LocalDateTime DATE_JOIN) {
    this.DATE_JOIN = DATE_JOIN;
  }
  public User_Entity() {}
  @Override
  public String toString() {
    try {
      return "EMAIL " + SecurityFunction.decrypt(EMAIL_ACC);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}