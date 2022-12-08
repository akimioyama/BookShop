package com.example.bookshop;

public class Book {
    private int id;
    private String name;
    private String author;
    private String description;
    private String price;
    private String publisher;
    private String year;
    private String pages;
    private String img_url;

    public Book(Integer id, String name, String author, String description,
                String price, String publisher, String year, String pages, String img_url){
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.price = price;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.img_url = img_url;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getAuthor(){
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getPublisher() {
        return this.publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getYear() {
        return this.year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getPages() {
        return this.pages;
    }
    public void setPages(String pages) {
        this.pages = pages;
    }
    public String getImg_url() {
        return this.img_url;
    }
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
