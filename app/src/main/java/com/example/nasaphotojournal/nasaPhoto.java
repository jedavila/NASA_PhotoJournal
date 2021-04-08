package com.example.nasaphotojournal;

public class nasaPhoto {

    public String iconUrl;
    public String name;
    public String imageUrl;
    public String size;

    public nasaPhoto() {
    }

    public nasaPhoto(String iconUrl, String name, String imageUrl, String size) {
        this.iconUrl = "https://photojournal.jpl.nasa.gov"+ iconUrl;
        this.name = name;
        this.imageUrl = "https://photojournal.jpl.nasa.gov"+ imageUrl;
        this.size = size;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSize() {
        return size;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
