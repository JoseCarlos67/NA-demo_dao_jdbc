package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {
  public static void main(String[] args) {
    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println("=== TEST 1: seller findById ===");
    Seller seller = sellerDao.findById(3);
    System.out.println(seller);

    System.out.println("\n=== TEST 2: seller findByDepartment ===");
    List<Seller> sellerList = sellerDao.findByDepartment(new Department(2, null));

    for (Seller obj : sellerList) {
      System.out.println(obj);
    }

    System.out.println("\n=== TEST 3: seller findAll ===");
    sellerList = sellerDao.findAll();
    for (Seller obj : sellerList) {
      System.out.println(obj);
    }

    System.out.println("\n=== TEST 4: seller findAll ===");
    Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, new Department(2, null));
    sellerDao.insert(newSeller);
    System.out.println("Inserted! New ID = " + newSeller.getId());

    System.out.println("\n=== TEST 5: seller update ===");
    newSeller = sellerDao.findById(5);
    newSeller.setEmail("teste#$@hotmail.com");
    sellerDao.update(newSeller);
    System.out.println("Update completed!");

    System.out.println("\n=== TEST 6: seller delete ===");
    sellerDao.deleteById(5);
    System.out.printf("Delete completed!");
  }
}
