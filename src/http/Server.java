package http;

import model.Quote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    public static final int TCP_PORT = 8080;
    public static CopyOnWriteArrayList<Quote> quotelist = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {

        try {
            ServerSocket ss = new ServerSocket(TCP_PORT);
            while (true) {
                Socket sock = ss.accept();
                //osluskujemo nove konekcije, kad god dodje nova konekcija
                //otvaramo novi socket gde se obradjuje ta nova konekcija
                new Thread(new ServerThread(sock)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
