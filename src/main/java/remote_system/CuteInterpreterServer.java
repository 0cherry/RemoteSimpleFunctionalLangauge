package remote_system;

import interpreter.CuteInterpreter;
import parser.ast.Node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class CuteInterpreterServer implements Runnable {
    private final static int CUTEINTERPRETER_PORT = 4003;

    public void run() {
        System.out.println("CuteInterpreter Server Start");
        ServerSocket server;

        System.out.println("-------waiting for connection------");
        try{
            server = new ServerSocket(CUTEINTERPRETER_PORT);
            while(true) {
                Socket connected_socket = server.accept();         // 클라이언트가 접속하면 통신할 수 있는 소켓 반환
                System.out.println(connected_socket.getInetAddress() + " request connection");

                ServerHandler handler = new ServerHandler(connected_socket);
                handler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ServerHandler extends Thread {
        private Socket connected_socket;
        private InputStream is;
        private ObjectInputStream ois;
        private OutputStream os;
        private ObjectOutputStream oos;
        private CuteInterpreter ci = new CuteInterpreter();

        private ServerHandler(Socket connected_socket) {
            this.connected_socket = connected_socket;
            // 클라이언트로부터 데이터를 주고받기 위한 Stream
            setObjectInputStream();
            setObjectOutputStream();
        }

        public void run() {
            try {
                while (true) {
                    // 클라이언트로부터 받은 입력 데이터를 CuteInterprete
                    Object receive_data = ois.readObject();
                    Node sendData = null;
                    if(receive_data == null)
                        break;
                    if(receive_data instanceof Node)
                        sendData = ci.runExpr((Node) receive_data);

                    // CuteInterprete 결과 클라이언트에 보내기
                    oos.reset();
                    oos.writeObject(sendData);
                    System.out.println("**** transmit to " + connected_socket.getInetAddress() + " completed ****");
                }
            } catch (SocketException se) {
                System.out.println(connected_socket.getInetAddress() + " is disconnected");
                try {
                    connected_socket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setObjectOutputStream() {
            try {
                os = connected_socket.getOutputStream();
                oos = new ObjectOutputStream(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setObjectInputStream() {
            try {
                is = connected_socket.getInputStream();
                ois = new ObjectInputStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
