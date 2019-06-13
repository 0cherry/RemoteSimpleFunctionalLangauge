package client;

import lexer.CuteScanner;
import lexer.Token;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class CuteParserClient extends Thread {
    private final static String HOST = "168.188.128.138";
    private final static int PORT = 4002;
    private Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        CuteParserClient cm = new CuteParserClient();
        cm.run();
    }

    public void run(){
        Socket socket;
        OutputStream os;
        ObjectOutputStream oos;

        InputStream is;
        ObjectInputStream ois;
        CuteScanner cs = new CuteScanner();

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
                // 오브젝트 전송
                oos.writeObject(token_list);
                // 오브젝트 송신
                Object receiveData = ois.readObject();
                System.out.println("<< " + receiveData);
            }
        } catch(SocketException e) {
            System.out.println("server is closed");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
