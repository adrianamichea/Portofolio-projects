import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;

class Admin {
    private User user;
    public static String adminId = "admin";

    public Admin(User user) {
        this.user = user;
    }

    public String getName() {
        return user.getName();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getAdminId() {
        return adminId;
    }

    public void addMoviesByConsole(Database database) throws ParseException {
        String title;
        String category;
        String releaseDate;
        int likes = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the title of the movie:");
        title = scanner.nextLine();
        System.out.println("Enter the category of the movie:");
        category = scanner.nextLine();
        System.out.println("Enter the release date of the movie:");
        releaseDate = scanner.nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Movie movie = new Movie(title, category, formatter.parse(releaseDate), likes);
        database.writeMovieToCSV(movie);
        System.out.println("Movie added successfully.");
    }

    public void addMoviesByFile(Database database) {
        String fileName;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the path of the file:");
        fileName = scanner.nextLine();
        database.writeMultipleMoviesToFile(fileName);
        System.out.println("Movies added successfully.");

    }

    public void doAdminTasks(Database database) {
        char option;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("1. Add movies by console");
            System.out.println("2. Add movies by file");
            System.out.println("3. Exit");
            option = scanner.nextLine().charAt(0);
            switch (option) {
                case '1':
                    try {
                        addMoviesByConsole(database);
                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                    break;
                case '2':
                    addMoviesByFile(database);
                    break;
                case '3':
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (option != '3');

    }
}
