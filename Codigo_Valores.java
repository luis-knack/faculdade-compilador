import java.util.HashMap;

public class Codigo_Valores {
    HashMap<Integer,String> mapGetName = new HashMap<Integer,String>();
    HashMap<String,Integer> mapGetCode = new HashMap<String,Integer>();

    public Codigo_Valores(){
        populateMapGetName();
        populateMapGetCode();
    }

    private void populateMapGetName(){

        mapGetName.put(1,"INT");
        mapGetName.put(2,"WHILE");
        mapGetName.put(3,"DO");
        mapGetName.put(4,"IF");
        mapGetName.put(5,"ELSE");
        mapGetName.put(6,"FOR");
        mapGetName.put(7,"FLOAT");
        mapGetName.put(8,"MAIN");
        mapGetName.put(9,"CHAR");

        mapGetName.put(10,"MENOR_QUE");
        mapGetName.put(11,"MAIOR_QUE");
        mapGetName.put(12,"MENOR_IGUAL");
        mapGetName.put(13,"DIFERENTE");
        mapGetName.put(14,"MAIOR_IGUAL");
        mapGetName.put(15,"IGUAL");

        mapGetName.put(16,"BARRA");
        mapGetName.put(17,"SOMA");
        mapGetName.put(18,"SUBTRACAO");
        mapGetName.put(19,"ASTERISCO");

        mapGetName.put(20,"DIGITO_FLOAT");
        mapGetName.put(21,"DIGITO_CHAR");
        mapGetName.put(22,"DIGITO_INTEIRO");

        mapGetName.put(23,"CHARACTER_MAL_FORMULADO");
        mapGetName.put(24,"COMENTARIO_NAO_FECHADO");
        mapGetName.put(25,"FLOAT_MAL_FORMATADO");
        mapGetName.put(26,"CHARACTER_INVALIDO");

        mapGetName.put(27,"PONTO_VIRGULA");
        mapGetName.put(28,"VIRGULA");
        mapGetName.put(29,"RECEBE");
        mapGetName.put(30,"UNDERLINE");
        mapGetName.put(31,"IDENTIFICADOR");
        mapGetName.put(32,"PARENTESE_ESQUERDO");
        mapGetName.put(33,"PARENTESE_DIREITO");
        mapGetName.put(34,"CHAVE_ESQUERDA");
        mapGetName.put(35,"CHAVE_DIREITA");
        mapGetName.put(36,"COLCHETE_ESQUERDO");
        mapGetName.put(37,"COLCHETE_DIREITO");

        mapGetName.put(38,"FIM_ARQUIVO");
    }
    private void populateMapGetCode(){

        mapGetCode.put("INT",1);
        mapGetCode.put("WHILE",2);
        mapGetCode.put("DO",3);
        mapGetCode.put("IF",4);
        mapGetCode.put("ELSE",5);
        mapGetCode.put("FOR",6);
        mapGetCode.put("FLOAT",7);
        mapGetCode.put("MAIN",8);
        mapGetCode.put("CHAR",9);

        mapGetCode.put("MENOR_QUE",10);
        mapGetCode.put("MAIOR_QUE",11);
        mapGetCode.put("MENOR_IGUAL",12);
        mapGetCode.put("DIFERENTE",13);
        mapGetCode.put("MAIOR_IGUAL",14);
        mapGetCode.put("IGUAL",15);

        mapGetCode.put("BARRA",16);
        mapGetCode.put("SOMA",17);
        mapGetCode.put("SUBTRACAO",18);
        mapGetCode.put("ASTERISCO",19);

        mapGetCode.put("DIGITO_FLOAT",20);
        mapGetCode.put("DIGITO_CHAR",21);
        mapGetCode.put("DIGITO_INTEIRO",22);

        mapGetCode.put("CHARACTER_MAL_FORMULADO",23);
        mapGetCode.put("COMENTARIO_NAO_FECHADO",24);
        mapGetCode.put("FLOAT_MAL_FORMATADO",25);
        mapGetCode.put("CHARACTER_INVALIDO",26);

        mapGetCode.put("PONTO_VIRGULA",27);
        mapGetCode.put("VIRGULA",28);
        mapGetCode.put("RECEBE",29);
        mapGetCode.put("UNDERLINE",30);
        mapGetCode.put("IDENTIFICADOR",31);
        mapGetCode.put("PARENTESE_ESQUERDO",32);
        mapGetCode.put("PARENTESE_DIREITO",33);
        mapGetCode.put("CHAVE_ESQUERDA",34);
        mapGetCode.put("CHAVE_DIREITA",35);
        mapGetCode.put("COLCHETE_ESQUERDO",36);
        mapGetCode.put("COLCHETE_DIREITO",37);

        mapGetCode.put("FIM_ARQUIVO",38);
    }

    public Integer getCode(String argumento){
        if(mapGetCode.containsKey(argumento.toUpperCase())){
            return mapGetCode.get(argumento.toUpperCase());
        }
        return 99;
    }
    public String getName(Integer argumento){
        if(mapGetName.containsKey(argumento)){
            return mapGetName.get(argumento);
        }
        return "";
    }
    public int helpParserComparerIdentificador(int i){
        if(i==getCode("INT")){
            return getCode("DIGITO_INTEIRO");
        }
        else if(i==getCode("FLOAT")){
            return getCode("DIGITO_FLOAT");
        }
        else if(i==getCode("CHAR")){
            return getCode("DIGITO_CHAR");
        }
        else if(i==getCode("DIGITO_INTEIRO")){
            return getCode("INT");
        }
        else if(i==getCode("DIGITO_FLOAT")){
            return getCode("FLOAT");
        }
        else if(i==getCode("DIGITO_CHAR")){
            return getCode("CHAR");
        }
        return 99;//flag
    }

}
