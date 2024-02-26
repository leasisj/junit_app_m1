package org.ijimenez.junit5app.ejemplo.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {
    private String nombre;

    private List<Cuenta> cuentas;

    public Banco() {
        cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void tranferir(Cuenta  origen, Cuenta destino, BigDecimal monto){
        origen.debitotwo(monto);
        destino.credito(monto);
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void addCuenta(Cuenta cuenta){
        cuentas.add(cuenta);
        //se hace la relacion entre el la cuenta y el banco
        cuenta.setBanco(this);
    }
}
