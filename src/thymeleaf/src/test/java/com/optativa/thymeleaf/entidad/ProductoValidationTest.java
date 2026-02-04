package com.optativa.thymeleaf.entidad;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void productoConCamposInvalidos_muestraMensajesCorrectos() {
        Producto p = new Producto();
        p.setId(1);
        p.setNombre("");        // @NotBlank -> error
        p.setPrecio(-10.0);     // @Positive -> error
        p.setCategoria("");     // @NotBlank -> error

        Set<ConstraintViolation<Producto>> violations = validator.validate(p);

        // Tenemos 3 errores
        assertEquals(3, violations.size());

        // Nombre
        assertTrue(
            violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("nombre") &&
                v.getMessage().equals("Nombre no puede estar en blanco")
            ),
            "Mensaje de error de 'nombre' incorrecto"
        );

        // Precio
        assertTrue(
            violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("precio") &&
                v.getMessage().equals("El precio tiene que ser positivo")
            ),
            "Mensaje de error de 'precio' incorrecto"
        );

        // Categoría
        assertTrue(
            violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("categoria") &&
                v.getMessage().equals("Categoría tiene que tener un valor")
            ),
            "Mensaje de error de 'categoria' incorrecto"
        );
    }

    @Test
    void productoValido_noTieneViolaciones() {
        Producto p = new Producto(1, "Teclado", 59.99, "Periféricos");

        Set<ConstraintViolation<Producto>> violations = validator.validate(p);

        assertTrue(violations.isEmpty(), "No debería haber errores de validación");
    }
}
