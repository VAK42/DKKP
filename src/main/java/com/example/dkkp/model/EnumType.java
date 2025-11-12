package com.example.dkkp.model;
public class EnumType {
  public enum Bug_Type {
    UI("UI Issue"),
      Perf("Perfomance Bug"),
      Comp("Comp bug"),
      NET("Net bug"),
      DATA("Data bug"),
      FUNC("Funct bug");
    private final String description;
    Bug_Type(String description) {
      this.description = description;
    }
    public String getDescription() {
      return description;
    }
  }
  public enum Status_Bill {
    PEN("Pending"),
      CONF("Payed"),
      SHIP("Shipped"),
      DELI("Delivered"),
      CANC("Cancel");
    private final String description;
    Status_Bill(String description) {
      this.description = description;
    }
    public String getDescription() {
      return description;
    }
  }
}