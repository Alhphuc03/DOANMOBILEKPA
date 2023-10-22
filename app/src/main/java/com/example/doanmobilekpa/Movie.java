package com.example.doanmobilekpa;

public class Movie {
    String Id;
    String Image;
    String Name;
    String Category;
    String Time;
    String ReleaseDate;
    String Description;

    boolean Favourite;

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    String Video;

    public Movie(String id, String image, String name, String category, String time, String releaseDate, String description, Boolean favourite, String video) {
        Id = id;
        Image = image;
        Name = name;
        Category = category;
        Time = time;
        ReleaseDate = releaseDate;
        Description = description;
        Favourite = favourite;
        Video = video;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isFavourite() {
        return Favourite;
    }

    public void setFavourite(boolean Favourite) {
        this.Favourite = Favourite;
    }

    public Movie(){};

}
