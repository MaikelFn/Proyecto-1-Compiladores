
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
%column
%type String
%{
    public int getLinea() {
        return yyline + 1;
    }
%}

/* ==========================================
   2. DEFINICIONES REGULARES
   ========================================== */

Letra = [a-zA-Z]
Digito = [0-9]
Digito_No_Cero = [1-9]

Caracter = {Letra}|{Digito}

Id = {Letra}{Caracter}*

Lit_Int = {Digito}+

Parte_Entera = 0|{Digito_No_Cero}{Digito}*

Parte_Decimal = {Digito}*{Digito_No_Cero}

Lit_Float = {Parte_Entera}"."{Parte_Decimal}

Lit_Bool = "true"|"false"

%%

/* ==========================================
   3. REGLAS LEXICAS
   ========================================== */

/* ==========================================
    3.1 TIPOS DE DATOS
    ========================================== */

"int"       { return "INT"; }
"float"     { return "FLOAT"; }
"bool"      { return "BOOL"; }
"char"      { return "CHAR"; }
"string"    { return "STRING"; }
"void"      { return "VOID"; }

/* ==========================================
    3.2 PALABRAS RESERVADAS
    ========================================== */

"principal" { return "PRINCIPAL"; }

"if"        { return "IF"; }
"elif"      { return "ELIF"; }
"else"      { return "ELSE"; }

"while"     { return "WHILE"; }
"for"       { return "FOR"; }

"return"    { return "RETURN"; }
"break"     { return "BREAK"; }

"val"       { return "VAL"; }

"read"      { return "READ"; }
"write"     { return "WRITE"; }

{Lit_Bool} {
    return "LIT_BOOL";
}

{Lit_Float} {
    return "LIT_FLOAT";
}

{Lit_Int} {
    return "LIT_INT";
}

{Id} {
    return "ID";
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
        + ", columna " + (yycolumn + 1)
        + ": caracter no reconocido '" + yytext() + "'"
    );
}