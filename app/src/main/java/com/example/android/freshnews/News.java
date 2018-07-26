package com.example.android.freshnews;

class News {
    private String Section;
    private String Title;
    private String Author;
    private String Date;
    private String URL;

    News(String section, String title, String author, String date, String url) {
        Section = section;
        Title = title;
        Author = author;
        Date = date;
        URL = url;
    }

    public String getSection() {
        return Section;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getDate() {
        return Date;
    }

    public String getURL() {
        return URL;
    }
}
