package application;

import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Program {
  public static void main(String[] args) {
    Department department = new Department(1, "testes");
    System.out.println(department);

    Seller seller = new Seller(21, "Bobo", "boobbb", new Date(), 3000.0, department);
    System.out.println("\n" + seller);

  }
}
