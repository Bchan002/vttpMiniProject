package movieAppTracker.movieApp.models.movieModel;

public class MovieDetails {
    
    private String title;
    private String image;
    private double rating;
    private String genre;
    private int runtime;
    private String language;
    private String overview;

  
    private String userReviews;
    private String releaseDate;
    private String revenue;

    public String getRevenue() {
        return revenue;
    }
    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }
    public String getReleaseDate() {
        return releaseDate;
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
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
   
    public String getGenre() {
        return genre;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public int getRuntime() {
        return runtime;
    }
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    
    public String getUserReviews() {
        return userReviews;
    }
 
    public void setUserReviews(String userReviews) {
        this.userReviews = userReviews;
    }


    

}
