public class Token {
    public String lexema;
    public int col;
    public int line;
    public int numb;

    public Token(){
        lexema="";
    }

    public Token(String lexema, int col, int line, int numb){
        this.col = col;
        this.line = line;
        this.numb = numb;
        this.lexema = lexema;
    }
    
    public String getLexema(){
        return this.lexema;
    }
    public int getCol(){
        return this.col;
    }
    public int getLine(){
        return this.line;
    }
    public int getNumb(){
        return this.numb;
    }
    public void setLexema(String lexema){
        this.lexema=lexema;
    }
    public void setCol(int col){
        this.col=col;
    }
    public void setLine(int line){
        this.line=line;
    }
    public void setNumb(int numb){
        this.numb=numb;
    }
}
