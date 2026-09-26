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

            FileWriter escritorTokens = new FileWriter(
                rutaTokens,
                StandardCharsets.UTF_8
            );

            FileWriter escritorErrores = new FileWriter(
                rutaErrores,
                StandardCharsets.UTF_8
            )
        ) {

            Lexer lexer = new Lexer(archivo);

            while (true) {

                String token = lexer.yylex();

                while (lexer.hayErroresPendientes()) {

                    String error = lexer.obtenerSiguienteError();

                    System.out.println(error);
                    escritorErrores.write(error);
                    escritorErrores.write("\n");
                }

                if (token == null) {
                    break;
                }

                String lexema = lexer.yytext();
                int linea = lexer.getLinea();

                String salida =
                    "Token: " + token
                    + " | Lexema: " + lexema
                    + " | Linea: " + linea;

                System.out.println(salida);
                escritorTokens.write(salida);
                escritorTokens.write("\n");
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