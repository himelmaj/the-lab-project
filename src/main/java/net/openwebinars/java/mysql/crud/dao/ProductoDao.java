package net.openwebinars.java.mysql.crud.dao;

import net.openwebinars.java.mysql.crud.model.Producto;

import java.sql.SQLException;
import java.util.List;

public interface ProductoDao {

    int add(Producto producto) throws SQLException;

    Producto getById(int id) throws SQLException;

    List<Producto> getAll() throws SQLException;

    int update(Producto producto) throws SQLException;

    void delete(int id) throws SQLException;

}
