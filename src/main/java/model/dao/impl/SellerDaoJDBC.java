package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {
  private Connection connection;

  public SellerDaoJDBC(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void insert(Seller seller) {
    PreparedStatement preparedStatement = null;

    try {
      preparedStatement = connection.prepareStatement(
              "insert into seller " +
                      "(name, email, birthdate, basesalary, departmentid) " +
                      "values " +
                      "(?,?,?,?,?)",
              Statement.RETURN_GENERATED_KEYS);

      preparedStatement.setString(1, seller.getName());
      preparedStatement.setString(2, seller.getEmail());
      preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
      preparedStatement.setDouble(4, seller.getBaseSalary());
      preparedStatement.setInt(5, seller.getDepartment().getId());

      int rowsAffected = preparedStatement.executeUpdate();

      if (rowsAffected > 0) {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
          seller.setId(resultSet.getInt(1));
        }
        DB.closeResultSet(resultSet);
      } else {
        throw new DbException("Unexpected error! No rows affected!");
      }
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
    }
  }

  @Override
  public void update(Seller seller) {

  }

  @Override
  public void deleteById(Integer id) {

  }

  @Override
  public Seller findById(Integer id) {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      preparedStatement = connection.prepareStatement(
              "select seller.*,department.Name as DepName " +
                      "from seller inner join department " +
                      "on seller.departmentid = department.id " +
                      "where seller.id = ?");

      preparedStatement.setInt(1, id);
      resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        Department department = instantiateDepartment(resultSet);
        Seller seller = instantiateSeller(resultSet, department);

        return seller;
      }

      return null;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
      DB.closeResultSet(resultSet);
    }
  }

  @Override
  public List<Seller> findByDepartment(Department department) {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      preparedStatement = connection.prepareStatement(
              "select seller.*,department.Name as DepName " +
                      "from seller inner join department " +
                      "on seller.DepartmentId = department.Id " +
                      "where DepartmentId = ? " +
                      "order by Name");

      preparedStatement.setInt(1, department.getId());
      resultSet = preparedStatement.executeQuery();

      List<Seller> sellerList = new ArrayList<>();
      Map<Integer, Department> departmentMap = new HashMap<>();

      while (resultSet.next()) {
        Department departmentResulSet = departmentMap.get(resultSet.getInt("DepartmentId"));

        if (departmentResulSet == null) {
          departmentResulSet = instantiateDepartment(resultSet);
          departmentMap.put(resultSet.getInt("DepartmentId"), departmentResulSet);
        }

        Seller seller = instantiateSeller(resultSet, departmentResulSet);
        sellerList.add(seller);
      }

      return sellerList;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
      DB.closeResultSet(resultSet);
    }
  }


  @Override
  public List<Seller> findAll() {
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      preparedStatement = connection.prepareStatement(
              "select seller.*,department.Name as DepName " +
                      "from seller inner join department " +
                      "on seller.DepartmentId = department.Id " +
                      "order by Name");

      resultSet = preparedStatement.executeQuery();

      List<Seller> sellerList = new ArrayList<>();
      Map<Integer, Department> departmentMap = new HashMap<>();

      while (resultSet.next()) {
        Department departmentResulSet = departmentMap.get(resultSet.getInt("DepartmentId"));

        if (departmentResulSet == null) {
          departmentResulSet = instantiateDepartment(resultSet);
          departmentMap.put(resultSet.getInt("DepartmentId"), departmentResulSet);
        }

        Seller seller = instantiateSeller(resultSet, departmentResulSet);
        sellerList.add(seller);
      }

      return sellerList;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DB.closeStatement(preparedStatement);
      DB.closeResultSet(resultSet);
    }
  }

  private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
    Seller seller = new Seller();
    seller.setId(resultSet.getInt("Id"));
    seller.setName(resultSet.getString("Name"));
    seller.setEmail(resultSet.getString("Email"));
    seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
    seller.setBirthDate(resultSet.getDate("BirthDate"));
    seller.setDepartment(department);
    return seller;
  }

  private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
    Department department = new Department();
    department.setId(resultSet.getInt("DepartmentId"));
    department.setName(resultSet.getString("DepName"));
    return department;
  }
}
