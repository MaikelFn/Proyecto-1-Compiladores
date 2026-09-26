/* ==========================================
   PROYECTO 1 - COMPILADORES
   ANALIZADOR LEXICO
   ========================================== */

%%

/* ==========================================
   1. CONFIGURACION DEL ANALIZADOR
   ========================================== */

%public
%class Lexer
%unicode
%line
%type String

%xstate COMENTARIO_MULTI

%{
    /*
     * Guarda la linea donde inicia
     * un comentario multilinea.
     */
    private int lineaInicioComentario;

    /*
     * Devuelve la linea actual.
     * JFlex empieza a contar desde 0.
     */
    public int getLinea() {
        return yyline + 1;
    }
%}


/* ==========================================
   2. DEFINICIONES REGULARES
   ========================================== */


/* ==========================================
   2.1 LETRAS, DIGITOS E IDENTIFICADORES
   ========================================== */

Letra = [a-zA-Z]

Digito = [0-9]

Digito_No_Cero = [1-9]

Caracter = {Letra}|{Digito}

Id = {Letra}{Caracter}*


/* ==========================================
   2.2 LITERALES ENTEROS
   ========================================== */

Lit_Int = {Digito}+


/* ==========================================
   2.3 LITERALES FLOTANTES
   ========================================== */

Parte_Entera = 0|{Digito_No_Cero}{Digito}*

Parte_Decimal = {Digito}*{Digito_No_Cero}

Lit_Float = {Parte_Entera}"."{Parte_Decimal}


/* ==========================================
   2.4 LITERALES BOOLEANOS
   ========================================== */

Lit_Bool = "true"|"false"


/* ==========================================
   2.5 CARACTERES PERMITIDOS EN CHAR Y STRING
   ========================================== */

Caracter_String = [^\"'»¿?є:эʃʅͰλθΣ|¡!\r\n]


/* ==========================================
   2.6 LITERAL CHAR
   ========================================== */

/*
 * Char valido:
 * exactamente un caracter entre comillas simples.
 */

Lit_Char = "'" {Caracter_String} "'"


/*
 * Char invalido:
 * cero o varios caracteres entre comillas simples.
 *
 * La regla Lit_Char aparece antes en las reglas
 * lexicas, por lo que 'a' se reconoce correctamente.
 *
 * Ejemplos invalidos:
 * ''
 * 'ab'
 * 'Hola'
 */

Lit_Char_Invalido = "'" {Caracter_String}* "'"


/* ==========================================
   2.7 LITERAL STRING
   ========================================== */

/*
 * Un string utiliza comillas dobles.
 * Puede contener cero o mas caracteres.
 */

Lit_String = "\"" {Caracter_String}* "\""


/* ==========================================
   2.8 COMENTARIOS
   ========================================== */

Comentario_Linea = \|[^\r\n]*


%%


/* ==========================================
   3. REGLAS LEXICAS
   ========================================== */


/* ==========================================
   3.1 TIPOS DE DATOS
   ========================================== */

"int" {
    return "INT";
}

"float" {
    return "FLOAT";
}

"bool" {
    return "BOOL";
}

"char" {
    return "CHAR";
}

"string" {
    return "STRING";
}

"void" {
    return "VOID";
}


/* ==========================================
   3.2 PALABRAS RESERVADAS
   ========================================== */

"principal" {
    return "PRINCIPAL";
}

"if" {
    return "IF";
}

"elif" {
    return "ELIF";
}

"else" {
    return "ELSE";
}

"while" {
    return "WHILE";
}

"for" {
    return "FOR";
}

"return" {
    return "RETURN";
}

"break" {
    return "BREAK";
}

"val" {
    return "VAL";
}

"read" {
    return "READ";
}

"write" {
    return "WRITE";
}


/* ==========================================
   3.3 OPERADORES ARITMETICOS
   ========================================== */

"++" {
    return "INCREMENTO";
}

"--" {
    return "DECREMENTO";
}

"//" {
    return "DIVISION_ENTERA";
}

"+" {
    return "SUMA";
}

"-" {
    return "RESTA";
}

"*" {
    return "MULTIPLICACION";
}

"/" {
    return "DIVISION";
}

"mod" {
    return "MODULO";
}

"pot" {
    return "POTENCIA";
}


/* ==========================================
   3.4 OPERADORES RELACIONALES
   ========================================== */

"<=" {
    return "MENOR_IGUAL";
}

">=" {
    return "MAYOR_IGUAL";
}

"==" {
    return "IGUAL";
}

"!=" {
    return "DIFERENTE";
}

"<" {
    return "MENOR";
}

">" {
    return "MAYOR";
}


/* ==========================================
   3.5 OPERADORES LOGICOS
   ========================================== */

"λ" {
    return "AND";
}

"θ" {
    return "OR";
}

"Σ" {
    return "NOT";
}


/* ==========================================
   3.6 OPERADOR DE ASIGNACION
   ========================================== */

"Ͱ" {
    return "ASIGNACION";
}


/* ==========================================
   3.7 DELIMITADORES Y SIMBOLOS
   ========================================== */

"¿:" {
    return "BLOQUE_INI";
}

":?" {
    return "BLOQUE_FIN";
}

"є:" {
    return "PAR_INI";
}

":э" {
    return "PAR_FIN";
}

"ʃ:" {
    return "COR_INI";
}

":ʅ" {
    return "COR_FIN";
}

"»" {
    return "FIN_EXPR";
}

"," {
    return "COMA";
}


/* ==========================================
   3.8 LITERALES
   ========================================== */

{Lit_Bool} {
    return "LIT_BOOL";
}


/* CHAR VALIDO */

{Lit_Char} {
    return "LIT_CHAR";
}


/* CHAR INVALIDO */

{Lit_Char_Invalido} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": literal char invalido " + yytext()
        + ". Un char debe contener exactamente un caracter."
    );
}


/* STRING */

{Lit_String} {
    return "LIT_STRING";
}


/* FLOAT */

{Lit_Float} {
    return "LIT_FLOAT";
}


/* ENTERO */

{Lit_Int} {
    return "LIT_INT";
}


/* ==========================================
   3.9 IDENTIFICADORES
   ========================================== */

{Id} {
    return "ID";
}


/* ==========================================
   3.10 COMENTARIOS
   ========================================== */


/* Comentario de una linea */

{Comentario_Linea} {
    /* Ignorar */
}


/* Inicio del comentario multilinea */

"¡" {
    lineaInicioComentario = yyline + 1;
    yybegin(COMENTARIO_MULTI);
}


/* Cierre del comentario multilinea */

<COMENTARIO_MULTI> "!" {
    yybegin(YYINITIAL);
}


/* Contenido del comentario */

<COMENTARIO_MULTI> [^!\r\n]+ {
    /* Ignorar */
}


/* Saltos de linea dentro del comentario */

<COMENTARIO_MULTI> \r\n|\r|\n {
    /* Ignorar */
}


/* Comentario multilinea sin cerrar */

<COMENTARIO_MULTI> <<EOF>> {
    System.out.println(
        "Error lexico en linea " + lineaInicioComentario
        + ": comentario multilinea no fue cerrado con !"
    );

    return null;
}


/* ==========================================
   4. ESPACIOS EN BLANCO
   ========================================== */

[ \t\r\n]+ {
    /* Ignorar espacios en blanco */
}


/* ==========================================
   5. ERRORES LEXICOS
   ========================================== */

[^] {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": caracter no reconocido '" + yytext() + "'"
    );
}