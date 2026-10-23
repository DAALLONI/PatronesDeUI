package state;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Patrón de Feedback y Estado + Lazy Registration.
 *  - Completeness Meter: porcentaje de avance del formulario (0/33/67/100 %).
 *  - Lazy Registration: se puede armar la reserva sin sesión; el borrador
 *    temporal se guarda solo al intentar finalizar sin autenticarse.
 *  - Toast: notificación breve al confirmar el guardado.
 *
 * Sin Dark Patterns: cancelar y cerrar sesión son acciones de un solo paso
 * con mensajes neutros.
 */
public class GestorSesionUI {

    private final Map<String, String> campos = new LinkedHashMap<>();
    private Map<String, String> borradorTemporal = null;
    private boolean usuarioActivo = false;

    /** Los campos requeridos del formulario (nombre visible). */
    public GestorSesionUI(String... nombresCampos) {
        for (String nombre : nombresCampos) {
            campos.put(nombre, "");
        }
    }

    // ---------- Completeness Meter ----------

    public void setCampo(String nombre, String valor) {
        if (campos.containsKey(nombre)) {
            campos.put(nombre, valor == null ? "" : valor.trim());
        }
    }

    public int camposListos() {
        int listos = 0;
        for (String valor : campos.values()) {
            if (!valor.isEmpty()) {
                listos++;
            }
        }
        return listos;
    }

    public int calcularPorcentaje() {
        if (campos.isEmpty()) {
            return 0;
        }
        return (int) Math.round(camposListos() * 100.0 / campos.size());
    }

    public List<String> camposFaltantes() {
        List<String> faltantes = new ArrayList<>();
        for (Map.Entry<String, String> e : campos.entrySet()) {
            if (e.getValue().isEmpty()) {
                faltantes.add(e.getKey());
            }
        }
        return faltantes;
    }

    public void mostrarProgreso() {
        int pct = calcularPorcentaje();
        if (pct == 100) {
            System.out.println("[Progreso: 100%] ¡Formulario completo!");
        } else {
            System.out.println("[Progreso: " + pct + "%] Campos listos: "
                    + camposListos() + " de " + campos.size()
                    + " (Falta: " + String.join(", ", camposFaltantes()) + ")");
        }
    }

    // ---------- Lazy Registration ----------

    public boolean isUsuarioActivo() {
        return usuarioActivo;
    }

    public boolean hayBorrador() {
        return borradorTemporal != null;
    }

    /**
     * Intenta guardar la reserva final.
     * @return true si la reserva quedó guardada.
     */
    public boolean guardarReserva() {
        if (!usuarioActivo) {
            borradorTemporal = new LinkedHashMap<>(campos);
            System.out.println("[Lazy Registration] Avance guardado en borrador temporal. "
                    + "Por favor cree su cuenta para finalizar.");
            return false;
        }

        if (calcularPorcentaje() < 100) {
            System.out.println("[Aviso] Aún puede completar: "
                    + String.join(", ", camposFaltantes()) + ".");
            return false;
        }

        borradorTemporal = null;
        mostrarToast("Reserva guardada exitosamente.");
        return true;
    }

    /** Registro rápido: activa la sesión y recupera el borrador si existe. */
    public void completarRegistroRapido() {
        usuarioActivo = true;
        if (borradorTemporal != null) {
            campos.putAll(borradorTemporal);
        }
        System.out.println("Usuario completa registro rápido -> Sesión activa = true");
    }

    // ---------- Toast ----------

    private void mostrarToast(String mensaje) {
        System.out.println("[Toast de Estado] " + mensaje);
    }

    // ---------- Salidas directas, sin Dark Patterns ----------

    /** Cancelar en un solo paso, mensaje neutro (sin Confirmshaming / Roach Motel). */
    public void cancelarReserva() {
        campos.replaceAll((k, v) -> "");
        borradorTemporal = null;
        mostrarToast("Reserva cancelada.");
    }

    /** Cerrar sesión en un solo paso, mensaje neutro. */
    public void cerrarSesion() {
        usuarioActivo = false;
        mostrarToast("Sesión cerrada.");
    }
}
