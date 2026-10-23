package content;

/**
 * Patrón de Contenido y Estructura: Progressive Disclosure (Acordeón).
 * Por defecto solo se muestra nombre y estado; los detalles técnicos
 * permanecen ocultos hasta que el usuario los despliega.
 */
public class TarjetaElemento {

    private final String nombre;
    private final String estado;
    private final String ip;
    private final int puerto;
    private final int ramUsadaGb;
    private final int ramTotalGb;
    private boolean verAvanzado = false;

    public TarjetaElemento(String nombre, String estado, String ip,
                           int puerto, int ramUsadaGb, int ramTotalGb) {
        this.nombre = nombre;
        this.estado = estado;
        this.ip = ip;
        this.puerto = puerto;
        this.ramUsadaGb = ramUsadaGb;
        this.ramTotalGb = ramTotalGb;
    }

    public void setVerAvanzado(boolean verAvanzado) {
        this.verAvanzado = verAvanzado;
    }

    /** Despliega o contrae el acordeón. */
    public void alternarAvanzado() {
        this.verAvanzado = !this.verAvanzado;
    }

    public boolean isVerAvanzado() {
        return verAvanzado;
    }

    /** Devuelve la vista según el estado del acordeón. */
    public String renderizar() {
        if (!verAvanzado) {
            return "[Vista Resumen] " + nombre + " · Estado: " + estado;
        }
        return "[Acordeón Desplegado] IP: " + ip
                + " | Puerto: " + puerto
                + " | RAM: " + ramUsadaGb + "GB/" + ramTotalGb + "GB";
    }
}
