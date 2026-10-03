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
        String rutaTablaSimbolos = "salida/tabla_simbolos.txt";

        boolean huboErroresLexicos = analizarLexico(
            rutaEntrada,
            rutaTokens,
            rutaErroresLexicos
        );

        boolean huboErroresSintacticos = analizarSintactico(
            rutaEntrada,
            rutaErroresSintacticos,
            rutaTablaSimbolos
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
        String rutaErroresSintacticos,
        String rutaTablaSimbolos
    ) {

        Parser parser = null;
        boolean analisisInterrumpido = false;

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

            analisisInterrumpido = true;

            System.out.println();
            System.out.println(
                "El analisis sintactico se interrumpio: "
                + e.getMessage()
            );
        }

        boolean huboErrores =
            analisisInterrumpido
            || (parser != null && parser.huboErrorSintactico());

        /* Siempre recrea el archivo para no dejar errores viejos. */
        try (
            FileWriter escritorErrores = new FileWriter(
                rutaErroresSintacticos,
                StandardCharsets.UTF_8
            )
        ) {

            if (parser != null) {
                for (String error : parser.erroresSintacticos) {
                    escritorErrores.write(error);
                    escritorErrores.write("\n");
                }
            }

        } catch (IOException e) {
            System.out.println(
                "Error al escribir errores sintacticos: "
                + e.getMessage()
            );
        }

        if (parser != null) {
            guardarTablaSimbolos(
                parser.getTablaSimbolos(),
                rutaTablaSimbolos
            );
        }

        return huboErrores;
    }


    /* ==========================================
       TABLA DE SIMBOLOS
       ========================================== */

    private static void guardarTablaSimbolos(
        TablaSimbolos tabla,
        String rutaSalida
    ) {

        try (
            FileWriter escritor = new FileWriter(
                rutaSalida,
                StandardCharsets.UTF_8
            )
        ) {

            for (
                TablaSimbolos.Ambito ambito
                : tabla.getAmbitos()
            ) {

                escritor.write(
                    "=== TABLA DE SIMBOLOS: "
                    + ambito.getRuta()
                    + " ===\n"
                );

                escritor.write(
                    String.format(
                        "%-20s %-12s %-15s %s%n",
                        "Nombre",
                        "Tipo",
                        "Categoria",
                        "Linea"
                    )
                );

                escritor.write(
                    String.format(
                        "%-20s %-12s %-15s %s%n",
                        "------",
                        "----",
                        "---------",
                        "-----"
                    )
                );

                if (ambito.getSimbolos().isEmpty()) {
                    escritor.write("(sin simbolos)\n");
                } else {

                    for (
                        TablaSimbolos.Simbolo simbolo
                        : ambito.getSimbolos()
                    ) {

                        escritor.write(
                            String.format(
                                "%-20s %-12s %-15s %d%n",
                                simbolo.getNombre(),
                                simbolo.getTipo(),
                                simbolo.getCategoria(),
                                simbolo.getLinea()
                            )
                        );
                    }
                }

                escritor.write("\n");
            }

            System.out.println(
                "Tabla de simbolos guardada en: "
                + rutaSalida
            );

        } catch (IOException e) {

            System.out.println(
                "Error al guardar la tabla de simbolos: "
                + e.getMessage()
            );
        }
    }

}