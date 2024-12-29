package movieAppTracker.movieApp.models;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Notes {
    
    

    @Min(value=0)
    @Max(value=10)
    public int ratings;

    @NotBlank(message="Watch Status cannot be empty")
    public String watchStatus;

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getWatchStatus() {
        return watchStatus;
    }

    public void setWatchStatus(String watchStatus) {
        this.watchStatus = watchStatus;
    }

    
}
