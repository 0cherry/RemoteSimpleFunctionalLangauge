package client;

import interpreter.OriginalPrettyPrinter;
import lexer.CuteScanner;
import lexer.Token;
import parser.ast.Node;
import parser.parse.CuteParser;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class CuteInterpreterClient extends Thread {
    private final static String HOST = "168.188.128.138";
    private final static int PORT = 4003;
    private Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        CuteInterpreterClient cm = new CuteInterpreterClient();
        cm.run();
    }

    public void run(){
        Socket socket;
        OutputStream os;
        ObjectOutputStream oos;

        InputStream is;
        ObjectInputStream ois;
        CuteScanner cs = new CuteScanner();
        CuteParser cp = new CuteParser();

        try{
            socket = new Socket(HOST, PORT);
            // 서버로 전송을 위한 OutputStream
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);

            // 서버에 대한 송신을 위한 InputStream
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);

            while(true) {
                System.out.print(">> ");
                String in = input.nextLine();
                ArrayList<Token> token_list = cs.scan(in);
                cp.setTokenList(token_list);
                Object parse_tree = cp.parseExpr();
                // 오브젝트 전송
                oos.writeObject(parse_tree);
                if(in.equals("exit")) {
                    socket.close();
                    break;
                }
                // 오브젝트 송신
                Object receiveData = ois.readObject();
                System.out.print("<< ");
                OriginalPrettyPrinter.getPrinter(System.out).prettyPrint((Node) receiveData);
            }
        } catch(SocketException e) {
            System.out.println("server is closed");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
