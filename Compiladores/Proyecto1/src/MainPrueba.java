import java.io.FileReader;

public class MainPrueba {

    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("Uso: java MainPrueba <archivo>");
            return;
        }

        LexerIntegracion lexer =
            new LexerIntegracion(new FileReader(args[0]));

        ParserIntegracion parser =
            new ParserIntegracion(lexer);

        Integer resultado =
            (Integer) parser.parse().value;

        System.out.println("Analisis exitoso");
        System.out.println("Resultado: " + resultado);
    }
}