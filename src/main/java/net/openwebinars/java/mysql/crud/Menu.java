package net.openwebinars.java.mysql.crud;

import net.openwebinars.java.mysql.crud.dao.ProductosDaoImpl;
import net.openwebinars.java.mysql.crud.model.Productos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Menu {

    private KeyboardReader reader;
    private ProductosDaoImpl dao;

    public Menu() {
        reader = new KeyboardReader();
        dao = ProductosDaoImpl.getInstance();
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

        System.out.print("Introduzca el color: ");
        String color = reader.nextLine();

        try {
            dao.add(new Productos(nombre, fechaCaducidad, color));
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
            List<Productos> result = dao.getAll();

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
            Productos producto = dao.getById(id);

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
            Productos producto = dao.getById(id);

            if (producto == null) {
                System.out.println("No hay productos registrados en la base de datos con ese ID");
            } else {
                printCabeceraTablaProducto();
                printProducto(producto);
                System.out.println("\n");

                System.out.printf("Introduzca el nombre del producto (%s): ", producto.getNombre());
                String nombre = reader.nextLine();
                nombre = (nombre.isBlank()) ? producto.getNombre() : nombre;

                System.out.printf("Introduzca la fecha de caducidad del producto (formato dd/MM/aaaa) (%s): ", producto.getFechaCaducidad().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                String strFechaCaducidad = reader.nextLine();
                LocalDate fechaCaducidad = (strFechaCaducidad.isBlank()) ? producto.getFechaCaducidad() : LocalDate.parse(strFechaCaducidad, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                System.out.printf("Introduzca el color del producto (%s): ", producto.getColor());
                String color = reader.nextLine();
                color = (color.isBlank()) ? producto.getColor() : color;

                producto.setNombre(nombre);
                producto.setFechaCaducidad(fechaCaducidad);
                producto.setColor(color);

                dao.update(producto);

                System.out.println("");
                System.out.printf("Producto con ID %s actualizado\n", producto.getId_producto());
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
    }

    public void delete() {
        System.out.println("\nELIMINACIÓN DE UN PRODUCTO");
        System.out.println("---------------------------\n");

        try {
            System.out.print("Introduzca el ID del producto a borrar: ");
            int id = reader.nextInt();
            System.out.printf("¿Está seguro de querer eliminar el producto con ID=%s? (s/n): ", id);
            String confirmacion = reader.nextLine();

            if (confirmacion.equalsIgnoreCase("s")) {
                dao.delete(id);
                System.out.printf("El producto con ID %s se ha eliminado\n", id);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }
        System.out.println("");
    }

    private void printCabeceraTablaProducto() {
        System.out.printf("%2s %30s %14s %10s\n", "ID", "NOMBRE", "FEC. CADUCIDAD", "COLOR");
        IntStream.range(1, 80).forEach(x -> System.out.print("-"));
        System.out.println();
    }

    private void printProducto(Productos producto) {
        System.out.printf("%2d %30s %9s %10s\n",
                producto.getId_producto(),
                producto.getNombre(),
                producto.getFechaCaducidad().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                producto.getColor());
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
            return LocalDate.parse(next(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
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
