package navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Patrón de Navegación: Tabs + Breadcrumbs.
 * Mantiene la pestaña activa y los subniveles, y construye la ruta
 * jerárquica: Inicio > Sección > Subsección.
 */
public class BreadcrumbNavigator {

    private static final String RAIZ = "Inicio";
    private static final String SEPARADOR = " > ";

    private final List<String> pestanas;
    private final List<String> subniveles = new ArrayList<>();
    private String pestanaActiva;

    public BreadcrumbNavigator(String... pestanasDisponibles) {
        this.pestanas = new ArrayList<>(Arrays.asList(pestanasDisponibles));
    }

    /** Cambia de pestaña; reinicia los subniveles. Devuelve false si la pestaña no existe. */
    public boolean cambiarPestana(String pestana) {
        if (!pestanas.contains(pestana)) {
            return false;
        }
        pestanaActiva = pestana;
        subniveles.clear();
        return true;
    }

    /** Entra a una subcategoría dentro de la pestaña activa. */
    public void entrarSubcategoria(String subcategoria) {
        if (pestanaActiva == null) {
            return; // No hay sección activa a la cual anexar
        }
        subniveles.add(subcategoria);
    }

    /** Sube un nivel en la jerarquía (un paso atrás). */
    public void subirNivel() {
        if (!subniveles.isEmpty()) {
            subniveles.remove(subniveles.size() - 1);
        } else {
            pestanaActiva = null;
        }
    }

    /** Vuelve directamente al inicio en un solo paso. */
    public void irAInicio() {
        pestanaActiva = null;
        subniveles.clear();
    }

    /** Construye la cadena de ruta actual. */
    public String getRuta() {
        StringBuilder ruta = new StringBuilder(RAIZ);
        if (pestanaActiva != null) {
            ruta.append(SEPARADOR).append(pestanaActiva);
            for (String nivel : subniveles) {
                ruta.append(SEPARADOR).append(nivel);
            }
        }
        return ruta.toString();
    }

    public void mostrarRuta() {
        System.out.println("Ruta actual: " + getRuta());
    }

    public String getPestanaActiva() {
        return pestanaActiva;
    }
}
