import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class NormalUser {
  private User user;
  private List<Movie> listOfLikedMovies;
  private boolean add;

  public NormalUser(User user) {
    this.user = user;
    this.listOfLikedMovies = new ArrayList<Movie>();
  }

  public void searchMoviesByTitle(Database database) {
    Scanner scanner = new Scanner(System.in);
    String pattern = "";
    List<Movie> results = null;

    while (results == null) {
      System.out.print("Enter movie name pattern: ");
      pattern = scanner.nextLine();

      if (pattern.equals("*")) {
        pattern = ".*";
      }

      try {
        results = database.searchMoviesByName(pattern);
      } catch (PatternSyntaxException e) {
        System.out.println("Invalid regular expression. Please enter a valid pattern.");
      }
    }

    if (results.isEmpty()) {
      System.out.println("No movies found for pattern: " + pattern);
    } else {
      System.out.println("Movies found for pattern: " + pattern);
      int index = 1;
      System.out.println("Title Category ReleaseDate Likes");
      DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
      for (Movie movie : results) {
        System.out.println(index + ". " + movie.getTitle() + " " + movie.getCategory() + " "
            + dateFormat.format(movie.getReleaseDate())
            + " " + movie.getLikes());

        index++;
      }
      likeMovie(database, results);
    }
  }

  public void likeMovie(Database database, List<Movie> listOfMovies) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Do you want to like a movie? (y/n): ");
    char option = scanner.next().charAt(0);
    if (option == 'n') {
      return;
    }
    System.out.print("Enter the index of the movie you'll like to heart: ");
    int index = scanner.nextInt();
    Movie movie = listOfMovies.get(index - 1);
    listOfLikedMovies.add(movie);
    System.out.println("You liked " + movie.getTitle());
    database.incrementLikesofMovie(movie);
    likeMovie(database, listOfMovies);
  }

  public void showFeedOfMovies(Database database) {
    if (listOfLikedMovies.isEmpty()) {
      System.out.println("You have not liked any movies yet. Showing random movies.");
      System.out.println("Title Category ReleaseDate Likes");
      List<Movie> listOfMovies = database.showRandomMovies(10);
      likeMovie(database, listOfMovies);
    } else {
      System.out.println("Based on your likes, we recommend the following movies:");
      List<Movie> recommendedMovies = getRecommendedMovies(database.getMovies());
      int index = 1;
      System.out.println("Title Category ReleaseDate Likes");
      DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
      for (Movie movie : recommendedMovies) {
        System.out.println(movie.getTitle() + " " + movie.getCategory() + " "
            + dateFormat.format(movie.getReleaseDate())
            + " " + movie.getLikes());
        index++;
        if (index > 10) {
          break;
        }
      }
      if (index < 10) {
        recommendedMovies.addAll(database.showRandomMovies(10 - index));
        likeMovie(database, recommendedMovies);
      }

    }
  }

  public void doUserTasks(Database database) {
    char option;
    Scanner scanner = new Scanner(System.in);
    do {
      System.out.println("1. Search movies by title");
      System.out.println("2. Show feed of movies");
      System.out.println("3. Exit");
      option = scanner.next().charAt(0);
      switch (option) {
        case '1':
          searchMoviesByTitle(database);
          break;
        case '2':
          showFeedOfMovies(database);
          break;
        case '3':
          System.out.println("Exiting...");
          System.exit(0);
          break;
        default:
          System.out.println("Invalid option. Try again.");
      }
    } while (option != '4');
  }

  public List<Movie> getRecommendedMovies(List<Movie> allMovies) {
    Map<String, Long> categoryCounts = listOfLikedMovies.stream()
        .map(Movie::getCategory)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    String mostLikedCategory = categoryCounts.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("");
    String answer;
    Scanner scanner = new Scanner(System.in);
    System.out.print("Do you want them sorted by release date or likes?(date/likes) ");
    answer = scanner.nextLine();
    while (!answer.equals("date") && !answer.equals("likes")) {
      System.out.print("Invalid input. Do you want them sorted by release date or likes?(date/likes) ");
      answer = scanner.nextLine();
    }
    if (answer.equals("date")) {
      return allMovies.stream()
          .filter(movie -> movie.getCategory().equals(mostLikedCategory))
          .sorted(Comparator.comparing(Movie::getReleaseDate))
          .collect(Collectors.toList());

    } else {
      return allMovies.stream()
          .filter(movie -> movie.getCategory().equals(mostLikedCategory))
          .sorted(Comparator.comparing(Movie::getLikes).reversed())
          .collect(Collectors.toList());
    }
  }
}
