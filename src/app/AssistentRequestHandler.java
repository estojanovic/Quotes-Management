package app;

import http.HttpMethod;
import http.Request;
import http.response.Response;

public class AssistentRequestHandler {

    //na osnovu putanje koja je poslata radi nesto i vrati nesto sto nasledjuje apstraktnu klasu response
    //odlucuje koji kontroler ce da pozove
    public Response handle(Request request) throws Exception {
        if (request.getPath().equals("/quote-of-the-day") && request.getHttpMethod().equals(HttpMethod.GET)) {
            return (new AssistentController(request)).doGet();
            //instanciraj taj kontroler i uradi odgovarajucu metodu
        }

        throw new Exception("Page: " + request.getPath() + ". Method: " + request.getHttpMethod() + " not found!");
    }
}
