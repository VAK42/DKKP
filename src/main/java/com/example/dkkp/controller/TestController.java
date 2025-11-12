package com.example.dkkp.controller;
import com.example.dkkp.model.*;
import com.example.dkkp.service.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class TestController {
  public static void main(String[] args) throws Exception {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("DKKPPersistenceUnit");
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    try {
      transaction.begin();
      ProductFinalService productFinalService = new ProductFinalService(entityManager);
      LocalDateTime start = LocalDateTime.now();
      LocalDateTime end = LocalDateTime.now();
      List < Product_Final_Entity > p = productFinalService.getProductDashBoard(null, null);
      for (Product_Final_Entity p1: p) {
        System.out.println("ID " + p1.getID_SP() + " NAME " + p1.getNAME_PRODUCT() +
          " QUANTITY " + p1.getSum_quantity() + " PRICE " + p1.getPRICE_SP());
      }
      transaction.commit();
    } catch (Exception e) {
      if (transaction.isActive()) {
        transaction.rollback();
        System.out.println("da loi " + e.getMessage());
      }
    } finally {
      entityManager.close();
      entityManagerFactory.close();
    }
  }
}