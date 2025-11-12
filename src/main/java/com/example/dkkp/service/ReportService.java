package com.example.dkkp.service;
import com.example.dkkp.dao.ReportDao;
import com.example.dkkp.model.EnumType;
import com.example.dkkp.model.Report_Bug;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
public class ReportService {
  private final ReportDao reportDao;
  public ReportService(EntityManager entityManager) {
    this.reportDao = new ReportDao(entityManager);
  }
  public boolean createNewReport(Report_Bug report) {
    Integer ID_REPORT = report.getID_REPORT();
    EnumType.Bug_Type TYPE_BUG = report.getTYPE_BUG();
    String SCRIPT_BUG = report.getSCRIPT_BUG();
    report.setDATE_REPORT(LocalDateTime.now());
    String ID_USER = report.getID_USER();
    return reportDao.createReport(report);
  }
  public boolean deleteReport(Integer id) {
    if (id == null) {
      return reportDao.deleteAllReports();
    }
    reportDao.deleteReportById(id);
    return true;
  }
  public List < Report_Bug > getFilteredReports(
    Report_Bug report,
    String typeDate,
    String sortField,
    String sortOrder,
    Integer setOff,
    Integer offSet
  ) {
    String userId = report.getID_USER();
    Integer reportId = report.getID_REPORT();
    LocalDateTime dateReport = report.getDATE_REPORT();
    EnumType.Bug_Type bugType = report.getTYPE_BUG();
    String EMAIL_ACC = report.getEMAIL_ACC();
    return reportDao.getFilteredReports(userId, EMAIL_ACC, reportId, bugType, dateReport, typeDate, sortField, sortOrder, offSet, setOff);
  }
  public Integer getCountFilteredReports(
    Report_Bug report,
    String typeDate
  ) {
    String userId = report.getID_USER();
    Integer reportId = report.getID_REPORT();
    LocalDateTime dateReport = report.getDATE_REPORT();
    EnumType.Bug_Type bugType = report.getTYPE_BUG();
    String EMAIL_ACC = report.getEMAIL_ACC();
    return reportDao.getFilteredReportCount(userId, EMAIL_ACC, reportId, bugType, dateReport, typeDate);
  }
}