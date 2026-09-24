
import java.io.FileReader;
import java.io.IOException;

public class MainPrueba {

    public static void main(String[] args) {

        try {
            // Leer el archivo fuente.
            FileReader archivo = new FileReader("pruebas/Prueba.txt");

            // Crear el analizador léxico.
            Lexer lexer = new Lexer(archivo);

            String token;

            // Obtener los tokens hasta llegar al final.
            while ((token = lexer.yylex()) != null) {

                System.out.println(
                    "Token: " + token
                    + " | Lexema: " + lexer.yytext()
                    + " | Linea: " + lexer.getLinea()
                );
            }

            archivo.close();

        } catch (IOException e) {
            System.out.println(
                "Error al leer el archivo: " + e.getMessage()
            );
        }
    }
}