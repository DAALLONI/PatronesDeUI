package main;

import content.TarjetaElemento;
import input.ValidadorForgiving;
import navigation.BreadcrumbNavigator;
import state.GestorSesionUI;

public class Main {

    public static void main(String[] args) {
        casoNavegacion();
        casoContenido();
        casoForgivingFormat();
        casoCompletenessMeter();
        casoLazyRegistrationYToast();
    }

    private static void casoNavegacion() {
        System.out.println("--- 1. PATRÓN DE NAVEGACIÓN (BREADCRUMBS + TABS) ---");
        BreadcrumbNavigator nav = new BreadcrumbNavigator("Catálogo", "Reservas", "Mi cuenta");

        nav.mostrarRuta();                              // Inicio
        nav.cambiarPestana("Catálogo");
        nav.mostrarRuta();                              // Inicio > Catálogo
        nav.entrarSubcategoria("Servidor Principal");
        nav.mostrarRuta();                              // Inicio > Catálogo > Servidor Principal
        nav.cambiarPestana("Reservas");
        nav.entrarSubcategoria("Laboratorio de Redes");
        nav.mostrarRuta();                              // Inicio > Reservas > Laboratorio de Redes
        nav.irAInicio();
        nav.mostrarRuta();                              // Inicio
        System.out.println();
    }

    private static void casoContenido() {
        System.out.println("--- 2. PATRÓN DE CONTENIDO Y ESTRUCTURA (PROGRESSIVE DISCLOSURE) ---");
        TarjetaElemento tarjeta = new TarjetaElemento(
                "Servidor Principal", "Activo", "192.168.1.10", 8080, 4, 16);

        tarjeta.setVerAvanzado(false);
        System.out.println(tarjeta.renderizar());
        System.out.println("[Usuario solicita ver detalles avanzados...]");
        tarjeta.setVerAvanzado(true);
        System.out.println(tarjeta.renderizar());
        System.out.println();
    }

    private static void casoForgivingFormat() {
        System.out.println("--- 3. PATRÓN DE ENTRADA DE DATOS (FORGIVING FORMAT) ---");
        ValidadorForgiving validador = new ValidadorForgiving();

        String[] entradas = {"+57 (310) 999-8877", "+57 (300) 123-4567", "310 999 8877"};
        for (String entrada : entradas) {
            System.out.println("Entrada del usuario: \"" + entrada + "\"");
            System.out.println("Teléfono normalizado sin error: \""
                    + validador.normalizarTelefono(entrada) + "\"");
        }

        String fecha = "05/03/2026";
        System.out.println("Fecha ingresada: \"" + fecha + "\" -> "
                + validador.normalizarFecha(fecha).orElse("(se solicitará nuevamente)"));
        System.out.println();
    }

    private static void casoCompletenessMeter() {
        System.out.println("--- 4. PATRÓN DE FEEDBACK Y ESTADO (COMPLETENESS METER) ---");
        GestorSesionUI gestor = new GestorSesionUI(
                "Laboratorio", "Teléfono", "Correo de contacto");

        gestor.mostrarProgreso();                                   // 0%
        gestor.setCampo("Laboratorio", "Laboratorio de Redes");
        gestor.mostrarProgreso();                                   // 33%
        gestor.setCampo("Teléfono", "3109998877");
        gestor.mostrarProgreso();                                   // 67%
        System.out.println("Usuario ingresa correo: \"estudiante@udistrital.edu.co\"");
        gestor.setCampo("Correo de contacto", "estudiante@udistrital.edu.co");
        gestor.mostrarProgreso();                                   // 100%
        System.out.println();
    }

    private static void casoLazyRegistrationYToast() {
        System.out.println("--- 5. LAZY REGISTRATION + TOAST ---");
        GestorSesionUI gestor = new GestorSesionUI(
                "Laboratorio", "Teléfono", "Correo de contacto");
        gestor.setCampo("Laboratorio", "Laboratorio de Redes");
        gestor.setCampo("Teléfono", "3109998877");
        gestor.setCampo("Correo de contacto", "estudiante@udistrital.edu.co");

        System.out.println("Usuario intenta guardar sin haber iniciado sesión...");
        gestor.guardarReserva();                    // Borrador + solicitud de registro

        gestor.completarRegistroRapido();           // usuarioActivo = true
        gestor.guardarReserva();                    // Toast de éxito
    }
}
