import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java_cup.runtime.Symbol;

public class Main {

    public static void main(String[] args) {

        String rutaEntrada = "pruebas/Prueba.txt";
        String rutaTokens = "salida/tokens.txt";
        String rutaErroresLexicos = "salida/errores_lexicos.txt";
        String rutaErroresSintacticos = "salida/errores_sintacticos.txt";

        boolean huboErroresLexicos = analizarLexico(
            rutaEntrada,
            rutaTokens,
            rutaErroresLexicos
        );

        boolean huboErroresSintacticos = analizarSintactico(
            rutaEntrada,
            rutaErroresSintacticos
        );

        boolean aceptado = !huboErroresLexicos && !huboErroresSintacticos;

        System.out.println();
        if (aceptado) {
            System.out.println("El programa pertenece a la gramatica.");
        } else {
            System.out.println("El programa NO pertenece a la gramatica.");
            if (huboErroresLexicos) {
                System.out.println("Ver errores lexicos en: " + rutaErroresLexicos);
            }
            if (huboErroresSintacticos) {
                System.out.println("Ver errores sintacticos en: " + rutaErroresSintacticos);
            }
        }
    }


    /* ==========================================
       ANALISIS LEXICO
       ========================================== */

    private static boolean analizarLexico(
        String rutaEntrada,
        String rutaTokens,
        String rutaErrores
    ) {

        boolean huboErrores = false;

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

                    huboErrores = true;

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

                huboErrores = true;

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

        return huboErrores;
    }


    /* ==========================================
       ANALISIS SINTACTICO
       ========================================== */

    private static boolean analizarSintactico(
        String rutaEntrada,
        String rutaErroresSintacticos
    ) {

        Parser parser = null;

        try (
            FileReader archivo = new FileReader(
                rutaEntrada,
                StandardCharsets.UTF_8
            )
        ) {

            Lexer lexer = new Lexer(archivo);

            parser = new Parser(lexer);

            parser.parse();

            System.out.println();
            System.out.println(
                "Analisis sintactico finalizado."
            );

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                "El analisis sintactico se interrumpio: " + e.getMessage()
            );
        }

        boolean huboErrores =
            parser != null && parser.huboErrorSintactico();

        if (huboErrores) {
            try (FileWriter escritorErrores = new FileWriter(
                    rutaErroresSintacticos, StandardCharsets.UTF_8)) {

                for (String error : parser.erroresSintacticos) {
                    escritorErrores.write(error);
                    escritorErrores.write("\n");
                }

            } catch (IOException e) {
                System.out.println(
                    "Error al escribir errores sintacticos: " + e.getMessage()
                );
            }
        }

        return huboErrores;
    }
}