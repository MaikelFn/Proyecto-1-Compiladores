import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TablaSimbolos {

    private final List<Ambito> ambitos = new ArrayList<>();
    private final Deque<Ambito> pilaAmbitos = new ArrayDeque<>();
    private int contadorBloques;

    public TablaSimbolos() {
        Ambito global = new Ambito("Global", "Global", 0);
        ambitos.add(global);
        pilaAmbitos.push(global);
    }

    public void abrirAmbito(String nombre) {
        Ambito padre = pilaAmbitos.peek();
        String ruta = padre.getRuta() + " / " + nombre;
        Ambito ambito = new Ambito(nombre, ruta, padre.getNivel() + 1);
        ambitos.add(ambito);
        pilaAmbitos.push(ambito);
    }

    public void abrirAmbitoBloque() {
        contadorBloques++;
        abrirAmbito("Bloque " + contadorBloques);
    }

    public void cerrarAmbito() {
        if (pilaAmbitos.size() == 1) {
            throw new IllegalStateException("No se puede cerrar el ambito global.");
        }
        pilaAmbitos.pop();
    }

    public boolean agregarSimbolo(String nombre, String tipo, int linea, String categoria) {
        return pilaAmbitos.peek().agregarSimbolo(nombre, tipo, linea, categoria);
    }

    public List<Ambito> getAmbitos() {
        return Collections.unmodifiableList(new ArrayList<>(ambitos));
    }

    public static final class Ambito {
        private final String nombre;
        private final String ruta;
        private final int nivel;
        private final List<Simbolo> simbolos = new ArrayList<>();
        private final Set<String> nombresSimbolos = new HashSet<>();

        private Ambito(String nombre, String ruta, int nivel) {
            this.nombre = nombre;
            this.ruta = ruta;
            this.nivel = nivel;
        }

        private boolean agregarSimbolo(String nombre, String tipo, int linea, String categoria) {
            if (!nombresSimbolos.add(nombre)) {
                return false;
            }
            simbolos.add(new Simbolo(nombre, tipo, linea, categoria));
            return true;
        }

        public String getNombre() {
            return nombre;
        }

        public String getRuta() {
            return ruta;
        }

        public int getNivel() {
            return nivel;
        }

        public List<Simbolo> getSimbolos() {
            return Collections.unmodifiableList(simbolos);
        }
    }

    public static final class Simbolo {
        private final String nombre;
        private final String tipo;
        private final int linea;
        private final String categoria;

        private Simbolo(String nombre, String tipo, int linea, String categoria) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.linea = linea;
            this.categoria = categoria;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public int getLinea() {
            return linea;
        }

        public String getCategoria() {
            return categoria;
        }
    }
}
