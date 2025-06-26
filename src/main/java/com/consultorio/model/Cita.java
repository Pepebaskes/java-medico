package com.consultorio.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cita {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty apellido = new SimpleStringProperty();
    private final StringProperty fechaConsulta = new SimpleStringProperty();
    private final StringProperty horaConsulta = new SimpleStringProperty();
    private final StringProperty localidad = new SimpleStringProperty();
    private final StringProperty telefono = new SimpleStringProperty();
    private final BooleanProperty asistencia = new SimpleBooleanProperty();

   public Cita(int id, String nombre, String apellido, String fecha, String hora, String localidad, String telefono, boolean asistencia) {
    this.id.set(id);
    this.nombre.set(nombre);
    this.apellido.set(apellido);
    this.fechaConsulta.set(fecha);
    this.horaConsulta.set(hora);
    this.localidad.set(localidad);
    this.telefono.set(telefono);
    this.asistencia.set(asistencia);
}

public Cita(String nombre, String apellido, String fecha, String hora, String localidad, String telefono, boolean asistencia) {
    this(0, nombre, apellido, fecha, hora, localidad, telefono, asistencia);
}
    public int getId() { return id.get(); }
    public String getNombre() { return nombre.get(); }
    public String getApellido() { return apellido.get(); }
    public String getFechaConsulta() { return fechaConsulta.get(); }
    public String getHoraConsulta() { return horaConsulta.get(); }
    public String getLocalidad() { return localidad.get(); }
    public String getTelefono() { return telefono.get(); }
    public boolean isAsistencia() { return asistencia.get(); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty apellidoProperty() { return apellido; }
    public StringProperty fechaConsultaProperty() { return fechaConsulta; }
    public StringProperty horaConsultaProperty() { return horaConsulta; }
    public StringProperty localidadProperty() { return localidad; }
    public StringProperty telefonoProperty() { return telefono; }
    public BooleanProperty asistenciaProperty() { return asistencia; }
    public void setAsistencia(boolean asistencia) {
    this.asistencia.set(asistencia);
}

}
