import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * COMP 2150 Summer 23 Assignment 2: Hotel reservations client
 */

/**
 * Main program: read in a floor plan and reservation requests.
 * /**
   * A2Client class
   *
   * COMP 2150 SECTION A01
   * INSTRUCTOR    Lauren Himbeaul
   * ASSIGNMENT    Assignment 2, question 1
   * @author       Ochiagha ifeanyi, 7900465
   * @version      26th oct 2023
   *
   * REMARKS: This runs the commands passed in by the user to do different things such as make a reservation, change password of a user and so much more. 
   */

 
public class A2Client {
  public static void main(String[] args) {
    ReservationServer server = new A2Server();

    Scanner in = new Scanner(System.in);
    boolean quit = false;
    String response;
    
    response = server.readUserFile("a2users.txt");
    if (response != null) {
      System.out.println(response);
    } else {
      while (!quit) {
        quit = login(in, server);
        if (!quit) {
          response = server.loginType();
          assert null != response;
          if ("customer".equals(response)) {
            handleCustomerRequests(in, server);
          } else if ("manager".equals(response)) {
            handleManagerRequests(in, server);
          } else if ("administrator".equals(response)) {
            handleAdministratorRequests(in, server);
          }
        }
      }
    }
    
    server.exit();

    System.out.println("\nEnd of processing.");
  }
  
  public static boolean login(Scanner in, ReservationServer server) {
    String user, password, response;
    boolean quit = false;
    
    while (!quit) {
      System.out.println("\nWelcome to Reserv-a-tron!");
      user = nextNonEmptyLine(in, "Please enter your user ID to login or \"QUIT\" to quit: ");
      if (user != null) {
        if (user.equals("QUIT")) {
          quit = true;
        } else {
          password = nextNonEmptyLine(in, "Welcome " + user + ", please enter your password: ");
          if (password != null) {
            response = server.login(user, password);
            if (response != null) {
              System.out.print("Unable to login as " + user + ": ");
              System.out.println(response);
            } else {
              return false;
            }
          }
        }
      }
    }
    
    return true;
  }
  
  public static void handleCustomerRequests(Scanner in, ReservationServer server) {
    int option = -1;
    String line = "", response;

    while (option != 0) {
      option = menuOption(in, "\nWelcome " + server.currentUserID() + "! Enter a number to select an option:\n" +
        "   1 - Make a reservation\n" +
        "   2 - Cancel a reservation\n" + 
        "   3 - View your current reservations\n" +
        "   0 - Log out", 0, 3);
      if (option == 1) {
        try {
          line = nextNonEmptyLine(in, "How many guests in the reservation? ");
          int numGuests = Integer.parseInt(line);
          line = nextNonEmptyLine(in, "Enter the starting day of the reservation (1-365): ");
          int start = Integer.parseInt(line);
          line = nextNonEmptyLine(in, "How many days will the reservation be? ");
          int duration = Integer.parseInt(line);
          response = server.makeReservation(server.currentUserID(), numGuests, start, duration);
          if (response != null) {
            System.out.println(response);
          }
        } catch (NumberFormatException nfe) {
          System.out.println("Invalid input: " + line);
        }
      } else if (option == 2) {
        try {
          line = nextNonEmptyLine(in, "Enter the starting day to cancel (1-365): ");
          response = server.cancelReservation(server.currentUserID(), Integer.parseInt(line));
          if (response != null) {
            System.out.println(response);
          }
        } catch (NumberFormatException nfe) {
          System.out.println("Invalid input: " + line);
        }
      } else if (option == 3) {
        System.out.println(server.printCustomer(server.currentUserID()));
      }
    }

    if ((response = server.logout()) != null) {
      System.out.println(response);
    }
  }

  public static void handleManagerRequests(Scanner in, ReservationServer server) {
    int option = -1;
    String line = "", response, customer = null;

    while (option != 0) {
      option = menuOption(in, "\nWelcome " + server.currentUserID() + " (manager)! Enter a number to select an option:\n" +
        "   1 - Make a reservation for a customer\n" +
        "   2 - Cancel a reservation for a customer\n" + 
        "   3 - View your current reservations for a customer\n" +
        "   4 - View your current reservations for a day\n" +
        "   5 - View your current reservations for a room\n" +
        "   0 - Log out", 0, 5);
      
      if (option >= 1 && option <= 3) {
        customer = nextNonEmptyLine(in, "What is the customer's user ID? ");
        if (!"customer".equals(server.loginTypeID(customer))) {
          System.out.println("Invalid user ID " + customer);
          customer = null;
        }
      }
      
      if (option == 1 && customer != null) {
        {
          try {
            line = nextNonEmptyLine(in, "How many guests in the reservation? ");
            int numGuests = Integer.parseInt(line);
            line = nextNonEmptyLine(in, "Enter the starting day of the reservation (1-365): ");
            int start = Integer.parseInt(line);
            line = nextNonEmptyLine(in, "How many days will the reservation be? ");
            int duration = Integer.parseInt(line);
            response = server.makeReservation(customer, numGuests, start, duration);
            if (response != null) {
              System.out.println(response);
            }
          } catch (NumberFormatException nfe) {
            System.out.println("Invalid input: " + line);
          }
        }
      } else if (option == 2 && customer != null) {
        try {
          line = nextNonEmptyLine(in, "Enter the starting day to cancel (1-365): ");
          response = server.cancelReservation(customer, Integer.parseInt(line));
          if (response != null) {
            System.out.println(response);
          }
        } catch (NumberFormatException nfe) {
          System.out.println("Invalid input: " + line);
        }
      } else if (option == 3 && customer != null) {
        System.out.println(server.printCustomer(customer));
      } else if (option == 4) {
        try {
          line = nextNonEmptyLine(in, "Enter a day (1-365): ");
          response = server.printDay(Integer.parseInt(line));
          if (response != null) {
            System.out.println(response);
          }
        } catch (NumberFormatException nfe) {
          System.out.println("Invalid input: " + line);
        }
      } else if (option == 5) {
        try {
          line = nextNonEmptyLine(in, "Enter a room: ");
          response = server.printRoom(Integer.parseInt(line));
          if (response != null) {
            System.out.println(response);
          }
        } catch (NumberFormatException nfe) {
          System.out.println("Invalid input: " + line);
        }
      }
    }

    if ((response = server.logout()) != null) {
      System.out.println(response);
    }
  }

  public static void handleAdministratorRequests(Scanner in, ReservationServer server) {
    int option = -1;
    String user, name, type, password, response;

    while (option != 0) {
      option = menuOption(in, "\nWelcome " + server.currentUserID() + " (administrator)! Enter a number to select an option:\n" +
        "   1 - Create a new user\n" +
        "   2 - Change a user's password\n" + 
        "   3 - Delete a user\n" +
        "   0 - Log out", 0, 3);
      if (option == 1) {
        user = nextNonEmptyLine(in, "Enter a new user ID: ");
        response = server.loginTypeID(user);
        if (response != null) {
          System.out.println("User " + user + " already exists.");
        } else {
          name = nextNonEmptyLine(in, "Enter their real name: ");
          type = nextNonEmptyLine(in, "Enter their user type (administrator, customer, manager): ");
          response = server.createUser(user, name, type, "password");
          if (response != null) {
            System.out.println(response);
          }
        }
      } else if (option == 2) {
        user = nextNonEmptyLine(in, "Enter a user ID: ");
        response = server.loginTypeID(user);
        if (response == null) {
          System.out.println("User " + user + " doesn't exist.");
        } else {
          password = nextNonEmptyLine(in, "Enter their password: ");
          response = server.changePassword(user, password);
          if (response != null) {
            System.out.println(response);
          }
        }
      } else if (option == 3) {
        user = nextNonEmptyLine(in, "Enter a user ID: ");
        response = server.loginTypeID(user);
        if (response == null) {
          System.out.println("User " + user + " doesn't exist.");
        } else {
          if ("customer".equals(response)) {
            System.out.println(server.printCustomer(user));
            System.out.println("Warning! This will delete all the customer's reservations!");
          }
          response = nextNonEmptyLine(in, String.format("Are you certain you want to delete %s (%s) (yes or no)? ", user, server.realName(user)));
          if ("yes".equals(response)) {
            response = server.deleteUser(user);
            if (response != null) {
              System.out.println(response);
            }
          }
        }
      }
    }

    if ((response = server.logout()) != null) {
      System.out.println(response);
    }
    
  }

  /**
   * Helper method to handle menu items.
   * Print the prompt on each line of input.
   * @return a number between min and max
   */
  private static int menuOption(Scanner in, String menu, int min, int max) {
    int option = Integer.MIN_VALUE;
    String line;

    do {
      System.out.println(menu);
      line = nextNonEmptyLine(in, String.format("\nChoose an option between %d and %d: ", min, max));
      try {
        option = Integer.parseInt(line);
      } catch (NumberFormatException nfe) {
        System.out.println("Invalid input: " + line);
      }
    } while (option < min || option > max);
    
    return option;
  }

  /**
   * Helper method for Scanner to skip over empty lines.
   * Print the prompt on each line of input.
   */
  private static String nextNonEmptyLine(Scanner in, String prompt) {
    String line = null;

    System.out.print(prompt);
    while (line == null && in.hasNextLine()) {
      line = in.nextLine();
      if (line.trim().length() == 0) {
        line = null;
        System.out.print(prompt);
      }
    }

    return line;
  }
}
