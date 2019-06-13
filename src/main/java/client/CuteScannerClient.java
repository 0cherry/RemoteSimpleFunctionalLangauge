package client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CuteScannerClient extends Thread {
    private final static String HOST = "168.188.128.138";
    private final static int PORT = 4001;
    private Scanner input = new Scanner(System.in);
    private Socket socket;
    private OutputStream os;
    private ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream ois;

    public static void main(String[] args) {
        CuteScannerClient cm = new CuteScannerClient();
        cm.run();
    }

    public CuteScannerClient() {
        try {
            socket = new Socket(HOST, PORT);
            // 서버로 전송을 위한 OutputStream
            setObjectOutputStream();
            // 서버에 대한 송신을 위한 InputStream
            setObjectInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            while(true) {
                System.out.print(">> ");
                Object in = input.nextLine();
                // 오브젝트 전송
                oos.writeObject(in);
                // 오브젝트 송신
                Object receiveData = ois.readObject();
                System.out.println("<< " + receiveData);
            }
        } catch(SocketException e) {
            System.out.println("server is closed");
        } catch(NoSuchElementException e) {
            System.out.println("");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setObjectInputStream() {
        try {
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setObjectOutputStream() {
        try {
            os = socket.getOutputStream();
            oos = new ObjectOutputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
