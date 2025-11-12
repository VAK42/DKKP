package com.example.dkkp.service;
import com.example.dkkp.dao.UserDao;
import com.example.dkkp.model.User_Entity;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
public class UserService {
  private final UserDao userDao;
  public UserService(EntityManager entityManager) {
    this.userDao = new UserDao(entityManager);
  }
  public User_Entity getUsersByID(String id) throws Exception {
    return userDao.getUsersByID(id);
  }
  public User_Entity getUsersByEmail(String Email) throws Exception {
    User_Entity user = userDao.getUsersByMail(SecurityFunction.encrypt(Email));
    return user;
  }
  public void registerNewUser(User_Entity user) throws Exception {
    String salt = SecurityFunction.generateSalt();
    user.setSALT(salt);
    String pass = user.getPASSWORD_ACC() + salt;
    pass = SecurityFunction.hashString(pass);
    user.setPASSWORD_ACC(pass);
    user.setID_USER(SecurityFunction.hashString(user.getEMAIL_ACC()));
    LocalDateTime DATE_JOIN = LocalDateTime.now();
    user.setDATE_JOIN(DATE_JOIN);
    encryptUserSensitiveData(user);
    userDao.createUser(user);
  }
  public List < User_Entity > getAllUser() {
    return userDao.getAllUsers();
  }
  public boolean login(String EMAIL_ACC, String PASSWORD_ACC) throws Exception {
    String mail = EMAIL_ACC;
    if (userDao.isUserByMail(mail)) {
      System.out.println("co tai khoan");
      return userDao.loginValidate(mail, PASSWORD_ACC);
    }
    return false;
  }
  public void updateUserInfo(User_Entity user) throws Exception {
    encryptUserSensitiveData(user);
    String id = user.getID_USER();
    String email = user.getEMAIL_ACC();
    String phone = user.getPHONE_ACC();
    String role = user.getROLE_ACC();
    String name = user.getNAME_USER();
    String add = user.getADDRESS();
    userDao.updateUser(id, add, email, phone, role, name);
  }
  public void changePassword(String mail, String newPassword) throws Exception {
    userDao.changePasswordByEmail(SecurityFunction.encrypt(mail), newPassword);
  }
  public void decryptUserSensitiveData(User_Entity user) throws Exception {
    System.out.println("trong dec 1");
    if (user.getADDRESS() != null) {
      System.out.println("trong dec 2");
      user.setADDRESS(SecurityFunction.decrypt(user.getADDRESS()));
      System.out.println("trong dec 3");
    }
    if (user.getPHONE_ACC() != null) {
      System.out.println("trong dec 4");
      user.setPHONE_ACC(SecurityFunction.decrypt(user.getPHONE_ACC()));
      System.out.println("trong dec 5");
    }
    if (user.getEMAIL_ACC() != null) {
      System.out.println("trong dec 6");
      user.setEMAIL_ACC(SecurityFunction.decrypt(user.getEMAIL_ACC()));
      System.out.println("trong dec 7");
    }
    System.out.println("trong dec");
  }
  public void encryptUserSensitiveData(User_Entity user) throws Exception {
    if (user.getADDRESS() != null) {
      user.setADDRESS(SecurityFunction.encrypt(user.getADDRESS()));
    }
    if (user.getPHONE_ACC() != null) {
      user.setPHONE_ACC(SecurityFunction.encrypt(user.getPHONE_ACC()));
    }
    if (user.getEMAIL_ACC() != null) {
      user.setEMAIL_ACC(SecurityFunction.encrypt(user.getEMAIL_ACC()));
    }
  }
}