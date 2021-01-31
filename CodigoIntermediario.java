public class CodigoIntermediario {
    private String expressao;
    private Token token;
    private int numb;
    private boolean checked;

    CodigoIntermediario(){}
    CodigoIntermediario(String expressao, Token token, int numb){
        this.expressao=expressao;
        this.token = token;
        this.numb=numb;
        this.checked=false;
    }

    public String getExpressao(){
        return this.expressao;
    }
    public boolean isChecked(){
        return this.checked;
    }
    public int getNumb(){
        return this.numb;
    }
    public Token getToken(){
        return this.token;
    }
    public void setExpressao(String expressao){
        this.expressao = expressao;
    }
    public void setChecked(boolean checked){
        this.checked = checked;
    }
    public void setNumb(int numb){
        this.numb = numb;
    }
    public void setToken(Token token){
        this.token = token;
    }
}
