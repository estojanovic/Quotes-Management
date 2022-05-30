package app;

import com.google.gson.Gson;
import http.Request;
import http.Server;
import http.response.HtmlResponse;
import http.response.RedirectResponse;
import http.response.Response;
import model.Quote;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicReference;

public class NewsletterController extends Controller {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private Quote quoteOfTheDay;


    //sadrzi biznis logiku i zaduzen je da isprinta html stranicu
    public NewsletterController(Request request) {
        super(request);
    }

    @Override
    public Response doGet() {
        Gson gson = new Gson();

        try {
            socket = new Socket("localhost", 8070);
            //inicijalizacija ulaznog toka
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            //inicijalizacija izlaznog sistema
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())), true);

            out.println("GET /quote-of-the-day HTTP/1.1\n" + "Accept: application/json\r\n\r\n");

            String requestLine = in.readLine();

            StringTokenizer stringTokenizer = new StringTokenizer(requestLine);
            //po defaultu daje do prvog razmaka
            String method = stringTokenizer.nextToken();
            String path = stringTokenizer.nextToken();

            System.out.println("\nHTTP ZAHTEV KLIJENTA:\n");
            do {
                System.out.println(requestLine);
                requestLine = in.readLine();
            } while (!requestLine.trim().equals(""));


            quoteOfTheDay = gson.fromJson(in.readLine(),Quote.class);

        } catch (IOException e) {
            e.printStackTrace();
        }


        //probace da uradi zahtev na newsletter metodi
        //da bi u formi naznacio koju putanju ce submit activirati za to imamo action atribut
        //ovom formom radimo post zahtev na metodu applu
        String htmlBody = "" +
                "<form method=\"POST\" action=\"save-quote\">" +
                "<label>Author: </label><input name=\"author\" type=\"author\"><br><br>" +
                "<label>Quote: </label><input name=\"quote\" type=\"quote\"><br><br>" +
                "<button>Save Quote</button>" +
                "<h2>Quote of the day:</h2>" + makeParagraphHtml(quoteOfTheDay)+
                "<h1>Saved Quotes</h1>" + makeQuoteHtml() +
                "</form>";

        String content = "<html><head><title>Quotes Management</title></head>\n";
        content += "<body><center>" + htmlBody + "</center></body></html>";

        return new HtmlResponse(content);
        //response koji postuje http sadrzaj
    }

    @Override
    public Response doPost() {
        // TODO: obradi POST zahtev
        //ideja je da vrati neki konkretan odgovor


        //kako ga je sacuvao? ono sto se naslo u telu zahteva bilo je email=email@gmail.com

       // return new HtmlResponse("OK");

        //TIME ne vrti stalno nego vraca ok
        //kada radi refresh te stranice ok ponovo se salje zahtev post ka apply
        // da smo imali bazu ponovo bi nam uneo u bazu

        //ideja je da nakon post zahteva gde imamo neku notifikaciju na nivou baze
        //praksa je da se ne vrati html nego neka redirekcija na postojecu stranicu
        //sacuvam to sto sam setovao u fieldu i vratim redirekciju na prvu stranicu

        //return null;

        return new RedirectResponse("/quotes");
    }

    public String makeQuoteHtml(){
        AtomicReference<String> htmlQuotes = new AtomicReference<>("");
        Server.quotelist.iterator().forEachRemaining((Quote q)->{
              htmlQuotes.set(htmlQuotes + makeParagraphHtml(q)+"<hr>");
        });
        return htmlQuotes.get();
    }

    public String makeParagraphHtml(Quote q){
        String html = "<p> " + q.getAuthor() + ": \""+ q.getQuotetext() + "\" " +"</p>";
        return html;
    }

}
