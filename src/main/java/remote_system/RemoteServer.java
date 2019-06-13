package remote_system;

public class RemoteServer {
    public static void main(String[] args) throws InterruptedException {
        Thread[] pool = new Thread[3];
        pool[0] = new Thread(new CuteScannerServer());
        pool[1] = new Thread(new CuteParserServer());
        pool[2] = new Thread(new CuteInterpreterServer());

        for(Thread t : pool)
            t.start();

        for(Thread t : pool)
            t.join();
    }
}
