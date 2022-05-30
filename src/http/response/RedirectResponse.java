package http.response;

public class RedirectResponse extends Response{
    private String location;

    public RedirectResponse(String location) {
        this.location = location;
    }

    @Override
    public String getResponseString() {
        String response = "HTTP/1.1 301 OK\r\nLocation: " + this.location +"\r\n\r\n";

        //potrebno je i da unesemo negde tu lokaciju, to cemo u newsletter controlleru

        return response;
    }
}
