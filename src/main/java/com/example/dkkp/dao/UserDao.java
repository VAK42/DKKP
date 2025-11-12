package com.example.dkkp.dao;
import com.example.dkkp.model.User_Entity;
import com.example.dkkp.service.SecurityFunction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
public class UserDao {
  private final EntityManager entityManager;
  public UserDao(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  public void createUser(User_Entity user) {
    try {
      entityManager.persist(user);
    } catch (RuntimeException e) {
      throw new RuntimeException("Email already used");
    }
  }
  public EntityManager getEntityManager() {
    return this.entityManager;
  }
  public List < User_Entity > getAllUsers() {
    String jpql = "SELECT u FROM User_Entity u";
    TypedQuery < User_Entity > query = entityManager.createQuery(jpql, User_Entity.class);
    return query.getResultList();
  }
  public User_Entity getUsersByID(String id) {
    String jpql = "SELECT u FROM User_Entity u WHERE u.ID_USER = :id";
    TypedQuery < User_Entity > query = entityManager.createQuery(jpql, User_Entity.class);
    query.setParameter("id", id);
    return query.getResultList().get(0);
  }
  public User_Entity getUsersByMail(String mail) {
    String jpql = "SELECT u FROM User_Entity u WHERE u.EMAIL_ACC = :mail";
    TypedQuery < User_Entity > query = entityManager.createQuery(jpql, User_Entity.class);
    query.setParameter("mail", mail);
    List < User_Entity > result = query.getResultList();
    return result.getFirst();
  }
  public boolean isUserByMail(String mail) {
    String jpql = "SELECT u FROM User_Entity u WHERE u.EMAIL_ACC = :mail";
    TypedQuery < User_Entity > query = entityManager.createQuery(jpql, User_Entity.class);
    query.setParameter("mail", mail);
    query.setMaxResults(1);
    return !query.getResultList().isEmpty();
  }
  public boolean loginValidate(String EMAIL_ACC, String PASSWORD_ACC) {
    String jpql = "SELECT u FROM User_Entity u WHERE u.EMAIL_ACC = :email AND u.PASSWORD_ACC = :password";
    TypedQuery < User_Entity > query = entityManager.createQuery(jpql, User_Entity.class);
    query.setParameter("email", EMAIL_ACC);
    query.setParameter("password", PASSWORD_ACC);
    List < User_Entity > users = query.getResultList();
    return !users.isEmpty();
  }
  public void updateUser(String id, String add, String email, String phone, String role, String name) {
    User_Entity user = entityManager.find(User_Entity.class, id);
    if (user == null) {
      throw new RuntimeException("Can not find user");
    }
    if (email != null) user.setEMAIL_ACC(email);
    if (add != null) user.setADDRESS(add);
    if (phone != null) user.setPHONE_ACC(phone);
    if (role != null) user.setROLE_ACC(role);
    if (name != null) user.setNAME_USER(name);
    entityManager.merge(user);
  }
  public void changePasswordByEmail(String email, String newPassword) {
    String jpql = "SELECT u FROM User_Entity u WHERE u.EMAIL_ACC = :email";
    TypedQuery < User_Entity > query = entityManager.createQuery(jpql, User_Entity.class);
    query.setParameter("email", email);
    User_Entity user = query.getResultStream().findFirst().orElse(null);
    if (user == null) {
      throw new RuntimeException("Could not find user with email " + email);
    }
    String newSalt = SecurityFunction.generateSalt();
    user.setSALT(newSalt);
    String newPass = SecurityFunction.hashString(newPassword + newSalt);
    user.setPASSWORD_ACC(newPass);
    entityManager.merge(user);
  }
}