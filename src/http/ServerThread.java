package http;

import app.RequestHandler;
import http.response.Response;
import model.Quote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ServerThread(Socket sock) {
        this.client = sock;
        //uzimamo socket koji smo dobili kad smo kreirali konekciju

        try {
            //inicijalizacija ulaznog toka
            in = new BufferedReader(
                    new InputStreamReader(
                            client.getInputStream()));

            //inicijalizacija izlaznog sistema
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    client.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

            String contentLength = "";
            //na domacem cemo mozda parsirati header
            System.out.println("\nHTTP ZAHTEV KLIJENTA:\n");
            do {
                if (requestLine.contains("Content-Length")){
                    contentLength = requestLine;
                }
                System.out.println(requestLine);
                requestLine = in.readLine();
            } while (!requestLine.trim().equals(""));

            if (method.equals(HttpMethod.POST.toString())) {
                // TODO: Ako je request method POST, procitaj telo zahteva (parametre)

                //post zahtevi mogu da imaju telo koje dolazi nakon dve linije dilimitera
                //kada znamo da citamo sa tela? kada znamo da smo procitali onoliki broj bajtova koji
                //nam je prosledio klijent

                //poenta je da procitamo taj header pre nego sto zatvorimo string
                //citamo header, isparsiramo content-length i dobijemo broj

                Integer length = Integer.parseInt((contentLength.split(" "))[1]);

                char[] buffer = new char[length];

                System.out.println("content length : " + contentLength);
                System.out.println("buffer : " + buffer);
                System.out.println("content length number : " + length);

                //na input streamu imam read gde ce da kaze da unese ono sto je procitao

                in.read(buffer);

                //bice napunjen karakterima iz tela odgovora

                String content = new String(buffer);
                System.out.println("content : " + content);


                //author=ema&quote=emica+je+najbolja
                try {
                    String contents[] = content.split("&");
                    String author = (contents[0].split("="))[1];
                    String quotetext = (contents[1].split("="))[1];


                    Quote quote = new Quote(URLDecoder.decode(author, StandardCharsets.UTF_8.name()), URLDecoder.decode(quotetext, StandardCharsets.UTF_8.name()));

                    System.out.println("author : " + author);
                    System.out.println("quotetext : " + quotetext);

                    Server.quotelist.add(quote);
                } catch (ArrayIndexOutOfBoundsException e){
                    System.out.println("Enter author and quote");
                }



                //mi ga sami spojimo, splitujemo po =, trazimo email i uradimo save
                //kako da izvuces content length? mozes da citas sve do kraja
                //in.read() proba da cita pa ga radi u nekom while-u samo izvlacimo karakter po karakter
                //sve dok ne dodje do kraja

            }

            Request request = new Request(HttpMethod.valueOf(method), path);
            //da bi request koji wrappuje tu metodu i putanju, mogao da prosledim controleru npr
            RequestHandler requestHandler = new RequestHandler();
            Response response = requestHandler.handle(request);

            System.out.println("\nHTTP odgovor:\n");
            System.out.println(response.getResponseString());
            //klasa response ima getResponseString koji printam i to je zapravo odgovor servera
            //koji mi saljemo klijentu
            out.println(response.getResponseString());

            in.close();
            out.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
