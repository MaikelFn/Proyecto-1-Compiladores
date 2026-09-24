
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainPrueba {

    public static void main(String[] args) {

        try (FileReader archivo = new FileReader(
                "pruebas/Prueba.txt",
                StandardCharsets.UTF_8)) {

            // Crear el analizador lexico.
            Lexer lexer = new Lexer(archivo);

            String token;

            // Recorrer todos los tokens del archivo.
            while ((token = lexer.yylex()) != null) {

                String lexema = lexer.yytext();

                // Obtener el valor Unicode de cada caracter.
                StringBuilder unicode = new StringBuilder();

                lexema.codePoints().forEach(c ->
                    unicode.append(
                        String.format("U+%04X ", c)
                    )
                );

                // Mostrar la informacion del token.
                System.out.println(
                    "Token: " + token
                    + " | Lexema: " + lexema
                    + " | Unicode: " + unicode
                    + " | Linea: " + lexer.getLinea()
                );
            }

        } catch (IOException e) {
            System.out.println(
                "Error al leer el archivo: " + e.getMessage()
            );
        }
    }
}