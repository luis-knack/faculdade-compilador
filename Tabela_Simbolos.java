public class Tabela_Simbolos {
    
    private Token token;
    private int escopo;
    private int numb;
    private String lexema;

    public Tabela_Simbolos(Token token,int escopo, int numb,String lexema){
        this.token=token;
        this.escopo=escopo;
        this.lexema=lexema;
        this.numb=numb;
    }
    public Tabela_Simbolos(int escopo, int numb,String lexema){
        this.escopo=escopo;
        this.lexema=lexema;
        this.numb=numb;
    }
    public Tabela_Simbolos(){}

    public Token getToken(){
        return this.token;
    }
    public int getEscopo(){
        return this.escopo;
    }
    public String getLexema(){
        return this.lexema;
    }
    public int getNumb(){
        return this.numb;
    }
    public void setToken(Token token){
        this.token = token;
    }
    public void setLexema(String lexema){
        this.lexema = lexema;
    }
    public void setEscopo(int escopo){
        this.escopo = escopo;
    }
    public void setNumb(int numb){
        this.numb=numb;
    }

}
