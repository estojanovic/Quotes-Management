package app;

import http.HttpMethod;
import http.Request;
import http.response.Response;

public class RequestHandler {

    //na osnovu putanje koja je poslata radi nesto i vrati nesto sto nasledjuje apstraktnu klasu response
    //odlucuje koji kontroler ce da pozove
    public Response handle(Request request) throws Exception {
        if (request.getPath().equals("/quotes") && request.getHttpMethod().equals(HttpMethod.GET)) {
            return (new NewsletterController(request)).doGet();
            //instanciraj taj kontroler i uradi odgovarajucu metodu
        } else if (request.getPath().equals("/save-quote") && request.getHttpMethod().equals(HttpMethod.POST)) {
            return (new NewsletterController(request)).doPost();
        }

        throw new Exception("Page: " + request.getPath() + ". Method: " + request.getHttpMethod() + " not found!");
    }
}
