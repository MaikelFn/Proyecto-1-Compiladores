import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;
import java.io.IOException;

/**
 * Adaptador temporal de pruebas: NO forma parte de la entrega final.
 * Conecta el Lexer.flex original (que retorna String) con CUP (que
 * exige un java_cup.runtime.Scanner que retorne Symbol). No modifica
 * Lexer.flex ni ningun archivo original del equipo: solo lo envuelve.
 *
 * Tambien reenvia al flujo de salida los errores lexicos que el Lexer
 * va encolando (via hayErroresPendientes/obtenerSiguienteError), para
 * que en una sola corrida se vean errores lexicos Y sintacticos.
 */
public class AdaptadorEscaner implements Scanner {

    private final Lexer lexer;

    public ScannerAdapter(Lexer lexer) {
        this.lexer = lexer;
    }

    private int mapear(String token) {
        switch (token) {
            case "INT": return sym.INT;
            case "FLOAT": return sym.FLOAT;
            case "BOOL": return sym.BOOL;
            case "CHAR": return sym.CHAR;
            case "STRING": return sym.STRING;
            case "VOID": return sym.VOID;
            case "PRINCIPAL": return sym.PRINCIPAL;
            case "IF": return sym.IF;
            case "ELIF": return sym.ELIF;
            case "ELSE": return sym.ELSE;
            case "WHILE": return sym.WHILE;
            case "FOR": return sym.FOR;
            case "RETURN": return sym.RETURN;
            case "BREAK": return sym.BREAK;
            case "VAL": return sym.VAL;
            case "READ": return sym.READ;
            case "WRITE": return sym.WRITE;
            case "INCREMENTO": return sym.INCREMENTO;
            case "DECREMENTO": return sym.DECREMENTO;
            case "DIVISION_ENTERA": return sym.DIVISION_ENTERA;
            case "SUMA": return sym.SUMA;
            case "RESTA": return sym.RESTA;
            case "MULTIPLICACION": return sym.MULTIPLICACION;
            case "DIVISION": return sym.DIVISION;
            case "MODULO": return sym.MODULO;
            case "POTENCIA": return sym.POTENCIA;
            case "MENOR_IGUAL": return sym.MENOR_IGUAL;
            case "MAYOR_IGUAL": return sym.MAYOR_IGUAL;
            case "IGUAL": return sym.IGUAL;
            case "DIFERENTE": return sym.DIFERENTE;
            case "MENOR": return sym.MENOR;
            case "MAYOR": return sym.MAYOR;
            case "AND": return sym.AND;
            case "OR": return sym.OR;
            case "NOT": return sym.NOT;
            case "ASIGNACION": return sym.ASIGNACION;
            case "BLOQUE_INI": return sym.BLOQUE_INI;
            case "BLOQUE_FIN": return sym.BLOQUE_FIN;
            case "PAR_INI": return sym.PAR_INI;
            case "PAR_FIN": return sym.PAR_FIN;
            case "COR_INI": return sym.COR_INI;
            case "COR_FIN": return sym.COR_FIN;
            case "FIN_EXPR": return sym.FIN_EXPR;
            case "COMA": return sym.COMA;
            case "LIT_BOOL": return sym.LIT_BOOL;
            case "LIT_CHAR": return sym.LIT_CHAR;
            case "LIT_STRING": return sym.LIT_STRING;
            case "LIT_FLOAT": return sym.LIT_FLOAT;
            case "LIT_INT": return sym.LIT_INT;
            case "ID": return sym.ID;
            default:
                throw new IllegalStateException(
                    "Token del Lexer sin mapeo en ScannerAdapter: " + token
                );
        }
    }

    private void volcarErroresLexicosPendientes() {
        while (lexer.hayErroresPendientes()) {
            System.out.println(lexer.obtenerSiguienteError());
        }
    }

    @Override
    public Symbol next_token() throws IOException {
        String token = lexer.yylex();

        volcarErroresLexicosPendientes();

        if (token == null) {
            return new Symbol(sym.EOF, lexer.getLinea(), lexer.getLinea());
        }

        int codigo = mapear(token);
        String lexema = lexer.yytext();
        int linea = lexer.getLinea();

        return new Symbol(codigo, linea, linea, lexema);
    }
}
