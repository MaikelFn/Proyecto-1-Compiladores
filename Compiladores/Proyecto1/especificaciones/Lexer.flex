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
    private int lineaInicioComentario;

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

Lit_Int = 0|{Digito_No_Cero}{Digito}*

Int_Cero_Inicial = 0{Digito}+


/* ==========================================
   2.3 LITERALES FLOTANTES
   ========================================== */

Parte_Entera = 0|{Digito_No_Cero}{Digito}*

Parte_Decimal = {Digito}*{Digito_No_Cero}

Lit_Float = {Parte_Entera}"."{Parte_Decimal}


/* ==========================================
   2.4 FLOTANTES INVALIDOS
   ========================================== */

Float_Sin_Decimal = {Parte_Entera}"."

Float_Sin_Entera = "."{Digito}+

Float_Entera_Invalida = 0{Digito}+"."{Digito}+

Float_Decimal_Invalida = {Parte_Entera}"."{Digito}*"0"


/* ==========================================
   2.5 LITERALES BOOLEANOS
   ========================================== */

Lit_Bool = "true"|"false"


/* ==========================================
   2.6 CARACTERES PERMITIDOS EN CHAR Y STRING
   ========================================== */

Caracter_String = [^\"'»¿?є:эʃʅͰλθΣ|¡!\r\n]


/* ==========================================
   2.7 LITERAL CHAR
   ========================================== */

Lit_Char = "'" {Caracter_String} "'"

Lit_Char_Invalido = "'" {Caracter_String}* "'"


/* ==========================================
   2.8 LITERAL STRING
   ========================================== */

Lit_String = "\"" {Caracter_String}* "\""


/* ==========================================
   2.9 COMENTARIOS
   ========================================== */

Comentario_Linea = \|[^\r\n]*


/* ==========================================
   2.10 LITERALES SIN CERRAR
   ========================================== */

String_Sin_Cerrar = "\"" [^\"\r\n]*

Char_Sin_Cerrar = "'" [^'\r\n]*


/* ==========================================
   2.11 COMILLAS MEZCLADAS
   ========================================== */

Char_Comillas_Mezcladas = "'" [^\"\r\n]* "\""

String_Comillas_Mezcladas = "\"" [^'\r\n]* "'"


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
   3.8 LITERALES VALIDOS
   ========================================== */

{Lit_Bool} {
    return "LIT_BOOL";
}

{Lit_Char} {
    return "LIT_CHAR";
}

{Lit_String} {
    return "LIT_STRING";
}

{Lit_Float} {
    return "LIT_FLOAT";
}


/* ==========================================
   3.9 ERRORES DE FLOTANTES
   ========================================== */

{Float_Entera_Invalida} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": parte entera invalida en literal flotante "
        + yytext()
        + ". No se permiten ceros a la izquierda."
    );
}

{Float_Decimal_Invalida} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": parte decimal invalida en literal flotante "
        + yytext()
        + ". La parte decimal debe terminar en un digito distinto de cero."
    );
}

{Float_Sin_Decimal} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": literal flotante sin parte decimal "
        + yytext()
    );
}

{Float_Sin_Entera} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": literal flotante sin parte entera "
        + yytext()
    );
}


/* ==========================================
   3.10 ERRORES DE ENTEROS
   ========================================== */

{Int_Cero_Inicial} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": literal entero invalido "
        + yytext()
        + ". No se permiten ceros a la izquierda."
    );
}

{Lit_Int} {
    return "LIT_INT";
}


/* ==========================================
   3.11 CHAR INVALIDO
   ========================================== */

{Lit_Char_Invalido} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": literal char invalido "
        + yytext()
        + ". Un char debe contener exactamente un caracter."
    );
}


/* ==========================================
   3.12 COMILLAS MEZCLADAS
   ========================================== */

{Char_Comillas_Mezcladas} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": comillas mezcladas en literal "
        + yytext()
    );
}

{String_Comillas_Mezcladas} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": comillas mezcladas en literal "
        + yytext()
    );
}


/* ==========================================
   3.13 STRING Y CHAR SIN CERRAR
   ========================================== */

{String_Sin_Cerrar} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": string sin cerrar "
        + yytext()
    );
}


{Char_Sin_Cerrar} {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": char sin cerrar "
        + yytext()
    );
}


/* ==========================================
   3.14 IDENTIFICADORES
   ========================================== */

{Id} {
    return "ID";
}


/* ==========================================
   3.15 COMENTARIOS
   ========================================== */

{Comentario_Linea} {
    /* Ignorar */
}

"¡" {
    lineaInicioComentario = yyline + 1;
    yybegin(COMENTARIO_MULTI);
}

<COMENTARIO_MULTI> "!" {
    yybegin(YYINITIAL);
}

<COMENTARIO_MULTI> [^!\r\n]+ {
    /* Ignorar */
}

<COMENTARIO_MULTI> \r\n|\r|\n {
    /* Ignorar */
}

<COMENTARIO_MULTI> <<EOF>> {
    System.out.println(
        "Error lexico en linea "
        + lineaInicioComentario
        + ": comentario multilinea no fue cerrado con !"
    );

    return null;
}


/* ==========================================
   4. ESPACIOS EN BLANCO
   ========================================== */

[ \t\r\n]+ {
    /* Ignorar */
}


/* ==========================================
   5. ERROR LEXICO GENERAL
   ========================================== */

[^] {
    System.out.println(
        "Error lexico en linea " + (yyline + 1)
        + ": caracter no reconocido '"
        + yytext()
        + "'"
    );
}