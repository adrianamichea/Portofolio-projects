import java.util.Date;

class Movie {
    private String title;
    private String category;
    private Date releaseDate;
    private int likes;

    public Movie(String title, String category, Date releaseDate, int likes) {
        this.title = title;
        this.category = category;
        this.releaseDate = releaseDate;
        this.likes = likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getLikes() {
        return likes;
    }

}
