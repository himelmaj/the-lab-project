package net.openwebinars.java.mysql.crud;

import net.openwebinars.java.mysql.crud.dao.ProductoDao;
import net.openwebinars.java.mysql.crud.dao.ProductoDaoImpl;
import net.openwebinars.java.mysql.crud.model.Producto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class MenuProducto {

    private KeyboardReader reader;
    private ProductoDao dao;

    public MenuProducto() {
        reader = new KeyboardReader();
        dao = ProductoDaoImpl.getInstance();
    }

    public void init() {

        int opcion;

        do {
            menu();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1:
                    listAll();
                    break;
                case 2:
                    listById();
                    break;
                case 3:
                    insert();
                    break;
                case 4:
                    update();
                    break;
                case 5:
                    delete();
                    break;
                case 0:
                    System.out.println("\nSaliendo del programa...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una operación válida\n\n");
            }

        } while(opcion != 0);
    }

    public void menu() {
        System.out.println("SISTEMA DE GESTIÓN DE PRODUCTOS");
        System.out.println("===============================\n");
        System.out.println("-> Introduzca una opción de entre las siguientes: \n");
        System.out.println("0: Salir");
        System.out.println("1: Listar todos los productos");
        System.out.println("2: Listar un producto por su ID");
        System.out.println("3: Insertar un nuevo producto");
        System.out.println("4: Actualizar un producto");
        System.out.println("5: Eliminar un producto");
        System.out.print("\nOpción: ");
    }

    public void insert() {
        System.out.println("\nINSERCIÓN DE UN NUEVO PRODUCTO");
        System.out.println("--------------------------------\n");

        System.out.print("Introduzca el nombre del producto: ");
        String nombre = reader.nextLine();

        System.out.print("Introduzca la fecha de caducidad del producto (formato dd/MM/aaaa): ");
        LocalDate fechaCaducidad = reader.nextLocalDate();

        try {
            dao.add(new Producto(nombre, fechaCaducidad));
            System.out.println("Nuevo producto registrado");
        } catch (SQLException e) {
            System.err.println("Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    public void listAll() {
        System.out.println("\nLISTADO DE TODOS LOS PRODUCTOS");
        System.out.println("--------------------------------\n");

        try {
            List<Producto> result = dao.getAll();

            if (result.isEmpty())
                System.out.println("No hay productos registrados en la base de datos");
            else {
                printCabeceraTablaProducto();
                result.forEach(this::printProducto);
            }

        } catch (SQLException e) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("\n");
    }

    public void listById() {
        System.out.println("\nBÚSQUEDA DE PRODUCTOS POR ID");
        System.out.println("------------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a buscar: ");
            int id = reader.nextInt();

            Producto producto = dao.getById(id);

            if (producto == null)
                System.out.println("No hay productos registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaProducto();
                printProducto(producto);
            }

            System.out.println("\n");

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
    }

    public void update() {
        System.out.println("\nACTUALIZACIÓN DE UN PRODUCTO");
        System.out.println("------------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a buscar: ");
            int id = reader.nextInt();

            Producto producto = dao.getById(id);

            if (producto == null)
                System.out.println("No hay productos registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaProducto();
                printProducto(producto);
                System.out.println("\n");

                System.out.print("Introduzca el nombre del producto: ");
                String nombre = reader.nextLine();
                nombre = (nombre.isBlank()) ? producto.getNombreProducto() : nombre;

                System.out.printf("Introduzca la fecha de caducidad del producto (formato dd/MM/aaaa) (%s): ",
                        producto.getFechaCaducidad().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                String strFechaCaducidad = reader.nextLine();
                LocalDate fechaCaducidad = (strFechaCaducidad.isBlank()) ? producto.getFechaCaducidad() :
                        LocalDate.parse(strFechaCaducidad, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                producto.setNombreProducto(nombre);
                producto.setFechaCaducidad(fechaCaducidad);

                dao.update(producto);

                System.out.println("");
                System.out.printf("Producto con ID %s actualizado", producto.getId_producto());
                System.out.println("");
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
    }

    public void delete() {
        System.out.println("\nBORRADO DE UN PRODUCTO");
        System.out.println("-----------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a borrar: ");
            int id = reader.nextInt();

            System.out.printf("¿Está seguro de querer eliminar el producto con ID=%s? (s/n): ", id);
            String borrar = reader.nextLine();

            if (borrar.equalsIgnoreCase("s")) {
                dao.delete(id);
                System.out.printf("El producto con ID %s se ha borrado\n", id);
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");
    }

    private void printCabeceraTablaProducto() {
        System.out.printf("%2s %30s %15s", "ID", "NOMBRE", "FECHA CADUCIDAD");
        System.out.println("");
        IntStream.range(1, 60).forEach(x -> System.out.print("-"));
        System.out.println("\n");
    }
    private void printProducto(Producto producto) {
        System.out.printf("%2s %30s %15s\n",
                producto.getId_producto(),
                producto.getNombreProducto(),
                producto.getFechaCaducidad().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    static class KeyboardReader {

        BufferedReader br;
        StringTokenizer st;

        public KeyboardReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {

            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException ex) {
                    System.err.println("Error leyendo del teclado");
                    ex.printStackTrace();
                }
            }

            return st.nextToken();

        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        LocalDate nextLocalDate() {
            return LocalDate.parse(next(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        String nextLine() {
            String str = "";
            try {
                if (st.hasMoreElements())
                    str = st.nextToken("\n");
                else
                    str = br.readLine();
            } catch (IOException ex) {
                System.err.println("Error leyendo del teclado");
                ex.printStackTrace();
            }

            return str;
        }
    }
}
