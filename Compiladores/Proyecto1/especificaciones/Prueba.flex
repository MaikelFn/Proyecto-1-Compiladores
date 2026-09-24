import java_cup.runtime.Symbol;
%%
%class LexerIntegracion
%unicode
%cup
%line
%column
%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
%%
[0-9]+     { return symbol(sym.NUMERO, Integer.valueOf(yytext())); }
"+"        { return symbol(sym.MAS); }
";"        { return symbol(sym.PUNTOCOMA); }
[ \t\r\n]+ { /* ignorar espacios */ }
.          { System.err.println("Error lexico: " + yytext()); }
