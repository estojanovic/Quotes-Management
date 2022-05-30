package model;

public class Quote {
    private String author;
    private String quotetext;

    public Quote(String author, String quotetext) {
        this.author = author;
        this.quotetext = quotetext;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuotetext() {
        return quotetext;
    }

    public void setQuotetext(String quotetext) {
        this.quotetext = quotetext;
    }
}
