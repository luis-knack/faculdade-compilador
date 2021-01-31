import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

    private static String argumento_arquivo;
    public static void main(String []args) throws Exception {

        if(args.length!=1){
            System.out.println("ERRO. Passe apenas um argumento, que deve ser o endereço do arquivo.");
            System.exit(0);
        }

        argumento_arquivo = args[0];
        FileReader fr = new FileReader(argumento_arquivo);
        BufferedReader buff = new BufferedReader(fr);

        Scanner scan = new Scanner(buff);
        Parser parser = new Parser(scan);

        parser.start();

        parser.readAllIntermediateCodes();

    }

}