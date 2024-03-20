package net.openwebinars.java.mysql.crud;

import net.openwebinars.java.mysql.crud.dao.EmpleadoDao;
import net.openwebinars.java.mysql.crud.dao.EmpleadoDaoImpl;
import net.openwebinars.java.mysql.crud.model.Empleado;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class MenuEmpleado {

    private KeyboardReader reader;
    private EmpleadoDao dao;

    public MenuEmpleado() {
        reader = new KeyboardReader();
        dao = EmpleadoDaoImpl.getInstance();
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

        System.out.println("SISTEMA DE GESTIÓN DE EMPLEADOS");
        System.out.println("===============================\n");
        System.out.println("-> Introduzca una opción de entre las siguientes: \n");
        System.out.println("0: Salir");
        System.out.println("1: Listar todos los empleados");
        System.out.println("2: Listar un empleado por su ID");
        System.out.println("3: Insertar un nuevo empleado");
        System.out.println("4: Actualizar un empleado");
        System.out.println("5: Eliminar un empleado");
        System.out.print("\nOpción: ");


    }

    public void insert() {

        System.out.println("\nINSERCIÓN DE UN NUEVO EMPLEADO");
        System.out.println("--------------------------------\n");

        System.out.print("Introduzca el nombre (sin apellidos) del empleado: ");
        String nombre = reader.nextLine();

        System.out.print("Introduzca los apellidos del empleado: ");
        String apellidos = reader.nextLine();

        System.out.print("Introduzca la fecha de nacimiento del empleado (formato dd/MM/aaaa): ");
        LocalDate fechaNacimiento = reader.nextLocalDate();

        System.out.print("Introduzca el puesto del empleado: ");
        String puesto = reader.nextLine();

        System.out.print("Introduzca el email del nuevo empleado: ");
        String email = reader.nextLine();

        try {
            dao.add(new Empleado(nombre, apellidos, fechaNacimiento, puesto, email));
            System.out.println("Nuevo empleado reigstrado");
        } catch (SQLException e) {
            System.err.println("Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");

    }

    public void listAll() {
        System.out.println("\nLISTADO DE TODOS LOS EMPLEADOS");
        System.out.println("--------------------------------\n");


        try {
            List<Empleado> result = dao.getAll();

            if (result.isEmpty())
                System.out.println("No hay empleados registrados en la base de datos");
            else {
                printCabeceraTablaEmpleado();
                result.forEach(this::printEmpleado);
            }

        } catch (SQLException e) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("\n");


    }

    public void listById() {

        System.out.println("\nBÚSQUEDA DE EMPLEADOS POR ID");
        System.out.println("------------------------------\n");

        try {

            System.out.print("Introduzca el ID del empleado a buscar: ");
            int id = reader.nextInt();

            Empleado empleado = dao.getById(id);

            if (empleado == null)
                System.out.println("No hay empleados registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaEmpleado();
                printEmpleado(empleado);
            }

            System.out.println("\n");

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

    }

    public void update() {

        System.out.println("\nACTUALIZACIÓN DE UN EMPLEADO");
        System.out.println("------------------------------\n");

        try {

            System.out.print("Introduzca el ID del empleado a buscar: ");
            int id = reader.nextInt();

            Empleado empleado = dao.getById(id);

            if (empleado == null)
                System.out.println("No hay empleados registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaEmpleado();
                printEmpleado(empleado);
                System.out.println("\n");

                System.out.printf("Introduzca el nombre (sin apellidos) del empleado (%s): ", empleado.getNombre());
                String nombre = reader.nextLine();
                nombre = (nombre.isBlank()) ? empleado.getNombre() : nombre;

                System.out.printf("Introduzca los apellidos del empleado (%s): ", empleado.getApellidos());
                String apellidos = reader.nextLine();
                apellidos = (apellidos.isBlank()) ? empleado.getApellidos() : apellidos;

                System.out.printf("Introduzca la fecha de nacimiento del empleado (formato dd/MM/aaaa) (%s)",
                        empleado.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                String strFechaNacimiento = reader.nextLine();
                LocalDate fechaNacimiento = (strFechaNacimiento.isBlank()) ? empleado.getFechaNacimiento() :
                            LocalDate.parse(strFechaNacimiento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                System.out.printf("Introduzca el puesto del empleado (%s): ", empleado.getPuesto());
                String puesto = reader.nextLine();
                puesto = (puesto.isBlank()) ? empleado.getPuesto() : puesto;

                System.out.printf("Introduzca el email del empleado (%s): ", empleado.getEmail());
                String email = reader.nextLine();
                email = (email.isBlank()) ? empleado.getEmail() : email;

                empleado.setNombre(nombre);
                empleado.setApellidos(apellidos);
                empleado.setFechaNacimiento(fechaNacimiento);
                empleado.setPuesto(puesto);
                empleado.setEmail(email);

                dao.update(empleado);

                System.out.println("");
                System.out.printf("Empleado con ID %s actualizado", empleado.getId_empleado());
                System.out.println("");

            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }


    }

    public void delete() {

        System.out.println("\nBORRADO DE UN EMPLEADO");
        System.out.println("-----------------------\n");

        try {

            System.out.print("Introduzca el ID del empleado a borrar: ");
            int id = reader.nextInt();

            System.out.printf("¿Está usted seguro de querer eliminar al empleado con ID=%s? (s/n): ", id);
            String borrar = reader.nextLine();

            if (borrar.equalsIgnoreCase("s")) {
                dao.delete(id);
                System.out.printf("El empleado con ID %s se ha borrado\n", id);
            }


        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");

    }




    private void printCabeceraTablaEmpleado() {
        System.out.printf("%2s %30s %8s %10s %25s", "ID", "NOMBRE", "FEC. NAC.", "PUESTO", "EMAIL");
        System.out.println("");
        IntStream.range(1, 100).forEach(x -> System.out.print("-"));
        System.out.println("\n");
    }

    private void printEmpleado(Empleado emp) {
        System.out.printf("%2s %30s %9s %10s %25s\n",
                emp.getId_empleado(),
                emp.getNombre() + " " + emp.getApellidos(),
                emp.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                emp.getPuesto(),
                emp.getEmail());
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
