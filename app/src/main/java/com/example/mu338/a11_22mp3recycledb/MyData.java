package com.example.mu338.a11_22mp3recycledb;

import java.io.File;

public class MyData {

    private String celeName;
    private String celeMusicName;
    private Integer albumImage;

    private File file;

    private String albumName;

    private String date;
    private String genre;

    private byte image;

    public MyData(File file) {
        this.file = file;
    }

    public MyData(String celeMusicName, String celeName) {
        this.celeMusicName = celeMusicName;
        this.celeName = celeName;
    }

    public MyData(String celeName, String celeMusicName, Integer albumImage) {
        this.celeName = celeName;
        this.celeMusicName = celeMusicName;
        this.albumImage = albumImage;
    }

    public MyData(String celeMusicName, String celeName, String albumName, String date, String genre) {
        this.celeMusicName = celeMusicName;
        this.celeName = celeName;
        this.albumName = albumName;
        this.date = date;
        this.genre = genre;
    }

    public MyData(String celeName, String celeMusicName, String albumName, String date, String genre, Byte image) {
        this.celeName = celeName;
        this.celeMusicName = celeMusicName;
        this.albumName = albumName;
        this.date = date;
        this.genre = genre;
        this.image = image;
    }



    public String getCeleName() {
        return celeName;
    }

    public void setCeleName(String celeName) {
        this.celeName = celeName;
    }

    public String getCeleMusicName() {
        return celeMusicName;
    }

    public void setCeleMusicName(String celeMusicName) {
        this.celeMusicName = celeMusicName;
    }

    public Integer getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(Integer albumImage) {
        this.albumImage = albumImage;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
