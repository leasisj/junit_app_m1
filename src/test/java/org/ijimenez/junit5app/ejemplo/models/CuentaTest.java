package org.ijimenez.junit5app.ejemplo.models;

import org.ijimenez.junit5app.ejemplo.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("isael", new BigDecimal("1000.12345"));
        assertEquals("isael", cuenta.getPersona());
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Isael", new BigDecimal("1000.12345"));
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("Susan", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("Susan", new BigDecimal("8900.9997"));
        //assertNotEquals(cuenta, cuenta2);//comparacion por referencia
        assertEquals(cuenta, cuenta2);//comparacion por valor --> refactorizamos el metodo equals
    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("Isael", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testcreditoCuenta() {
        Cuenta cuenta = new Cuenta("Isael", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }


    @Test
    void testDierneroInsuficienteException() {
        Cuenta cuenta = new Cuenta("Isael", new BigDecimal("1000.12345"));
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debitotwo(new BigDecimal("1500"));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTranferirDineroCuentas() {
        Cuenta destino = new Cuenta("Isael", new BigDecimal("2500"));
        Cuenta origen = new Cuenta("Susan", new BigDecimal("1500.8989"));
        Banco banco = new Banco();
        banco.setNombre("Banco bbva");
        banco.tranferir(origen, destino, new BigDecimal(500));
        assertEquals("1000.8989", origen.getSaldo().toPlainString());
        assertEquals("3000", destino.getSaldo().toPlainString());
    }

    @Test
    void testRelacionBancoCuentas() {
        Cuenta destino = new Cuenta("Isael", new BigDecimal("2500"));
        Cuenta origen = new Cuenta("Susan", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        //relacion entre banco y cuentas
        banco.addCuenta(destino);
        banco.addCuenta(origen);

        banco.setNombre("Banco bbva");
        //se prueba el metodo tranferir
        banco.tranferir(origen, destino, new BigDecimal(500));

        assertAll(() -> assertEquals("1000.8989", origen.getSaldo().toPlainString()),
                () -> assertEquals("3000", destino.getSaldo().toPlainString()),
                () -> assertEquals(2, banco.getCuentas().size()),
                () -> assertEquals("Banco bbva", destino.getBanco().getNombre()),
                () -> assertEquals("Isael", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Isael"))
                        .findFirst()
                        .get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream()
                        .anyMatch(c -> c.getPersona().equals("Susan")))
        );
    }
}