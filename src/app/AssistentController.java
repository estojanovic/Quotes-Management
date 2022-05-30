package app;

import http.Request;
import http.response.Response;
import model.Quote;
import http.response.AssistentJsonResponse;
import quote_of_the_day.AssistentServer;

public class AssistentController extends Controller {
    private AssistentJsonResponse response;

    public AssistentController(Request request) {
        super(request);
    }

    @Override
    public Response doGet() {

        Quote quote = AssistentServer.quoteOfTheDay;
        response = new AssistentJsonResponse(quote);
        return response;
    }

    @Override
    public Response doPost() {
        return null;
    }
}
