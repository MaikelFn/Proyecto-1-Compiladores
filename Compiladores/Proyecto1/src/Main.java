import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {

        String rutaEntrada = "pruebas/Prueba.txt";
        String rutaTokens = "salida/tokens.txt";
        String rutaErrores = "salida/errores.txt";

        try (
            FileReader archivo = new FileReader(
                rutaEntrada,
                StandardCharsets.UTF_8
            );

            BufferedWriter escritorTokens = new BufferedWriter(
                new FileWriter(
                    rutaTokens,
                    StandardCharsets.UTF_8
                )
            );

            BufferedWriter escritorErrores = new BufferedWriter(
                new FileWriter(
                    rutaErrores,
                    StandardCharsets.UTF_8
                )
            )
        ) {

            Lexer lexer = new Lexer(archivo);

            while (true) {

                /*
                 * El lexer intenta obtener el siguiente token.
                 *
                 * Durante esta llamada pueden ocurrir errores
                 * lexicos antes de encontrar un token valido.
                 */
                String token = lexer.yylex();


                /* ==========================================
                   PROCESAR ERRORES LEXICOS
                   ========================================== */

                while (lexer.hayErroresPendientes()) {

                    String error =
                        lexer.obtenerSiguienteError();

                    // Mostrar error en consola
                    System.out.println(error);

                    // Guardar error en errores.txt
                    escritorErrores.write(error);
                    escritorErrores.newLine();
                }


                /* ==========================================
                   FIN DEL ARCHIVO
                   ========================================== */

                /*
                 * Cuando yylex() devuelve null significa
                 * que se llego al final del archivo.
                 */
                if (token == null) {
                    break;
                }


                /* ==========================================
                   PROCESAR TOKEN
                   ========================================== */

                String lexema = lexer.yytext();
                int linea = lexer.getLinea();

                String salida =
                    "Token: " + token
                    + " | Lexema: " + lexema
                    + " | Linea: " + linea;


                // Mostrar token en consola
                System.out.println(salida);


                // Guardar token en tokens.txt
                escritorTokens.write(salida);
                escritorTokens.newLine();
            }


            System.out.println();
            System.out.println("Analisis lexico finalizado.");

        } catch (IOException e) {

            System.out.println(
                "Error al procesar el archivo: "
                + e.getMessage()
            );
        }
    }
}