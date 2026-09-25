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
%type String

%{
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

/*
 * No permite:
 * - comillas dobles
 * - comillas simples
 * - simbolos especiales reservados
 * - saltos de linea
 */

Caracter_String = [^\"'»¿?є:эʃʅͰλθΣ|¡!\r\n]


/* ==========================================
   2.6 LITERAL CHAR
   ========================================== */

/*
 * Un char contiene exactamente un caracter
 * entre comillas simples.
 *
 * Ejemplos:
 * 'a'
 * '5'
 * 'Z'
 */

Lit_Char = "'" {Caracter_String} "'"


/* ==========================================
   2.7 LITERAL STRING
   ========================================== */

/*
 * Un string utiliza comillas dobles.
 * Puede contener cero o mas caracteres.
 *
 * Ejemplos:
 * ""
 * "a"
 * "Hola"
 * "12345"
 */

Lit_String = "\"" {Caracter_String}* "\""


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


/* Bloque de codigo */

"¿:" {
    return "BLOQUE_INI";
}

":?" {
    return "BLOQUE_FIN";
}


/* Parentesis especiales */

"є:" {
    return "PAR_INI";
}

":э" {
    return "PAR_FIN";
}


/* Corchetes especiales */

"ʃ:" {
    return "COR_INI";
}

":ʅ" {
    return "COR_FIN";
}


/* Final de expresion */

"»" {
    return "FIN_EXPR";
}


/* Coma */

"," {
    return "COMA";
}


/* ==========================================
   3.8 LITERALES
   ========================================== */

/*
 * IMPORTANTE:
 * Los literales booleanos deben aparecer
 * antes del identificador, ya que true y
 * false tambien cumplen con la forma de Id.
 */

{Lit_Bool} {
    return "LIT_BOOL";
}


/*
 * CHAR:
 * exactamente un caracter entre comillas simples.
 */

{Lit_Char} {
    return "LIT_CHAR";
}


/*
 * STRING:
 * cero o mas caracteres entre comillas dobles.
 */

{Lit_String} {
    return "LIT_STRING";
}


/*
 * FLOAT debe evaluarse antes que INT.
 */

{Lit_Float} {
    return "LIT_FLOAT";
}


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
   4. ESPACIOS EN BLANCO
   ========================================== */

[ \t\r\n]+ {
    /* Se ignoran los espacios en blanco */
}


/* ==========================================
   5. ERRORES LEXICOS
   ========================================== */

[^] {
    System.out.println(
        "Error lexico: caracter no reconocido '" + yytext() + "'"
    );
}