/**
 * COMP 2150 Summer 23 Assignment 2: Reserv-a-tron server interface
 */

public interface ReservationServer {
  // This can only be called, once, on creation
  // Returns null on success, or an error message if something went wrong
  String readUserFile(String userFilename);
  
  // This can only be called once, on quit
  // All of the data files (user file, reservations) must be updated!
  void exit();

  // This is called to allow a user to log in
  // Returns null on success, or an error message if something went wrong
  String login(String user, String password);
  
  /*** The calls below only succeed if the user is logged in ***/
  
  // This is called to allow the currently-logged in user to log out
  // Returns null on success, or an error message if something went wrong
  String logout();
  
  // Determine the type of user ("customer" or "manager" or "administrator")
  // Returns null on failure or one of the three strings above
  String loginType();
  
  // Get the currently-logged in user ID
  // Returns null on failure or the currently logged in user ID
  String currentUserID();

  // Make a reservation for the given user, with the given options
  // Returns null on success, or an error message if something went wrong
  String makeReservation(String user, int numGuests, int start, int duration);

  // Cancel reservations for the given user, starting from the given day
  // Returns null on success, or an error message if something went wrong
  String cancelReservation(String user, int start);
  
  // Print the name and reservations for the given user ID
  // Returns null on failure or a single string to print
  String printCustomer(String user);
  
  /*** The next two calls can only be made if a "manager" is logged in ***/
  
  // Print reservations on a day
  // Returns null on failure or a single string to print
  String printDay(int day);
  
  // Print reservations for a room
  // Returns null on failure or a single string to print
  String printRoom(int room);
  
  /*** The following calls can only be made if a "manager" or "administrator" is logged in ***/
  
  // Check the type of a user ID
  // Returns null on failure or one of the three strings above
  String loginTypeID(String user);
  
  // Get the user's real name
  // Returns null on failure or the real name of the user
  String realName(String user);
  
  /*** The next three calls can only be made if an "administrator" is logged in ***/
  
  // Create a new user with the given user ID, name, type, and password
  // Returns null on success, or an error message if something went wrong
  String createUser(String user, String name, String type, String password);
  
  // Change a user's password
  // Returns null on success, or an error message if something went wrong
  String changePassword(String user, String password);
  
  // Delete a user
  // Returns null on success, or an error message if something went wrong
  String deleteUser(String user);  
}