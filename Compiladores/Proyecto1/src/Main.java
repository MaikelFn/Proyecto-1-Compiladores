import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java_cup.runtime.Symbol;

public class Main {

    public static void main(String[] args) {

        String rutaEntrada = "pruebas/Prueba.txt";
        String rutaTokens = "salida/tokens.txt";
        String rutaErrores = "salida/errores.txt";

        analizarLexico(
            rutaEntrada,
            rutaTokens,
            rutaErrores
        );

        analizarSintactico(
            rutaEntrada
        );
    }


    /* ==========================================
       ANALISIS LEXICO
       ========================================== */

    private static void analizarLexico(
        String rutaEntrada,
        String rutaTokens,
        String rutaErrores
    ) {

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

                Symbol token = lexer.next_token();

                while (lexer.hayErroresPendientes()) {

                    String error =
                        lexer.obtenerSiguienteError();

                    System.out.println(error);

                    escritorErrores.write(error);
                    escritorErrores.write("\n");
                }


                if (token.sym == sym.EOF) {
                    break;
                }


                String nombreToken =
                    sym.terminalNames[token.sym];

                String lexema =
                    token.value != null
                    ? token.value.toString()
                    : "";

                int linea = token.left;


                String salida =
                    "Token: " + nombreToken
                    + " | Lexema: " + lexema
                    + " | Linea: " + linea;


                System.out.println(salida);

                escritorTokens.write(salida);
                escritorTokens.write("\n");
            }


            /*
             * Por seguridad, revisamos si quedó
             * algún error pendiente al llegar al EOF.
             */
            while (lexer.hayErroresPendientes()) {

                String error =
                    lexer.obtenerSiguienteError();

                System.out.println(error);

                escritorErrores.write(error);
                escritorErrores.write("\n");
            }


            System.out.println();
            System.out.println(
                "Analisis lexico finalizado."
            );


        } catch (Exception e) {

            System.out.println(
                "Error durante el analisis lexico: "
                + e.getMessage()
            );
        }
    }


    /* ==========================================
       ANALISIS SINTACTICO
       ========================================== */

    private static void analizarSintactico(
        String rutaEntrada
    ) {

        try (
            FileReader archivo = new FileReader(
                rutaEntrada,
                StandardCharsets.UTF_8
            )
        ) {

            Lexer lexer = new Lexer(archivo);

            Parser parser = new Parser(lexer);

            parser.parse();


            System.out.println();
            System.out.println(
                "Analisis sintactico finalizado."
            );

            System.out.println(
                "El programa pertenece a la gramatica."
            );


        } catch (Exception e) {

            System.out.println();
            System.out.println(
                "El programa no pertenece a la gramatica."
            );

            System.out.println(
                "Detalle: " + e.getMessage()
            );
        }
    }
}