import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Parser {
    
    Token token;
    Scanner scan;
    Codigo_Valores codigo;
    
    int escopo;
    Stack<Tabela_Simbolos> pilhaCodigos = new Stack<Tabela_Simbolos>();
    Tabela_Simbolos simboloIdentificador;
    Tabela_Simbolos simboloAritimetico;
    int overAllCodeAritimetico;
    Token tokenPrev;
    
    boolean flagExpArit;
    boolean flagAtribuicao;

    int contadorCodigoInt;
    int contadorCodigoIntLabel;
    String aux1CodInt,aux2CodInt;
    List<String> CodigosIntermediarios = new ArrayList<String>();

    public Parser(Scanner scan){
        contadorCodigoIntLabel = 0;
        flagExpArit=false;
        overAllCodeAritimetico=99;//flag
        flagAtribuicao=false;
        escopo = 0;
        codigo = new Codigo_Valores();
        this.scan = scan;
        aux1CodInt="";
        aux2CodInt="";
    }

    public void readAllIntermediateCodes(){
        
        if(CodigosIntermediarios.size()>0){
            
            for(String codigo:CodigosIntermediarios){
                System.out.println(codigo);
            }

        }
    }

    public void start() throws Exception {
        bloco_inicial();
    }

    public void bloco_inicial() throws Exception {
        tokenPrev=token;
        token = scan.analise_lexica();
        if(!token.getLexema().toUpperCase().equals("INT")){
            // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Erro na Construção do Main.","Parser"));
            System.out.println(Auxiliar.getErrorMessage(token, codigo, "Erro na Construção do Main.","Parser"));
            System.exit(0);
        }
        tokenPrev=token;
        token = scan.analise_lexica();
        if(!token.getLexema().toUpperCase().equals("MAIN")){
            // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Erro na Construção do Main.","Parser"));
            System.out.println(Auxiliar.getErrorMessage(token, codigo, "Erro na Construção do Main.","Parser"));
            System.exit(0);
        }
        tokenPrev=token;
        token = scan.analise_lexica();
        if(!token.getLexema().equals("(")){
            // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Erro na Construção do Main.","Parser"));
            System.out.println(Auxiliar.getErrorMessage(token, codigo, "Erro na Construção do Main.","Parser"));
            System.exit(0);
        }
        tokenPrev=token;
        token = scan.analise_lexica();
        if(!token.getLexema().equals(")")){
            // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Erro na Construção do Main.","Parser"));
            System.out.println(Auxiliar.getErrorMessage(token, codigo, "Erro na Construção do Main.","Parser"));
            System.exit(0);
        }
        tokenPrev=token;
        token = scan.analise_lexica();
        if(!bloco()){
            // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Construção do Bloco.","Parser"));
            System.out.println(Auxiliar.getErrorMessage(token, codigo, "Erro na Construção do Bloco.","Parser"));
            System.exit(0);
        }
    }
    
    public boolean bloco() throws Exception {
        // System.out.println("bloco");
        if(token.getLexema().equals("{")){
            escopo++;
            do{
                tokenPrev=token;
                token = scan.analise_lexica();
            }while(decl_var());

            while(Comando());
            
            
            if(token.getLexema().equals("}")){
                removerPilhaByEscopo(escopo);
                escopo --;
                tokenPrev=token;
                token = scan.analise_lexica();
                return true;
            }else{
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Fechamento do Bloco.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(token, codigo, "Erro no Fechamento do Bloco.","Parser"));
                System.exit(0);
            }
        }
        return false;
    }

    public boolean decl_var() throws Exception {
        
        if(token.getNumb()==codigo.getCode("INT")||token.getNumb()==codigo.getCode("FLOAT")||token.getNumb()==codigo.getCode("CHAR")){
            int codigoVar = token.getNumb();
            tokenPrev=token;
            token = scan.analise_lexica();
            if(codigo.getName(token.getNumb()).toUpperCase().equals("IDENTIFICADOR")){
                if(checkIfSameLexByEscopo(escopo, token.getLexema())){
                    // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Identificador Já Declarado Antes.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(token, codigo, "Identificador Já Declarado Antes.","Semantico"));
                    System.exit(0);
                }
                pilhaCodigos.add(new Tabela_Simbolos(token, escopo, codigoVar,token.getLexema()));
                tokenPrev=token;
                token = scan.analise_lexica();
                if(token.getLexema().equals(";")){
                    return true;
                }
                else if(token.getLexema().equals(",")){
                    laco: while (true){
                        if(token.getLexema().equals(",")){
                            tokenPrev=token;
                            token = scan.analise_lexica();
                            if(codigo.getName(token.getNumb()).toUpperCase().equals("IDENTIFICADOR")){
                                if(checkIfSameLexByEscopo(escopo, token.getLexema())){
                                    // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Identificador Já Declarado Antes.","Semantico"));
                                    System.out.println(Auxiliar.getErrorMessage(token, codigo, "Identificador Já Declarado Antes.","Semantico"));
                                    System.exit(0);
                                }
                                pilhaCodigos.add(new Tabela_Simbolos(token, escopo, codigoVar,token.getLexema()));
                                tokenPrev=token;
                                token = scan.analise_lexica();
                                if(token.getLexema().equals(";")){
                                    return true;
                                }
                                else if(token.getLexema().equals(",")){
                                    continue laco;
                                }
                                else{
                                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro a Declaração da Variavel.","Parser"));
                                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro a Declaração da Variavel.","Parser"));
                                    System.exit(0);
                                }
                            }else{
                                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro a Declaração da Variavel.","Parser"));
                                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro a Declaração da Variavel.","Parser"));
                                System.exit(0);
                            }
                        }
                    }
                }else{
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Fechamento da Declaração da Variável.","Parser"));
                        System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Fechamento da Declaração da Variável.","Parser"));
                        System.exit(0);
                }
            }
        }
        return false;
    }

    public boolean Comando() throws Exception {
        if(comando_basico()){
            return true;
        }
        else if(iteracao()){
            return true;
        }
        else{
            if(token.getNumb()==codigo.getCode("IF")){
                
                tokenPrev=token;
                token = scan.analise_lexica();
                if(!token.getLexema().equals("(")){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Abertura dos Parenteses do IF.","Parser"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Abertura dos Parenteses do IF.","Parser"));
                    System.exit(0);
                }
                tokenPrev=token;
                token = scan.analise_lexica();
                if(!expr_relacional()){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro Expressão Relacional.","Parser"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro Expressão Relacional.","Parser"));
                    System.exit(0);
                }
                if(!token.getLexema().equals(")")){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Fechamento dos Parenteses do IF.","Parser"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Fechamento dos Parenteses do IF.","Parser"));
                    System.exit(0);
                }
                CodigosIntermediarios.add("if "+aux1CodInt+" == 0 goto LABEL"+contadorCodigoIntLabel+":");
                // System.out.println("if "+aux1CodInt+" == 0 goto LABEL"+contadorCodigoIntLabel+":");
                int contadorLabelIfFalso = contadorCodigoIntLabel;
                contadorCodigoIntLabel++;
                tokenPrev=token;
                token = scan.analise_lexica();
                if(!Comando()){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Comando do IF.","Parser"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Comando do IF.","Parser"));
                    System.exit(0);
                }
                
                if(token.getNumb()!=codigo.getCode("ELSE")){
                    CodigosIntermediarios.add("LABEL"+contadorLabelIfFalso+":");
                    // System.out.println("LABEL"+contadorLabelIfFalso+":");
                    return true;
                }
                CodigosIntermediarios.add("goto LABEL"+contadorCodigoIntLabel+":");
                int contadorLabelIfTrue = contadorCodigoIntLabel;
                contadorCodigoIntLabel++;

                CodigosIntermediarios.add("LABEL"+contadorLabelIfFalso+":");
                // System.out.println("LABEL"+contadorLabelIfFalso+":");
                tokenPrev=token;
                token = scan.analise_lexica();
                if(!Comando()){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Comando do ELSE.","Parser"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Comando do ELSE.","Parser"));
                    System.exit(0);
                }
                CodigosIntermediarios.add("LABEL"+contadorLabelIfTrue+":");
                return true;
            }
        }
        return false;
    }

    public boolean comando_basico() throws Exception {
        if(atribuicao()){
            return true;
        }
        else if(bloco()){
            return true;
        }
        return false;
    }
    public boolean atribuicao() throws Exception {

        if(codigo.getName(token.getNumb()).toUpperCase().equals("IDENTIFICADOR")){
            // System.out.println("ATRIBUICAO");
            //verificar se token esta cadastrado
            String identificator = token.getLexema();
            simboloIdentificador = getSimboloPilhaByLexema(token.getLexema());
            flagAtribuicao=true;
            if(simboloIdentificador==null){
                // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Semantico"));
                System.out.println(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Semantico"));
                System.exit(0);
            }
            tokenPrev=token;
            token = scan.analise_lexica();
            if(!token.getLexema().equals("=")){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Atribuição, Falta do =","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Atribuição, Falta do =","Parser"));
                System.exit(0);
            }
            tokenPrev = token;
            token = scan.analise_lexica();
            if(!exprecao_arit()){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Expressão Aritimetica.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Expressão Aritimetica.","Parser"));
                System.exit(0);
            }
            tokenPrev = token;
            if(!token.getLexema().equals(";")){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro Fechamento da Atribuição.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro Fechamento da Atribuição.","Parser"));
                System.exit(0);
            }

            CodigosIntermediarios.add(identificator+" = "+aux1CodInt);
            // System.out.println(identificator+" = "+aux1CodInt);
            
            tokenPrev=token;
            token = scan.analise_lexica();
            return true;
        }
        return false;
    }
    public boolean exprecao_arit() throws Exception {

        // System.out.println("exprecao_arit");
        aux1CodInt = token.getLexema();
        if(!flagAtribuicao){
            flagExpArit=true;
        }

        if(flagExpArit){
            simboloAritimetico = new Tabela_Simbolos(token, escopo, token.getNumb(),token.getLexema());
            flagExpArit=false;
        }

        CodigoIntermediario aux1 = termo();
        aux1CodInt = aux1.getExpressao();

        laco: while(true){

            if( token.getLexema().equals("+") || token.getLexema().equals("-") ){
                String operador = token.getLexema();
                
                token = scan.analise_lexica();
                CodigoIntermediario aux2 = termo();

                if(!token.getLexema().equals("(") ){
                    CodigosIntermediarios.add("T"+contadorCodigoInt+" = "+aux1.getExpressao()+operador+aux2.getExpressao());
                    // System.out.println("T"+contadorCodigoInt+" = "+aux1.getExpressao()+operador+aux2.getExpressao());
                    aux1 = new CodigoIntermediario("T"+contadorCodigoInt, token, token.getNumb());
                    aux1CodInt = "T"+contadorCodigoInt;
                    contadorCodigoInt++;
                } 
                aux2 = aux1;
                continue laco;
            }else{
                return true;
            }
        }
    }
    public CodigoIntermediario termo() throws Exception {

        CodigoIntermediario aux1 = fator();

        laco: while(true){
            token = scan.analise_lexica();
            
            if(aux1==null){
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Construção do Fator.","Parser"));
                System.exit(0);
            }
            
            tokenPrev=token;
            flagExpArit=false;

            if(flagAtribuicao){
                if(aux1.isChecked()==false &&
                  (codigo.getName(simboloIdentificador.getNumb()).equals("FLOAT")     ||
                   codigo.getName(simboloIdentificador.getNumb()).equals("DIGITO_FLOAT"))){
                    aux1 = checkIntToFloat(aux1.getToken());
                    aux1.setChecked(true);
                }
            }else{
                if(aux1.isChecked()==false &&
                (codigo.getName(simboloAritimetico.getNumb()).equals("FLOAT")     ||
                 codigo.getName(simboloAritimetico.getNumb()).equals("DIGITO_FLOAT"))){
                  aux1 = checkIntToFloat(aux1.getToken());
                  aux1.setChecked(true);
                }
            }

            // if( aux1.isChecked()==false &&
            //     (codigo.getName(simboloIdentificador.getNumb()).equals("FLOAT")     ||
            //     codigo.getName(simboloIdentificador.getNumb()).equals("DIGITO_FLOAT")||
            //     codigo.getName(simboloAritimetico.getNumb()).equals("FLOAT")     ||
            //     codigo.getName(simboloAritimetico.getNumb()).equals("DIGITO_FLOAT"))){

            //     aux1 = checkIntToFloat(aux1.getToken());
            //     aux1.setChecked(true);
            // }

            if( token.getLexema().equals("*")){

                String operador = token.getLexema();
                
                tokenPrev=token;
                token = scan.analise_lexica();
                CodigoIntermediario aux2 = fator();
                
                if(flagAtribuicao){

                    if(aux2.isChecked()==false &&
                      (codigo.getName(simboloIdentificador.getNumb()).equals("FLOAT")     ||
                       codigo.getName(simboloIdentificador.getNumb()).equals("DIGITO_FLOAT"))){
                        aux2 = checkIntToFloat(aux2.getToken());
                        aux2.setChecked(true);
                    }
                }else{

                    if(aux2.isChecked()==false &&
                    (codigo.getName(simboloAritimetico.getNumb()).equals("FLOAT")     ||
                     codigo.getName(simboloAritimetico.getNumb()).equals("DIGITO_FLOAT"))){
                      aux2 = checkIntToFloat(aux2.getToken());
                      aux2.setChecked(true);
                    }
                }
                CodigosIntermediarios.add("T"+contadorCodigoInt+" = "+aux1.getExpressao()+operador+aux2.getExpressao());
                // System.out.println("T"+contadorCodigoInt+" = "+aux1.getExpressao()+operador+aux2.getExpressao());
                aux1CodInt = "T"+contadorCodigoInt;

                // aux1 = aux2;
                aux1 = new CodigoIntermediario("T"+contadorCodigoInt, token, token.getNumb());
                aux1.setChecked(true);
                contadorCodigoInt++;

                continue laco;
            }
            else if(token.getLexema().equals("/") ){
                
                String operador = token.getLexema();
                
                tokenPrev=token;

                token = scan.analise_lexica();
                CodigoIntermediario aux2 = fator();
                if(flagAtribuicao){
                    if(aux2.isChecked()==false &&
                      (codigo.getName(simboloIdentificador.getNumb()).equals("FLOAT")     ||
                       codigo.getName(simboloIdentificador.getNumb()).equals("DIGITO_FLOAT"))){
                        aux2 = checkIntToFloat(aux2.getToken());
                        aux2.setChecked(true);
                    }
                    if(codigo.getName(simboloIdentificador.getNumb()).equals("INT")     ||
                       codigo.getName(simboloIdentificador.getNumb()).equals("DIGITO_INTEIRO")){
                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                        System.exit(0);
                    }
                }else{
                    if(aux2.isChecked()==false &&
                    (codigo.getName(simboloAritimetico.getNumb()).equals("FLOAT")     ||
                     codigo.getName(simboloAritimetico.getNumb()).equals("DIGITO_FLOAT"))){
                      aux2 = checkIntToFloat(aux2.getToken());
                      aux2.setChecked(true);
                    }
                    if(codigo.getName(simboloIdentificador.getNumb()).equals("INT")     ||
                       codigo.getName(simboloIdentificador.getNumb()).equals("DIGITO_INTEIRO")){
                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                        System.exit(0);
                    }
                }
                CodigosIntermediarios.add("T"+contadorCodigoInt+" = "+aux1.getExpressao()+operador+aux2.getExpressao());
                // System.out.println("T"+contadorCodigoInt+" = "+aux1.getExpressao()+operador+aux2.getExpressao());
                aux1CodInt = "T"+contadorCodigoInt;

                // aux1 = aux2;
                aux1 = new CodigoIntermediario("T"+contadorCodigoInt, token, token.getNumb());
                aux1.setChecked(true);
                contadorCodigoInt++;

                //Toda divisão deve ser float, logo é checado. Porém é checado se antes tem algum char.
                
                if(auxiliarTermoChar(tokenPrev)){
                    if(!tokenPrev.getLexema().equals("(")&&
                       !tokenPrev.getLexema().equals(")")){
                           checkAtribuicaoSimbolos(token, codigo.getCode("DIGITO_FLOAT"));
                    }
                }
                continue laco;
            }
            else{
                return aux1;
            }
        }
    }

    public CodigoIntermediario fator() throws Exception {
        // System.out.println("fator");
        if(token.getNumb()==codigo.getCode("IDENTIFICADOR")    ||
           token.getNumb()==codigo.getCode("DIGITO_FLOAT")     ||
           token.getNumb()==codigo.getCode("DIGITO_INTEIRO")   ||
           token.getNumb()==codigo.getCode("DIGITO_CHAR")){
            String lexema = token.getLexema();
            int numb = token.getNumb();
            if(flagExpArit==false){
                if(token.getNumb()==codigo.getCode("IDENTIFICADOR")){
                    Tabela_Simbolos auxSimb = getSimboloPilhaByLexema(token.getLexema());
                    if(auxSimb==null){
                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Semantico"));
                        System.exit(0);
                    }
                    lexema = auxSimb.getLexema();
                    numb = auxSimb.getNumb();
                    checkAtribuicaoSimbolos(token,auxSimb.getNumb());
                }else{
                    checkAtribuicaoSimbolos(token,99);
                }
            }
            return new CodigoIntermediario(lexema,token,numb);

        }
        else if(token.getLexema().equals("(")){
            String save = aux1CodInt;
            String operador = tokenPrev.getLexema();
            token = scan.analise_lexica();
            tokenPrev = token;
            if(exprecao_arit()){
                if(token.getLexema().equals(")")){
                    // System.out.println("TTT"+contadorCodigoInt+" = "+save+operador+aux1CodInt);
                    aux1CodInt = "T"+(contadorCodigoInt-1);
                    // contadorCodigoInt++;
                    CodigoIntermediario retornado = new CodigoIntermediario(aux1CodInt, tokenPrev, tokenPrev.getNumb());
                    retornado.setChecked(true);
                    return retornado;
                }
            }
        }
        return null;
    }

    public void checkAtribuicaoSimbolos(Token token, int numb) throws Exception {

        int number = numb;
        
        if(numb==99){ //99 é a flag
            number=token.getNumb();
        }

        if(flagAtribuicao){
            if(codigo.getName(simboloIdentificador.getNumb()).equals("INT")          ||
               codigo.getName(simboloIdentificador.getNumb()).equals("DIGITO_INTEIRO")){

                if(!codigo.getName(number).equals("INT")           &&
                    !codigo.getName(number).equals("DIGITO_INTEIRO")){
                    // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                    System.exit(0);
                }
    
            }
            else if(codigo.getName(simboloIdentificador.getNumb()).equals("FLOAT")     ||
                    codigo.getName(simboloIdentificador.getNumb()).equals("DIGITO_FLOAT")){
                        if(!codigo.getName(number).equals("INT")            &&
                        !codigo.getName(number).equals("DIGITO_INTEIRO") &&
                        !codigo.getName(number).equals("FLOAT")          &&
                        !codigo.getName(number).equals("DIGITO_FLOAT")){
                            
                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                        System.exit(0);
                    }
            }
            else if(codigo.getName(simboloIdentificador.getNumb()).equals("CHAR")     ||
                    codigo.getName(simboloIdentificador.getNumb()).equals("DIGITO_CHAR")){

                if(!codigo.getName(number).equals("CHAR")     &&
                    !codigo.getName(number).equals("DIGITO_CHAR")){
                        
                    // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                    System.exit(0);
                    
                }
            }
            else if(codigo.getName(simboloIdentificador.getNumb()).equals("IDENTIFICADOR")){

                
                Tabela_Simbolos auxSimb1 = getSimboloPilhaByLexema(simboloIdentificador.getLexema());
                if(auxSimb1==null){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.exit(0);
                }
                
                if(codigo.getName(auxSimb1.getNumb()).equals("CHAR")||
                   codigo.getName(auxSimb1.getNumb()).equals("DIGITO_CHAR")){
                       
                    if(!codigo.getName(number).equals("CHAR")&&
                       !codigo.getName(number).equals("DIGITO_CHAR")){
                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.exit(0);
                   }
                }
                else if(codigo.getName(auxSimb1.getNumb()).equals("FLOAT")||
                        codigo.getName(auxSimb1.getNumb()).equals("DIGITO_FLOAT")||
                        codigo.getName(auxSimb1.getNumb()).equals("INT")||
                        codigo.getName(auxSimb1.getNumb()).equals("DIGITO_INTEIRO")){
             
                    if(!codigo.getName(number).equals("FLOAT")&&
                       !codigo.getName(number).equals("DIGITO_FLOAT")&&
                       !codigo.getName(number).equals("INT")&&
                       !codigo.getName(number).equals("DIGITO_INTEIRO")){

                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.exit(0);
                        
                    }
                }

            }

        }else{
            // simboloAritimetico
            if(codigo.getName(simboloAritimetico.getNumb()).equals("INT")          ||
               codigo.getName(simboloAritimetico.getNumb()).equals("DIGITO_INTEIRO")){
                
                if(codigo.getName(number).equals("FLOAT")     ||
                   codigo.getName(number).equals("DIGITO_FLOAT")){

                    simboloAritimetico = new Tabela_Simbolos(token, escopo, number,token.getLexema());

                }
                else if(codigo.getName(number).equals("CHAR")     ||
                        codigo.getName(number).equals("DIGITO_CHAR")){
                            // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                            System.out.println(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                            System.exit(0);
                        }
    
            }
            else if(codigo.getName(simboloAritimetico.getNumb()).equals("FLOAT")     ||
                    codigo.getName(simboloAritimetico.getNumb()).equals("DIGITO_FLOAT")){
                
                
                if(codigo.getName(number).equals("CHAR")     ||
                   codigo.getName(number).equals("DIGITO_CHAR")){
                   
                    // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes.","Semantico"));
                    System.exit(0);
                    
                }
    
            }
            else if(codigo.getName(simboloAritimetico.getNumb()).equals("CHAR")     ||
                    codigo.getName(simboloAritimetico.getNumb()).equals("DIGITO_CHAR")){
    
                
                if(!codigo.getName(number).equals("CHAR")     &&
                   !codigo.getName(number).equals("DIGITO_CHAR")){

                      //throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes. ","Semantico"));
                      System.out.println(Auxiliar.getErrorMessage(token, codigo, "Atribuição com Valores de Tipos Diferentes. ","Semantico"));
                      System.exit(0);
                    
                }
            }
            else if(codigo.getName(simboloAritimetico.getNumb()).equals("IDENTIFICADOR")){

                
                Tabela_Simbolos auxSimb1 = getSimboloPilhaByLexema(simboloAritimetico.getLexema());
                if(auxSimb1==null){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.exit(0);
                }
                
                if(codigo.getName(auxSimb1.getNumb()).equals("CHAR")||
                   codigo.getName(auxSimb1.getNumb()).equals("DIGITO_CHAR")){
                       
                    if(!codigo.getName(number).equals("CHAR")&&
                       !codigo.getName(number).equals("DIGITO_CHAR")){
                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.exit(0);
                   }
                }
                else if(codigo.getName(auxSimb1.getNumb()).equals("FLOAT")||
                        codigo.getName(auxSimb1.getNumb()).equals("DIGITO_FLOAT")||
                        codigo.getName(auxSimb1.getNumb()).equals("INT")||
                        codigo.getName(auxSimb1.getNumb()).equals("DIGITO_INTEIRO")){

                    if(!codigo.getName(number).equals("FLOAT")&&
                       !codigo.getName(number).equals("DIGITO_FLOAT")&&
                       !codigo.getName(number).equals("INT")&&
                       !codigo.getName(number).equals("DIGITO_INTEIRO")){

                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.exit(0);
                        
                    }
                }

            }
        }
    }

    public boolean iteracao() throws Exception {
        if(token.getNumb()==codigo.getCode("WHILE")){
            CodigosIntermediarios.add("LABEL"+contadorCodigoIntLabel+":");
            // System.out.println("LABEL"+contadorCodigoIntLabel+":");
            int labelWhileInicial = contadorCodigoIntLabel;
            contadorCodigoIntLabel++;
            tokenPrev=token;
            token = scan.analise_lexica();
            if(!token.getLexema().equals("(")){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Abertura dos Parenteses do WHILE.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Abertura dos Parenteses do WHILE.","Parser"));
                System.exit(0);
            }
            tokenPrev=token;
            token = scan.analise_lexica();

            if(!expr_relacional()){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Expressão Relacional.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Expressão Relacional.","Parser"));
                System.exit(0);
            }
            if(!token.getLexema().equals(")")){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Fechamento dos Parenteses do WHILE.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Fechamento dos Parenteses do WHILE.","Parser"));
                System.exit(0);
            }
            CodigosIntermediarios.add("if "+aux1CodInt+" == 0 goto LABEL"+contadorCodigoIntLabel+":");
            // System.out.println("if "+aux1CodInt+" == 0 goto LABEL"+contadorCodigoIntLabel+":");
            int labelWhileFinal = contadorCodigoIntLabel;
            contadorCodigoIntLabel++;
            tokenPrev=token;
            token = scan.analise_lexica();
            if(Comando()){
                CodigosIntermediarios.add("goto LABEL"+labelWhileInicial+":");
                CodigosIntermediarios.add("LABEL"+labelWhileFinal+":");
                // System.out.println("goto LABEL"+labelWhileInicial+":");
                // System.out.println("LABEL"+labelWhileFinal+":");
                return true;
            }
            // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Comando do WHILE.","Parser"));
            System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Comando do WHILE.","Parser"));
            System.exit(0);
        }
        else if(token.getNumb()==codigo.getCode("DO")){
            CodigosIntermediarios.add("LABEL"+contadorCodigoIntLabel+":");
            // System.out.println("LABEL"+contadorCodigoIntLabel+":");
            int contadorLabelDoWhile = contadorCodigoIntLabel;
            contadorCodigoIntLabel++;
            tokenPrev=token;
            token = scan.analise_lexica();
            if(!Comando()){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Comando do DO WHILE.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Comando do DO WHILE.","Parser"));
                System.exit(0);
            }
            if(token.getNumb()!=codigo.getCode("WHILE")){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro Falta do Comando WHILE após o Fechamento das chaves.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro Falta do Comando WHILE após o Fechamento das chaves.","Parser"));
                System.exit(0);
            }
            tokenPrev=token;
            token = scan.analise_lexica();
            if(!token.getLexema().equals("(")){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Abertura dos Parenteses do DO WHILE.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Abertura dos Parenteses do DO WHILE.","Parser"));
                System.exit(0);
            }
            tokenPrev=token;
            token = scan.analise_lexica();
            if(!expr_relacional()){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Expressão Relacional.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Expressão Relacional.","Parser"));
                System.exit(0);
            }
            if(!token.getLexema().equals(")")){
                // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Fechamento dos Parenteses do DO WHILE.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro no Fechamento dos Parenteses do DO WHILE.","Parser"));
                System.exit(0);
            }
            tokenPrev=token;
            token = scan.analise_lexica();
            if(token.getLexema().equals(";")){
                tokenPrev=token;
                token = scan.analise_lexica();
                CodigosIntermediarios.add("if "+aux1CodInt+" != 0 goto LABEL"+contadorLabelDoWhile+":");
                // System.out.println("if "+aux1CodInt+" != 0 goto LABEL"+contadorLabelDoWhile+":");
                return true;
            }
            // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Construção do DO WHILE.","Parser"));
            System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Erro na Construção do DO WHILE.","Parser"));
            System.exit(0);
        }
        return false;
    }

    public boolean expr_relacional() throws Exception {
        flagAtribuicao=false;
        if(exprecao_arit()){
            String exp1 = aux1CodInt;
            Tabela_Simbolos PrevSimboloAritimetico = simboloAritimetico;
            if(token.getNumb()>=10&&token.getNumb()<=15){//checa os relacionais
                String relacional = token.getLexema();
                tokenPrev=token;
                token = scan.analise_lexica();
                if(exprecao_arit()){
                    String exp2 = aux1CodInt;
                    CodigosIntermediarios.add("T"+contadorCodigoInt+" = "+exp1+relacional+exp2);
                    // System.out.println("T"+contadorCodigoInt+" = "+exp1+relacional+exp2);
                    aux1CodInt = "T"+contadorCodigoInt;
                    contadorCodigoInt++;
                    checkSameTabelaSimbolos(PrevSimboloAritimetico, simboloAritimetico);
                    return true;
                }
            }
        }
        return false;
    }
    public void checkSameTabelaSimbolos(Tabela_Simbolos prev, Tabela_Simbolos actual) throws Exception {    

        if(codigo.getName(prev.getNumb()).equals("INT") ||
           codigo.getName(prev.getNumb()).equals("DIGITO_INTEIRO")){

            int auxiliar=actual.getNumb();
            if(codigo.getName(actual.getNumb()).equals("IDENTIFICADOR")){
                Tabela_Simbolos auxSimb = getSimboloPilhaByLexema(actual.getLexema());
                if(auxSimb==null){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado1.","Semantico"));
                    System.exit(0);
                }
                auxiliar=auxSimb.getNumb();
            }

            if(!codigo.getName(auxiliar).equals("INT") &&
                !codigo.getName(auxiliar).equals("DIGITO_INTEIRO")&&
                !codigo.getName(auxiliar).equals("FLOAT") &&
                !codigo.getName(auxiliar).equals("DIGITO_FLOAT")){
                // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                System.out.println(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                System.exit(0);
            }


        }
        else if(codigo.getName(prev.getNumb()).equals("FLOAT") ||
                codigo.getName(prev.getNumb()).equals("DIGITO_FLOAT")){
            int auxiliar=actual.getNumb();
            if(codigo.getName(actual.getNumb()).equals("IDENTIFICADOR")){
                Tabela_Simbolos auxSimb  = getSimboloPilhaByLexema(actual.getLexema());
                if(auxSimb==null){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.exit(0);
                }
                auxiliar=auxSimb.getNumb();
            }

            if(!codigo.getName(auxiliar).equals("FLOAT") &&
                !codigo.getName(auxiliar).equals("DIGITO_FLOAT")&&
                !codigo.getName(auxiliar).equals("INT") &&
                !codigo.getName(auxiliar).equals("DIGITO_INTEIRO")){
                // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores Não Compativeis.","Semantico"));
                System.out.println(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores Não Compativeis.","Semantico"));
                System.exit(0);
            }


        }
        else if(codigo.getName(prev.getNumb()).equals("CHAR") ||
                codigo.getName(prev.getNumb()).equals("DIGITO_CHAR")){

            int auxiliar = actual.getNumb();
            if(codigo.getName(actual.getNumb()).equals("IDENTIFICADOR")){
                Tabela_Simbolos auxSimb = getSimboloPilhaByLexema(actual.getLexema());
                if(auxSimb==null){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.exit(0);
                }
                auxiliar=auxSimb.getNumb();
            }

            if(!codigo.getName(auxiliar).equals("CHAR") &&
                !codigo.getName(auxiliar).equals("DIGITO_CHAR")){
                // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                System.out.println(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                System.exit(0);
            }

        }
        else if(codigo.getName(prev.getNumb()).equals("IDENTIFICADOR")){

            int auxiliarPrev = prev.getNumb();
            if(codigo.getName(prev.getNumb()).equals("IDENTIFICADOR")){
                Tabela_Simbolos auxSimb1 = getSimboloPilhaByLexema(prev.getLexema());
                if(auxSimb1==null){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.exit(0);
                }
                auxiliarPrev=auxSimb1.getNumb();
            }

            int auxiliarActual = actual.getNumb();
            if(codigo.getName(actual.getNumb()).equals("IDENTIFICADOR")){
                Tabela_Simbolos auxSimb2 = getSimboloPilhaByLexema(actual.getLexema());
                if(auxSimb2==null){
                    // throw new Exception(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.out.println(Auxiliar.getErrorMessage(tokenPrev, codigo, "Identificador Não Declarado.","Semantico"));
                    System.exit(0);
                }
                auxiliarActual=auxSimb2.getNumb();
            }

            if(codigo.getName(auxiliarPrev).equals("CHAR")||
               codigo.getName(auxiliarPrev).equals("DIGITO_CHAR")){
                
                if(!codigo.getName(auxiliarActual).equals("CHAR")&&
                   !codigo.getName(auxiliarActual).equals("DIGITO_CHAR")){
                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.exit(0);
                   }
            }
            else if(codigo.getName(auxiliarPrev).equals("FLOAT")||
                    codigo.getName(auxiliarPrev).equals("DIGITO_FLOAT")||
                    codigo.getName(auxiliarPrev).equals("INT")||
                    codigo.getName(auxiliarPrev).equals("DIGITO_INTEIRO")){
                        
                if(!codigo.getName(auxiliarPrev).equals("FLOAT")&&
                   !codigo.getName(auxiliarPrev).equals("DIGITO_FLOAT")&&
                   !codigo.getName(auxiliarPrev).equals("INT")&&
                   !codigo.getName(auxiliarPrev).equals("DIGITO_INTEIRO")){
                        // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.out.println(Auxiliar.getErrorMessage(token, codigo, "Expressão Aritimetica com Valores de Tipos Diferentes.","Semantico"));
                        System.exit(0);
                }
            }

        }
    }
    public void removerPilhaByEscopo(int escop){
        for(int i = pilhaCodigos.size()-1; i>=0; i--){
            if(pilhaCodigos.get(i).getEscopo()<escop){
                break;
            }
            if(pilhaCodigos.get(i).getEscopo()==escop){
                pilhaCodigos.pop();
            }
        }
    }

    public Tabela_Simbolos getSimboloPilhaByLexema(String lex){
        for(int i = pilhaCodigos.size()-1; i>=0; i--){
            if(pilhaCodigos.get(i).getToken().getLexema().equals(lex)){
                return pilhaCodigos.get(i);
            }
        }
        return null;
    }

    public boolean checkIfSameLexByEscopo(int escop, String lex){
        for(int i = pilhaCodigos.size()-1; i>=0; i--){
            if(pilhaCodigos.get(i).getEscopo()<escop){
                break;
            }
            if(pilhaCodigos.get(i).getLexema().equals(lex)){
                return true;
            }
        }
        return false;
    }
    public boolean auxiliarTermoChar(Token tokenPrev) throws Exception {

        if( codigo.getName(token.getNumb()).equals("CHAR") ||
        codigo.getName(token.getNumb()).equals("DIGITO_CHAR")||
        codigo.getName(token.getNumb()).equals("IDENTIFICADOR")||
        codigo.getName(tokenPrev.getNumb()).equals("CHAR") ||
        codigo.getName(tokenPrev.getNumb()).equals("DIGITO_CHAR")||
        codigo.getName(tokenPrev.getNumb()).equals("IDENTIFICADOR")){

            if(codigo.getName(token.getNumb()).equals("CHAR") ||
                    codigo.getName(token.getNumb()).equals("DIGITO_CHAR")||
                    codigo.getName(tokenPrev.getNumb()).equals("CHAR") ||
                    codigo.getName(tokenPrev.getNumb()).equals("DIGITO_CHAR")){

                return false;
            }
            
            Tabela_Simbolos auxSimb1 = getSimboloPilhaByLexema(tokenPrev.getLexema());
            Tabela_Simbolos auxSimb2 = getSimboloPilhaByLexema(token.getLexema());

            if(codigo.getName(tokenPrev.getNumb()).equals("IDENTIFICADOR")&&
               codigo.getName(token.getNumb()).equals("IDENTIFICADOR")){

                if(auxSimb1==null){
                    // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Parser"));
                    System.out.println(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Parser"));
                    System.exit(0);
                }
                if(auxSimb2==null){
                    // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Parser"));
                    System.out.println(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Parser"));
                    System.exit(0);
                }
                
                if( codigo.getName(auxSimb1.getNumb()).equals("CHAR") ||
                    codigo.getName(auxSimb1.getNumb()).equals("DIGITO_CHAR")||
                    codigo.getName(auxSimb2.getNumb()).equals("CHAR") ||
                    codigo.getName(auxSimb2.getNumb()).equals("DIGITO_CHAR")){

                        return false;
                }
            }
            else if(codigo.getName(tokenPrev.getNumb()).equals("IDENTIFICADOR")){
                if(auxSimb1==null){
                    // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Parser"));
                    System.out.println(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Parser"));
                    System.exit(0);
                }
                if( codigo.getName(auxSimb1.getNumb()).equals("CHAR") ||
                    codigo.getName(auxSimb1.getNumb()).equals("DIGITO_CHAR")){
                        return false;
                }
            
            }
            else if(codigo.getName(token.getNumb()).equals("IDENTIFICADOR")){
                if(auxSimb2==null){
                    // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Parser"));
                    System.out.println(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Parser"));
                    System.exit(0);
                }
                if( codigo.getName(auxSimb2.getNumb()).equals("CHAR") ||
                    codigo.getName(auxSimb2.getNumb()).equals("DIGITO_CHAR")){
                        return false;
                }
            }
        }
        return true;
    }
    private CodigoIntermediario checkIntToFloat(Token token){
        Token tokenAux = token;
        String retorno = token.getLexema();
        int numb = token.getNumb();

        if(codigo.getName(token.getNumb()).equals("IDENTIFICADOR")){
            
            
            Tabela_Simbolos auxSimb = getSimboloPilhaByLexema(tokenAux.getLexema());
            if(auxSimb==null){
                // throw new Exception(Auxiliar.getErrorMessage(token, codigo, "Identificador Não Declarado.","Parser"));
                System.out.println(Auxiliar.getErrorMessage(tokenAux, codigo, "Identificador Não Declarado.","Parser"));
                System.exit(0);
            }
            retorno = auxSimb.getLexema();
            tokenAux = auxSimb.getToken();
            numb = auxSimb.getNumb();
        }

        if(codigo.getName(numb).equals("INT")||
           codigo.getName(numb).equals("DIGITO_INTEIRO")){
            
            CodigosIntermediarios.add("T"+contadorCodigoInt+" = "+"int_to_float "+tokenAux.getLexema());
            // System.out.println("T"+contadorCodigoInt+" = "+"int_to_float "+tokenAux.getLexema());

            retorno = "T"+contadorCodigoInt;
            
            contadorCodigoInt++;

        }
        return new CodigoIntermediario(retorno, tokenAux, numb);
    }
}