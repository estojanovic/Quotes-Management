package http.response;

import com.google.gson.Gson;
import model.Quote;

public class AssistentJsonResponse extends Response {

    private final Quote quote;

    public AssistentJsonResponse(Quote quote) {
        this.quote = quote;
    }

    @Override
    public String getResponseString() {
        Gson gson = new Gson();
        String quoteoftheday = gson.toJson(quote);

        String response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n";
        response += quoteoftheday;

        return response;
    }
}
