package com.example.dkkp.model;
import jakarta.persistence.*;
import com.example.dkkp.model.EnumType.Bug_Type;
import java.time.LocalDateTime;
@Entity
@Table(name = "report_bug")
public class Report_Bug {
  @Id
  @Column(name = "\"ID_REPORT\"")
  private Integer ID_REPORT;
  @Column(name = "\"TYPE_BUG\"")
  @Enumerated(jakarta.persistence.EnumType.ORDINAL)
  private Bug_Type TYPE_BUG;
  @Column(name = "\"SCRIPT_BUG\"")
  private String SCRIPT_BUG;
  @Column(name = "\"DATE_REPORT\"")
  private LocalDateTime DATE_REPORT;
  @Column(name = "\"ID_USER\"")
  private String ID_USER;
  @Transient
  private String EMAIL_ACC;
  @ManyToOne
  @JoinColumn(name = "\"ID_USER\"", referencedColumnName = "\"ID_USER\"", insertable = false, updatable = false)
  private User_Entity userEntity;
  public String getEMAIL_ACC() {
    return EMAIL_ACC;
  }
  public void setEMAIL_ACC(String EMAIL_ACC) {
    this.EMAIL_ACC = EMAIL_ACC;
  }
  public Integer getID_REPORT() {
    return ID_REPORT;
  }
  public void setID_REPORT(Integer ID_REPORT) {
    this.ID_REPORT = ID_REPORT;
  }
  public Bug_Type getTYPE_BUG() {
    return TYPE_BUG;
  }
  public void setTYPE_BUG(Bug_Type TYPE_BUG) {
    this.TYPE_BUG = TYPE_BUG;
  }
  public String getSCRIPT_BUG() {
    return SCRIPT_BUG;
  }
  public void setSCRIPT_BUG(String SCRIPT_BUG) {
    this.SCRIPT_BUG = SCRIPT_BUG;
  }
  public LocalDateTime getDATE_REPORT() {
    return DATE_REPORT;
  }
  public void setDATE_REPORT(LocalDateTime DATE_REPORT) {
    this.DATE_REPORT = DATE_REPORT;
  }
  public String getID_USER() {
    return ID_USER;
  }
  public void setID_USER(String ID_USER) {
    this.ID_USER = ID_USER;
  }
  public Report_Bug(Integer ID_REPORT, Bug_Type TYPE_BUG, String SCRIPT_BUG, LocalDateTime DATE_REPORT, String ID_USER) {
    this.ID_REPORT = ID_REPORT;
    this.TYPE_BUG = TYPE_BUG;
    this.SCRIPT_BUG = SCRIPT_BUG;
    this.DATE_REPORT = DATE_REPORT;
    this.ID_USER = ID_USER;
  }
  public Report_Bug(Integer ID_REPORT, Bug_Type TYPE_BUG, String SCRIPT_BUG, LocalDateTime DATE_REPORT, String ID_USER, String EMAIL_ACC) {
    this.ID_REPORT = ID_REPORT;
    this.TYPE_BUG = TYPE_BUG;
    this.SCRIPT_BUG = SCRIPT_BUG;
    this.DATE_REPORT = DATE_REPORT;
    this.ID_USER = ID_USER;
    this.EMAIL_ACC = EMAIL_ACC;
  }
  public Report_Bug() {}
}