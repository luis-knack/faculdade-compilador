public class Auxiliar {

    public static String getErrorMessage(Token token,Codigo_Valores codigo, String erro, String fase){

        return "ERRO no "+fase+". Linha "+token.getLine()+", coluna "+token.getCol()+", Ultimo token lido "+token.getLexema()+": "+erro;
    }
}
