package quote_of_the_day;

import http.ServerThread;
import model.Quote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class AssistentServer {

    public static final int TCP_PORT = 8070;
    public static ArrayList<Quote> quotes = new ArrayList<>();
    public static Quote quoteOfTheDay;

    public static void main(String[] args) {

        quotes.add(new Quote("George Lorimer","You have got to get up every morning with determination if you are going to go to bed with satisfaction."));
        quotes.add(new Quote("Mahatma Gandhi","Learn as if you will live forever, live like you will die tomorrow."));
        quotes.add(new Quote("Oscar Wilde","I am so clever that sometimes I do not understand a single word of what I am saying."));
        quotes.add(new Quote("Henry David Thoreau","Success usually comes to those who are too busy looking for it."));
        quotes.add(new Quote("Will Rogers","Do not let yesterday take up too much of today."));
        quotes.add(new Quote("Paulo Coelho","When we strive to become better than we are, everything around us becomes better too."));
        quotes.add(new Quote("Amelia Earhart","The most difficult thing is the decision to act, the rest is merely tenacity."));

        int i = new Random().nextInt(3);

        quoteOfTheDay = quotes.get(i);

        try {
            ServerSocket ss = new ServerSocket(TCP_PORT);
            while (true) {
                Socket sock = ss.accept();
                new Thread(new AssistentThread(sock)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //json za domaci
    /*
     * String json = ("{ime : stefan}");
     * Dodas klasu user koja ima samo jedno public polje
     *
     * Gson ima vec podrsku za to
     * Gson gson=new Gson();
     * User u = gson.fromJson(json, User.class);
     * Sout (u.ime)
     *
     * Suprotno kako da iz klase dobijem string
     *
     * u.prezime = "antic";
     *
     * Sout gson.toJson(u)
     * dajemo mu objekat koji zelimo da prebacimo u json
     *
     * posto smo stavili public polja znao bi da serijalizuje
     *
     * ovo je za domaci
     *
     * */


}

