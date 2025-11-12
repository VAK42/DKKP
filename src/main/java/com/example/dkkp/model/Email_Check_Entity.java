package com.example.dkkp.model;
import jakarta.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
@Entity
@Table(name = "email_check")
public class Email_Check_Entity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"ID_CHECK\"")
  private Integer ID_CHECK;
  @Column(name = "\"EMAIL\"")
  private String EMAIL;
  @Column(name = "\"TOKEN\"")
  private String TOKEN;
  @Column(name = "\"DATE_END\"")
  private LocalDateTime DATE_END;
  public Integer getID_CHECK() {
    return ID_CHECK;
  }
  public void setID_CHECK(Integer ID_CHECK) {
    this.ID_CHECK = ID_CHECK;
  }
  public String getEMAIL() {
    return EMAIL;
  }
  public void setEMAIL(String EMAIL) {
    this.EMAIL = EMAIL;
  }
  public String getTOKEN() {
    return TOKEN;
  }
  public void setTOKEN(String TOKEN) {
    this.TOKEN = TOKEN;
  }
  public LocalDateTime getDate_End() {
    return DATE_END;
  }
  public void setDate_End(LocalDateTime DATE_ẸND) {
    this.DATE_END = DATE_ẸND;
  }
  public Email_Check_Entity() {}
  public Email_Check_Entity(String EMAIL, String TOKEN, LocalDateTime DATE_END) {
    this.EMAIL = EMAIL;
    this.TOKEN = TOKEN;
    this.DATE_END = DATE_END;
  }
}