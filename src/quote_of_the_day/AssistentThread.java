package quote_of_the_day;

import app.AssistentRequestHandler;
import http.HttpMethod;
import http.Request;
import http.response.Response;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class AssistentThread implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    public AssistentThread(Socket socket) {
        this.socket = socket;

        try {
            //inicijalizacija ulaznog toka
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            //inicijalizacija izlaznog sistema
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            //svaki zahtev koji dolazi je po http standardu
            //u pozadini chrome samo kreira string i salje ga na input stream servera
            //sve sto unesemo kao url je http

            // uzimamo samo prvu liniju zahteva, iz koje dobijamo HTTP method i putanju
            String requestLine = in.readLine();

            StringTokenizer stringTokenizer = new StringTokenizer(requestLine);
            //po defaultu daje do prvog razmaka
            String method = stringTokenizer.nextToken();
            String path = stringTokenizer.nextToken();


            //na domacem cemo mozda parsirati header
            System.out.println("\nHTTP ZAHTEV KLIJENTA:\n");
            do {

                System.out.println(requestLine);
                requestLine = in.readLine();
            } while (!requestLine.trim().equals(""));



            Request request = new Request(HttpMethod.valueOf(method), path);
            //da bi request koji wrappuje tu metodu i putanju, mogao da prosledim controleru npr
            AssistentRequestHandler requestHandler = new AssistentRequestHandler();
            Response response = requestHandler.handle(request);

            System.out.println("\nHTTP odgovor:\n");
            System.out.println(response.getResponseString());
            //klasa response ima getResponseString koji printam i to je zapravo odgovor servera
            //koji mi saljemo klijentu
            out.println(response.getResponseString());

            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
