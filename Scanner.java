import java.io.BufferedReader;
import java.io.IOException;

public class Scanner {

    public int col;
    public int line;
    public int actual_char;
    public String auxiliar = "";
    public BufferedReader file;

    public Scanner(BufferedReader buff) {
        col = 0;
        line = 1;
        actual_char = Integer.parseInt("32");
        file = buff;
    }
    private void att_auxiliar() throws IOException {
        actual_char = file.read();
        auxiliar = String.valueOf((char) actual_char);
        col++;
    }

    public Token analise_lexica() throws Exception {
        
        Codigo_Valores codigos = new Codigo_Valores();
        Token token = new Token();

        principal: while(actual_char!=-1){
            auxiliar = String.valueOf((char) actual_char);

            if(auxiliar.charAt(0)==' '){
                // System.out.println("espaço");
                actual_char = file.read();
                col++;
            }
            else if(Checks.isDigit(auxiliar.charAt(0))){
                // System.out.println("Digito");
                token.setLexema(token.getLexema()+auxiliar);
                att_auxiliar();
                while(Checks.isDigit(auxiliar.charAt(0))){
                    token.setLexema(token.getLexema()+auxiliar);
                    att_auxiliar();
                }
                if(auxiliar.charAt(0)=='.'){
                    token.setNumb(codigos.getCode("DIGITO_FLOAT"));
                    token.setLexema(token.getLexema()+auxiliar);
                    att_auxiliar();
                    if(!Checks.isDigit(auxiliar.charAt(0))){

                        token = setToken(token.getLexema(),col-1,line,codigos.getCode("FLOAT_MAL_FORMATADO"));
                        // throw new Exception(Auxiliar.getErrorMessage(token,codigos,"Scanner"));
                        System.out.println(Auxiliar.getErrorMessage(token,codigos,"Float Mal Formatado","Scanner"));
                        System.exit(0);
                    }else{
                        token.setLexema(token.getLexema());
                        while(Checks.isDigit(auxiliar.charAt(0))){
                            token.setLexema(token.getLexema()+auxiliar);
                            att_auxiliar();
                        }
                        token = setToken(token.getLexema(),col-1,line,token.getNumb());
                        return token;
                    }
                }else{
                    token = setToken(token.getLexema(),col-1,line,codigos.getCode("DIGITO_INTEIRO"));
                    return token;
                }
            }
            else if(auxiliar.charAt(0)=='('){
                // System.out.println("(");
                token = setToken(token.getLexema()+auxiliar, col, line, codigos.getCode("PARENTESE_ESQUERDA"));
                att_auxiliar();
                return token;
            }
            else if(auxiliar.charAt(0)==')'){
                // System.out.println(")");
                token = setToken(token.getLexema()+auxiliar, col, line, codigos.getCode("PARENTESE_DIREITA"));
                att_auxiliar();
                return token;
            }
            else if(auxiliar.charAt(0)=='{'){
                // System.out.println("{");
                token = setToken(token.getLexema()+auxiliar, col, line, codigos.getCode("CHAVE_ESQUERDA"));
                att_auxiliar();
                return token;
            }
            else if(auxiliar.charAt(0)=='}'){
                // System.out.println("}");
                token = setToken(token.getLexema()+auxiliar, col, line, codigos.getCode("CHAVE_DIREITA"));
                att_auxiliar();
                return token;
            }
            else if(auxiliar.charAt(0)=='-'){
                // System.out.println("-");
                token = setToken(token.getLexema()+auxiliar, col, line, codigos.getCode("SUBTRACAO"));
                att_auxiliar();
                return token;
            }
            else if(auxiliar.charAt(0)=='+'){
                // System.out.println("+");
                token = setToken(token.getLexema()+auxiliar, col, line, codigos.getCode("SOMA"));
                att_auxiliar();
                return token;
            }
            else if(auxiliar.charAt(0)=='*'){
                // System.out.println("*");
                token = setToken(token.getLexema()+auxiliar, col, line, codigos.getCode("ASTERISCO"));
                att_auxiliar();
                return token;
            }
            else if(auxiliar.charAt(0)==','){
                // System.out.println(",");
                token = setToken(token.getLexema()+auxiliar, col, line, codigos.getCode("VIRGULA"));
                att_auxiliar();
                return token;
            }
            else if(auxiliar.charAt(0)==';'){
                // System.out.println(";");
                token = setToken(token.getLexema()+auxiliar, col, line, codigos.getCode("PONTO_VIRGULA"));
                att_auxiliar();
                return token;
            }
            else if(auxiliar.charAt(0)=='\t'){
                // System.out.println("tab");
                col=col+3; //+3 áq a funcao ja adiciona 1 a coluna
                att_auxiliar();
            }
            else if(auxiliar.charAt(0)=='\r' || auxiliar.charAt(0)=='\n'){
                // System.out.println("new line");
                col=0;//0 pq a funcao ja adiciona 1 a coluna
                line++;
                att_auxiliar();
            }
            else if(auxiliar.charAt(0)=='/'){
                String lexemaAux = auxiliar;
                att_auxiliar();
                if(auxiliar.charAt(0)=='*'){
                    token.setLexema(token.getLexema()+auxiliar);
                    att_auxiliar();
                    while(actual_char!=-1){
                        token.setLexema(token.getLexema()+auxiliar);
                        if(auxiliar.charAt(0)=='*'){
                            att_auxiliar();
                            token.setLexema(token.getLexema()+auxiliar);
                            if(auxiliar.charAt(0)=='/'){
                                token.setLexema("");
                                att_auxiliar();
                                continue principal;
                            }
                        }
                        if(auxiliar.charAt(0)=='\n' || auxiliar.charAt(0)=='\r'){
                            col=0;//0 pq a funcao ja adiciona 1 a coluna
                            line++;
                        }
                        att_auxiliar();
                    }
                    token = setToken(token.getLexema(),col-1, line, codigos.getCode("COMENTARIO_NAO_FECHADO"));
                    // throw new Exception(Auxiliar.getErrorMessage(token,codigos,"Scanner"));
                    System.out.println(Auxiliar.getErrorMessage(token,codigos,"Comentario Não Fechado","Scanner"));
                    System.exit(0);
                }
                else if(auxiliar.charAt(0)=='/'){
                    token.setLexema("");
                    while(auxiliar.charAt(0)!='\n'&&auxiliar.charAt(0)!='\r'){
                        att_auxiliar();
                    }
                    col=0;//0 pq a funcao ja adiciona 1 a coluna
                    line++;
                    att_auxiliar();
                    continue principal;
                }
                else{
                    token = setToken(lexemaAux, col-1, line, codigos.getCode("BARRA"));
                    return token;
                }
            }
            else if(auxiliar.charAt(0)=='<'){
                token.setLexema(token.getLexema()+auxiliar);
                att_auxiliar();
                if(auxiliar.charAt(0)=='='){
                    token.setLexema(token.getLexema()+auxiliar);
                    token = setToken(token.getLexema(), col, line, codigos.getCode("MENOR_IGUAL"));
                    att_auxiliar();
                    return token;
                }
                token = setToken(token.getLexema(), col, line, codigos.getCode("MENOR_QUE"));
                return token;
            }
            else if(auxiliar.charAt(0)=='>'){
                token.setLexema(token.getLexema()+auxiliar);
                att_auxiliar();
                if(auxiliar.charAt(0)=='='){
                    token.setLexema(token.getLexema()+auxiliar);
                    token = setToken(token.getLexema(), col, line, codigos.getCode("MAIOR_IGUAL"));
                    att_auxiliar();
                    return token;
                }
                token = setToken(token.getLexema(), col, line, codigos.getCode("MAIOR_QUE"));
                return token;
            }
            else if(auxiliar.charAt(0)=='='){
                token.setLexema(token.getLexema()+auxiliar);
                att_auxiliar();
                if(auxiliar.charAt(0)=='='){
                    token.setLexema(token.getLexema()+auxiliar);
                    token = setToken(token.getLexema(), col, line, codigos.getCode("IGUAL"));
                    att_auxiliar();
                    return token;
                }
                token = setToken(token.getLexema(), col, line, codigos.getCode("RECEBE"));
                return token;
            }
            else if(auxiliar.charAt(0)=='_'){
                token.setLexema(token.getLexema()+auxiliar);
                att_auxiliar();
                if(auxiliar.charAt(0)=='_'||Checks.isDigit(auxiliar.charAt(0))||Checks.isLetter(auxiliar.charAt(0))){
                    token.setLexema(token.getLexema()+auxiliar);
                    while(auxiliar.charAt(0)=='_'||Checks.isDigit(auxiliar.charAt(0))||Checks.isLetter(auxiliar.charAt(0))){
                        token.setLexema(token.getLexema()+auxiliar);
                        att_auxiliar();
                    }
                    token = setToken(token.getLexema(), col-1, line, codigos.getCode("IDENTIFICADOR"));
                    return token;
                }
                token = setToken(token.getLexema(), col-1, line, codigos.getCode("UNDERLINE"));
                return token;
            }
            else if(auxiliar.charAt(0)=='.'){
                token.setLexema(auxiliar);
                att_auxiliar();

                if(Checks.isDigit(auxiliar.charAt(0))){
                    while(Checks.isDigit(auxiliar.charAt(0))){
                        token.setLexema('0'+token.getLexema()+auxiliar);
                        att_auxiliar();
                    }
                    token = setToken(token.getLexema(), col-1, line, codigos.getCode("DIGITO_FLOAT"));
                    return token;
                }else{
                    token = setToken(token.getLexema(), col-1, line, codigos.getCode("CHARACTER_INVALIDO"));
                    // throw new Exception(Auxiliar.getErrorMessage(token,codigos,"Scanner"));
                    System.out.println(Auxiliar.getErrorMessage(token,codigos,"Caracter Inválido Após o Ponto","Scanner"));
                    System.exit(0);
                }
            }
            else if(auxiliar.charAt(0)=='!'){
                token.setLexema(token.getLexema()+auxiliar);
                att_auxiliar();
                if(auxiliar.charAt(0)=='='){
                    token.setLexema(token.getLexema()+auxiliar);
                    token = setToken(token.getLexema(), col, line, codigos.getCode("DIFERENTE"));
                    att_auxiliar();
                    return token;
                }
                token = setToken(token.getLexema(), col-1, line, codigos.getCode("CHARACTER_INVALIDO"));
                // throw new Exception(Auxiliar.getErrorMessage(token,codigos,"Scanner"));
                System.out.println(Auxiliar.getErrorMessage(token,codigos,"Caracter Inválido Após a Exclamação","Scanner"));
                System.exit(0);
            }
            else if(auxiliar.charAt(0)=='\''){
                token.setLexema(token.getLexema()+auxiliar);
                att_auxiliar();
                if(auxiliar.charAt(0)=='\''){
                    token.setLexema(token.getLexema()+auxiliar);
                    token = setToken(token.getLexema(), col, line, codigos.getCode("CHARACTER_MAL_FORMULADO"));
                    // throw new Exception(Auxiliar.getErrorMessage(token,codigos,"Scanner"));
                    System.out.println(Auxiliar.getErrorMessage(token,codigos,"Char Mal Formulado(Vazio)","Scanner"));
                    System.exit(0);
                }
                token.setLexema(token.getLexema()+auxiliar);
                att_auxiliar();
                if(auxiliar.charAt(0)=='\''){
                    token.setLexema(token.getLexema().substring(1,2));
                    token = setToken(token.getLexema(), col, line, codigos.getCode("DIGITO_CHAR"));
                    att_auxiliar();
                    return token;
                }else{
                    token.setLexema(token.getLexema()+auxiliar);
                    token = setToken(token.getLexema(), col, line, codigos.getCode("CHARACTER_MAL_FORMULADO"));
                    // throw new Exception(Auxiliar.getErrorMessage(token,codigos,"Scanner"));
                    System.out.println(Auxiliar.getErrorMessage(token,codigos,"Char Mal Formulado(Mais de Uma Letra Após A Primeira Aspa.)","Scanner"));
                    System.exit(0);
                }
            }
            else if(Checks.isLetter(auxiliar.charAt(0))){
                token.setLexema(token.getLexema()+auxiliar);
                att_auxiliar();
                if(auxiliar.charAt(0)=='_'||Checks.isDigit(auxiliar.charAt(0))||Checks.isLetter(auxiliar.charAt(0))){
                    while(auxiliar.charAt(0)=='_'||Checks.isDigit(auxiliar.charAt(0))||Checks.isLetter(auxiliar.charAt(0))){
                        token.setLexema(token.getLexema()+auxiliar);
                        att_auxiliar();
                    }
                    for(int i = 1; i<=9; i++){
                        if(token.getLexema().toUpperCase().equals(codigos.getName(i))){
                            token = setToken(token.getLexema(),col-1, line, i);
                            return token;
                        }
                    }
                }
                token = setToken(token.getLexema(),col-1, line, codigos.getCode("IDENTIFICADOR"));
                return token;
            }
            else{
                token.setLexema(token.getLexema()+auxiliar);
                token = setToken(token.getLexema(),col, line, codigos.getCode("CHARACTER_INVALIDO"));
                // throw new Exception(Auxiliar.getErrorMessage(token,codigos,"Scanner"));
                System.out.println(Auxiliar.getErrorMessage(token,codigos,"Caracter Invalido","Scanner"));
                System.exit(0);
            }
        }
        token = setToken("", col, line, codigos.getCode("FIM_ARQUIVO"));
        return token;
    }
    private Token setToken(String lexema, int col, int line, int numb){
        return new Token(lexema,col,line,numb);
    }
}