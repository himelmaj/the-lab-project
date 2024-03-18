package net.openwebinars.java.mysql.crud.dao;

import net.openwebinars.java.mysql.crud.model.Productos;
import net.openwebinars.java.mysql.crud.pool.MyDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductosDaoImpl implements ProductosDao {

    private static final String INSERT_QUERY = "INSERT INTO productos (nombre, fecha_caducidad, color) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM productos WHERE id_producto = ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM productos";
    private static final String UPDATE_QUERY = "UPDATE productos SET nombre = ?, fecha_caducidad = ?, color = ? WHERE id_producto = ?";
    private static final String DELETE_QUERY = "DELETE FROM productos WHERE id_producto = ?";

    private static ProductosDaoImpl instance;

    private ProductosDaoImpl() {
    }

    public static synchronized ProductosDaoImpl getInstance() {
        if (instance == null) {
            instance = new ProductosDaoImpl();
        }
        return instance;
    }

    @Override
    public int add(Productos producto) throws SQLException {
        String INSERT_QUERY = "INSERT INTO productos (nombre, fecha_caducidad, color) VALUES (?, ?, ?)";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, producto.getNombre());
            statement.setDate(2, Date.valueOf(producto.getFechaCaducidad()));
            statement.setString(3, producto.getColor());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("No se pudo insertar el producto en la base de datos.");
            }

            // Obtener el ID generado por la base de datos
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    producto.setId_producto(generatedId); // Asignar el ID generado al objeto Productos
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para el producto insertado.");
                }
            }

            return rowsInserted;
        }
    }





    @Override
    public Productos getById(int id) throws SQLException {
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToProductos(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public List<Productos> getAll() throws SQLException {
        List<Productos> productosList = new ArrayList<>();
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
               productosList.add(mapResultSetToProductos(resultSet));
                             }
        }
        return productosList;
    }

    @Override
    public int update(Productos producto) throws SQLException {
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, producto.getNombre());
            statement.setDate(2, Date.valueOf(producto.getFechaCaducidad()));
            statement.setString(3, producto.getColor());
            statement.setInt(4, producto.getId_producto());
            return statement.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Productos mapResultSetToProductos(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id_producto"); // Obtener el valor del campo id_producto
        String nombre = resultSet.getString("nombre");
        LocalDate fechaCaducidad = resultSet.getDate("fecha_caducidad").toLocalDate();
        String color = resultSet.getString("color");
        return new Productos(Integer.parseInt(id), nombre, fechaCaducidad, color);
    }

}
