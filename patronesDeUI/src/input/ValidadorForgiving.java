package input;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Patrón de Entrada de Datos: Forgiving Format.
 * Acepta la entrada en casi cualquier formato y la normaliza
 * internamente, sin lanzar excepciones ni culpar al usuario.
 */
public class ValidadorForgiving {

    private static final Pattern NO_DIGITOS = Pattern.compile("\\D");

    // dd/mm/aaaa, dd-mm-aaaa, dd.mm.aaaa, dd mm aaaa (con espacios opcionales)
    private static final Pattern FECHA_DMY =
            Pattern.compile("^\\s*(\\d{1,2})\\s*[/\\-. ]\\s*(\\d{1,2})\\s*[/\\-. ]\\s*(\\d{4})\\s*$");

    // aaaa/mm/dd, aaaa-mm-dd, aaaa.mm.dd
    private static final Pattern FECHA_YMD =
            Pattern.compile("^\\s*(\\d{4})\\s*[/\\-. ]\\s*(\\d{1,2})\\s*[/\\-. ]\\s*(\\d{1,2})\\s*$");

    /**
     * Normaliza un teléfono colombiano: elimina espacios, paréntesis, guiones,
     * puntos y el prefijo +57 / 0057 / 57. Nunca lanza excepción.
     */
    public String normalizarTelefono(String entrada) {
        if (entrada == null) {
            return "";
        }
        String digitos = NO_DIGITOS.matcher(entrada).replaceAll("");

        if (digitos.startsWith("0057") && digitos.length() == 14) {
            digitos = digitos.substring(4);
        } else if (digitos.startsWith("57") && digitos.length() == 12) {
            digitos = digitos.substring(2);
        }
        return digitos;
    }

    /**
     * Normaliza una fecha a formato ISO (aaaa-MM-dd). Si no se reconoce el
     * formato devuelve Optional.empty() para que la interfaz pueda pedirla
     * de nuevo con un mensaje neutro, sin lanzar excepciones.
     */
    public Optional<String> normalizarFecha(String entrada) {
        if (entrada == null) {
            return Optional.empty();
        }

        Matcher dmy = FECHA_DMY.matcher(entrada);
        if (dmy.matches()) {
            return formatear(dmy.group(3), dmy.group(2), dmy.group(1));
        }

        Matcher ymd = FECHA_YMD.matcher(entrada);
        if (ymd.matches()) {
            return formatear(ymd.group(1), ymd.group(2), ymd.group(3));
        }
        return Optional.empty();
    }

    private Optional<String> formatear(String anio, String mes, String dia) {
        int m = Integer.parseInt(mes);
        int d = Integer.parseInt(dia);
        if (m < 1 || m > 12 || d < 1 || d > 31) {
            return Optional.empty();
        }
        return Optional.of(String.format("%s-%02d-%02d", anio, m, d));
    }
}
