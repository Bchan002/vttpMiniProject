package movieAppTracker.movieApp.models;

import java.time.LocalDate;

public class watchList {
    
    private String id;
    private String title;
    private String posterImage;
    private int rating;
    private String watchStatus;
    private String date;

    public String getId() {
        return id;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPosterImage() {
        return posterImage;
    }
    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public String getWatchStatus() {
        return watchStatus;
    }
    public void setWatchStatus(String watchStatus) {
        this.watchStatus = watchStatus;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    

}
