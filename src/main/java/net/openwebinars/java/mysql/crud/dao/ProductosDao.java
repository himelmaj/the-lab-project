package net.openwebinars.java.mysql.crud.dao;

import net.openwebinars.java.mysql.crud.model.Productos;

import java.sql.SQLException;
import java.util.List;

public interface ProductosDao {

    int add(Productos producto) throws SQLException;

    Productos getById(int id) throws SQLException;

    List<Productos> getAll() throws SQLException;

    int update(Productos producto) throws SQLException;

    void delete(int id) throws SQLException;

}
