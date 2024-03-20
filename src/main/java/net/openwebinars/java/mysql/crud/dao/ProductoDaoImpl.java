package net.openwebinars.java.mysql.crud.dao;

import net.openwebinars.java.mysql.crud.model.Producto;
import net.openwebinars.java.mysql.crud.pool.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDaoImpl implements ProductoDao {

    private static ProductoDaoImpl instance;

    static {
        instance = new ProductoDaoImpl();
    }

    private ProductoDaoImpl() {}

    public static ProductoDaoImpl getInstance() {
        return instance;
    }

    @Override
    public int add(Producto producto) throws SQLException {
        String sql = """
                    INSERT INTO producto (nombre_producto, fecha_caducidad)
                    VALUES (?, ?);
                """;

        int result;

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, producto.getNombreProducto());
            pstm.setDate(2, Date.valueOf(producto.getFechaCaducidad()));

            result = pstm.executeUpdate();
        }

        return result;
    }

    @Override
    public Producto getById(int id) throws SQLException {
        Producto result = null;

        String sql = "SELECT * FROM producto WHERE id_producto = ?";

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);

            try(ResultSet rs = pstm.executeQuery()) {

                while(rs.next()) {
                    result = new Producto();
                    result.setId_producto(rs.getInt("id_producto"));
                    result.setNombreProducto(rs.getString("nombre_producto"));
                    result.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
                }

            }
        }

        return result;
    }

    @Override
    public List<Producto> getAll() throws SQLException {
        String sql = "SELECT * FROM producto";

        List<Producto> result = new ArrayList<>();

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()) {

            Producto producto;

            while(rs.next()) {
                producto = new Producto();
                producto.setId_producto(rs.getInt("id_producto"));
                producto.setNombreProducto(rs.getString("nombre_producto"));
                producto.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());

                result.add(producto);
            }
        }

        return result;
    }

    @Override
    public int update(Producto producto) throws SQLException {
        String sql = """
                    UPDATE producto SET
                        nombre_producto = ?,
                        fecha_caducidad = ?
                    WHERE id_producto = ?
                """;

        int result;

        try(Connection conn = MyDataSource.getConnection();
            PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, producto.getNombreProducto());
            pstm.setDate(2, Date.valueOf(producto.getFechaCaducidad()));
            pstm.setInt(3, producto.getId_producto());

            result = pstm.executeUpdate();
        }

        return result;
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id_producto = ?";

        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, id);
            pstm.executeUpdate();
        }
    }
}
