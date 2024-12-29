package movieAppTracker.movieApp.models.movieModel;

public class Movie {
    
    private int id;
    private String overview;
    private String poster;
    private String releaseDate;
    private String title;
    private Double rating;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
    
    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public Double getRating() {
        return rating;
    }
    public void setRating(Double rating) {
        this.rating = rating;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
     


    


}
