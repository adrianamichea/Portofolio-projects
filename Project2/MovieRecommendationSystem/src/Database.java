import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {
  private List<User> users;
  private List<Movie> movies;

  public Database() {
    this.users = new ArrayList<>();
    this.movies = new ArrayList<Movie>();
  }

  public void addUser(User user) {
    users.add(user);
  }

  public void addMovie(Movie movie) {
    movies.add(movie);
  }

  public List<User> getUsers() {
    return users;
  }

  public List<Movie> getMovies() {
    return movies;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public void setMovies(List<Movie> movies) {
    this.movies = movies;
  }

  public void readMovies(String csvFile) {
    String line = "";
    String cvsSplitBy = ",";

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      while ((line = br.readLine()) != null) {
        String[] data = line.split(cvsSplitBy);
        String title = data[0];
        String category = data[1];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate = formatter.parse(data[2]);
        int likes = Integer.parseInt(data[3]);
        Movie movie = new Movie(title, category, releaseDate, likes);
        movies.add(movie);
      }
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
  }

  public void readUsers(String csvFile) {
    String line = "";
    String cvsSplitBy = ",";

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
      while ((line = br.readLine()) != null) {
        String[] data = line.split(cvsSplitBy);
        String name = data[0];
        String password = data[1];
        User user = new User(name, password);
        users.add(user);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void populateUserDatabase() {
    String csvFile = "src/Users.csv";
    readUsers(csvFile);
  }

  public void printUsers() {
    for (User user : users) {
      System.out.println(user.getName() + " " + user.getPassword());
    }
  }

  public void populateMovieDatabase() {
    String csvFile = "src/Movies.csv";
    readMovies(csvFile);
  }

  public void printMovies() {
    for (Movie movie : movies) {
      System.out.println(
          movie.getTitle() + " " + movie.getCategory() + " " + movie.getReleaseDate() + " " + movie.getLikes());
    }

  }

  public void writeMovieToCSV(Movie movie) {
    addMovie(movie);
    String csvFile = "src/Movies.csv";
    String csvSplitBy = ",";
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile, true))) {
      String line = movie.getTitle() + csvSplitBy + movie.getCategory() + csvSplitBy
          + formatter.format(movie.getReleaseDate()) + csvSplitBy + movie.getLikes();
      bw.write(line);
      bw.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeUserToCSV(User user) {
    String csvFile = "src/Users.csv";
    String csvSplitBy = ",";
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile, true))) {
      String line = user.getName() + csvSplitBy + user.getPassword();
      bw.write(line);
      bw.newLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeMultipleMoviesToFile(String csvFile) {
    readMovies(csvFile);
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/Movies.csv", true))) {
      String line = "";
      String cvsSplitBy = ",";
      try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        while ((line = br.readLine()) != null) {
          String[] data = line.split(cvsSplitBy);
          String title = data[0];
          String category = data[1];
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
          Date releaseDate = formatter.parse(data[2]);
          int likes = Integer.parseInt(data[3]);
          Movie movie = new Movie(title, category, releaseDate, likes);
          movies.add(movie);
          bw.write(line);
          bw.newLine();
        }
      } catch (IOException | ParseException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public User findUser(String name, String password) {
    for (User user : users) {
      if (user.getName().equals(name) && user.getPassword().equals(password)) {
        return user;
      }
    }
    return null;
  }

  public List<Movie> searchMoviesByName(String name) {
    List<Movie> results = new ArrayList<>();
    Pattern pattern = Pattern.compile(name, Pattern.CASE_INSENSITIVE);

    for (Movie movie : movies) {
      Matcher matcher = pattern.matcher(movie.getTitle());
      if (matcher.find()) {
        results.add(movie);
      }
    }
    return results;
  }

  public void printMovie(Movie movie) {
    DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
    System.out
        .println(movie.getTitle() + "  " + movie.getCategory() + "  " + dateFormat.format(movie.getReleaseDate()) + "  "
            + movie.getLikes());
  }

  public void incrementLikesofMovie(Movie movie) {
    boolean movieFound = false;

    for (Movie m : movies) {
      if (m.getTitle().equals(movie.getTitle()) && m.getCategory().equals(movie.getCategory())
          && m.getReleaseDate().equals(movie.getReleaseDate())) {
        m.setLikes(m.getLikes() + 1);
        movieFound = true;
        break;
      }
    }
    if (!movieFound) {
      System.out.println("Movie not found");
    }
  }

  public List<Movie> showRandomMovies(int number) {
    List<Movie> listOfMovies = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < number; i++) {
      int index = random.nextInt(movies.size());
      printMovie(movies.get(index));
      listOfMovies.add(movies.get(index));
    }
    return listOfMovies;
  }
}
