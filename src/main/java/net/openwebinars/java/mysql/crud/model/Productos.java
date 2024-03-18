package net.openwebinars.java.mysql.crud.model;

import java.time.LocalDate;
import java.util.Objects;

public class Productos {

    private int id_producto;
    private String nombre;
    private LocalDate fechaCaducidad;
    private String color;


    public Productos(int id,String nombre, LocalDate fechaCaducidad, String color) {
        this.id_producto = id;
        this.nombre = nombre;
        this.fechaCaducidad = fechaCaducidad;
        this.color = color;
    }

    public Productos(String nombre, LocalDate fechaCaducidad, String color) {
        this.nombre = nombre;
        this.fechaCaducidad = fechaCaducidad;
        this.color = color;
    }
    public Productos() { }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public LocalDate getFechaCaducidad() {        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Productos productos = (Productos) o;
        return id_producto == productos.id_producto && Objects.equals(nombre, productos.nombre)  && Objects.equals(fechaCaducidad, productos.fechaCaducidad) && Objects.equals(color, productos.color);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id_producto, nombre, fechaCaducidad, color);
    }

    @Override
    public String toString() {
        return "Productos{" +
                "id_producto=" + id_producto +
                ", nombre='" + nombre + '\'' +
                ", fechaCaducidad=" + fechaCaducidad +
                ", color='" + color + '\'' +
                '}';
    }
}

