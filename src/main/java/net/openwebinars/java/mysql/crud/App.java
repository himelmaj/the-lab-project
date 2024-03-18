package net.openwebinars.java.mysql.crud;

import net.openwebinars.java.mysql.crud.dao.ProductosDao;
import net.openwebinars.java.mysql.crud.dao.ProductosDaoImpl;
import net.openwebinars.java.mysql.crud.model.Productos;
import net.openwebinars.java.mysql.crud.pool.MyDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class App {

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.init();
    }

    public static void testDao() {
        ProductosDao dao = (ProductosDao) ProductosDaoImpl.getInstance();

        Productos prod = new Productos("Producto1", LocalDate.of(2024, 3, 15), "Azul");

        try {
            int n = dao.add(prod);
            System.out.println("Número de registros insertados: " + n);

            List<Productos> productos = dao.getAll();

            if (productos.isEmpty())
                System.out.println("No hay productos registrados");
            else
                productos.forEach(System.out::println);

            Productos prod1 = dao.getById(1);

            System.out.println("\n" + prod1 + "\n");

            prod1.setColor("Verde");

            n = dao.update(prod1);

            prod1 = dao.getById(1);

            System.out.println("\n" + prod1 + "\n");

            dao.delete(1);

            productos = dao.getAll();
            if (productos.isEmpty())
                System.out.println("No hay productos registrados");
            else
                productos.forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testPool() {
        try (Connection conn = MyDataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);

            while (tables.next()) {
                System.out.println(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
