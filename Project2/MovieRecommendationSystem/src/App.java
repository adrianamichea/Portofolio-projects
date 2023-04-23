import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Database database = new Database();
        database.populateMovieDatabase();
        database.populateUserDatabase();
        String username = "";
        String password = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Movie Database!");
        System.out.println("Please enter your username and password to login.");
        User user = null;
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Username: ");
            username = scanner.nextLine();
            System.out.print("Password: ");
            password = scanner.nextLine();

            user = database.findUser(username, password);
            if (user == null) {
                System.out.println("Incorrect username or password.");
            } else {
                loggedIn = true;
            }
        }
        if (user != null) {
            System.out.println("Welcome, " + username + "!");
            System.out.println("Do you want to enter as an admin or as an normal user? (admin/user)");
            String userType = scanner.nextLine();
            if (userType.equals("admin")) {
                System.out.println("Enter the admin ID: ");
                String adminId = scanner.nextLine();
                if (adminId.equals(Admin.adminId)) {
                    Admin admin = new Admin(user);
                    admin.doAdminTasks(database);
                }
            } else if (userType.equals("user")) {
                NormalUser normalUser = new NormalUser(user);
                normalUser.doUserTasks(database);
            } else
                System.out.println("Invalid user type.");
        }
        scanner.close();
    }

}
