package net.openwebinars.java.mysql.crud.model;

import java.time.LocalDate;
import java.util.Objects;

public class Producto {

    private int id_producto;
    private String nombreProducto;
    private LocalDate fechaCaducidad;

    public Producto() { }

    public Producto(String nombreProducto, LocalDate fechaCaducidad) {
        this.nombreProducto = nombreProducto;
        this.fechaCaducidad = fechaCaducidad;
    }

    public Producto(int id_producto, String nombreProducto, LocalDate fechaCaducidad) {
        this(nombreProducto, fechaCaducidad);
        this.id_producto = id_producto;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id_producto == producto.id_producto && Objects.equals(nombreProducto, producto.nombreProducto) && Objects.equals(fechaCaducidad, producto.fechaCaducidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_producto, nombreProducto, fechaCaducidad);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id_producto=" + id_producto +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", fechaCaducidad=" + fechaCaducidad +
                '}';
    }
}
