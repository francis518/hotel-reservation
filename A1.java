import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * COMP 2150 Summer 23 Assignment 1: Hotel reservations
 */

/**
 * Main program: read in a floor plan and reservation requests.
 * 
 * @author john
 */
public class A1 {
  public static void main(String[] args) {
    Hotel hotel;
    
    System.out.println("Reading the floor plan...");
    
    hotel = readFloorPlan("data2/a1plan.txt");

    System.out.println("\nProcessing requests...");
    
   // processRequests("data2/a1reserve.txt", hotel);

    System.out.println("\nEnd of processing.");
  }

  /**
   * Read a data file that describes the floor plan of a hotel. See
   * assignment for details on file format.
   */
  public static Hotel readFloorPlan(String filename) {
    Scanner in;
    String line;
    String[] tokens;

    int num, floor, room;
    String name = null;
    int price;

    // In order to make the floor plan immutable(!) we're going to build
    // an intermediate data representation that will be passed to the
    // Hotel constructor.
    Room[][] rooms = null;
    
    try {
      in = new Scanner(new FileReader(filename));
      if ((name = nextNonEmptyLine(in)) == null) {
        System.out.println("Missing hotel name.");
      } else {
        if ((line = nextNonEmptyLine(in)) == null) {
          System.out.println("Missing number of floors.");
        } else {
          try {
            num = Integer.parseInt(line);
            rooms = new Room[num][];
          } catch (NumberFormatException nfe) {
            System.out.println("Invalid number of floors: " + line);
          }
        }
      }
      
      if (rooms != null) {
        for (int i = 0; i < rooms.length; i++) {
          floor = i+1;
          num = 0;
          if ((line = nextNonEmptyLine(in)) == null) {
            System.out.println("Missing number of rooms on floor " + floor + ".");
          } else {
            try {
              num = Integer.parseInt(line);
              rooms[i] = new Room[num];
            } catch (NumberFormatException nfe) {
              System.out.println("Invalid number of rooms on floor " + floor + ": " + line);
            }
          }
          
          if (rooms[i] != null) {
            for (int j = 0; j < rooms[i].length; j++) {
              room = floor * 100 + (j+1);
              if ((line = nextNonEmptyLine(in)) == null) {
                System.out.println("Missing description for room " + room + ".");
              } else {
                tokens = line.split(",");
                if (tokens.length >= 3) {
                  if (tokens.length > 3) {
                    System.out.println("Ignoring extra tokens on room description for " + room + ":" + line);
                  }
                  try {
                    rooms[i][j] = new Room(room, tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                  } catch (NumberFormatException nfe) {
                    // error handled below because rooms[i][j] == null
                  }
                }
                if (rooms[i][j] == null) {
                  System.out.println("Invalid room description for " + room + ": " + line);
                }
              }
            }
          }
        }
      }
    } catch (FileNotFoundException fnf) {
      System.out.println(fnf.getMessage());
    }
    
    if (rooms == null) {
      return null;
    } else {
      return new Hotel(name, rooms);
    }
  }

  /**
   * Process a sequence of reservations/cancellations from an input file.
   * See the assignment for description of the file format.
   */
  // private static void processRequests(String filename, Hotel hotel) {
  //   ReservationManager rm = new ReservationManager(hotel);

  //   Scanner in;
  //   String line, result;
  //   String[] tokens;
  //   int num1, num2, num3;
    
  //   boolean valid;
    
  //   try {
  //     in = new Scanner(new FileReader(filename));
  //     while ((line = nextNonEmptyLine(in)) != null) {
  //       tokens = line.split(",");
  //       valid = false;

  //       if (tokens.length < 1) {
  //         valid = false;
  //       } else if (tokens[0].equals("RESERVE")) {
  //         try {
  //           if (tokens.length >= 5) {
  //             num1 = Integer.parseInt(tokens[2]);
  //             num2 = Integer.parseInt(tokens[3]);
  //             num3 = Integer.parseInt(tokens[4]);
  //             if (num1 <= 0 || num2 <= 0 || num3 <= 0) {
  //               valid = false;
  //             } else {
  //               result = rm.reserve(tokens[1], num1, num2, num3);
  //               if (result != null) {
  //                 System.out.println(result);
  //               }
  //               valid = true;
  //             }
  //           }
  //         } catch (NumberFormatException nfe) { }
  //         if (!valid) {
  //           System.out.println("Invalid reservation: " + line);
  //         }

  //       } else if (tokens[0].equals("CANCEL")) {
  //         try {
  //           if (tokens.length >= 3) {
  //             num1 = Integer.parseInt(tokens[2]);
  //             if (num1 <= 0) {
  //               valid = false;
  //             } else {
  //               result = rm.cancel(tokens[1], num1);
  //               if (result != null) {
  //                 System.out.println(result);
  //               }
  //               valid = true;
  //             }
  //           }
  //         } catch (NumberFormatException nfe) { }
  //         if (!valid) {
  //           System.out.println("Invalid cancellation: " + line);
  //         }

  //       } else if (tokens[0].equals("PRINT")) {
  //         try {
  //           if (tokens.length > 1) {
  //             if (tokens[1].equals("DAY")) {
  //               if (tokens.length >= 3) {
  //                 num1 = Integer.parseInt(tokens[2]);
  //                 if (num1 <= 0) {
  //                   valid = false;
  //                 } else {
  //                   rm.printDay(num1);
  //                   valid = true;
  //                 }
  //               }
  //             } else if (tokens[1].equals("ROOM")) {
  //               if (tokens.length >= 3) {
  //                 num1 = Integer.parseInt(tokens[2]);
  //                 if (num1 <= 0) {
  //                   valid = false;
  //                 } else {
  //                   rm.printRoom(num1);
  //                   valid = true;
  //                 }
  //               }
  //             } else if (tokens[1].equals("CUSTOMER")) {
  //               if (tokens.length >= 3) {
  //                 rm.printCustomer(tokens[2]);
  //                 valid = true;
  //               }
  //             } else if (tokens[1].equals("CUSTOMERS")) {
  //               rm.printCustomers();
  //               valid = true;
  //             }
  //           }
  //         } catch (NumberFormatException nfe) { }
  //         if (!valid) {
  //           System.out.println("Invalid print: " + line);
  //         }

  //       } else {
  //         System.out.println("Unknown command on line: " + line);
  //       }
  //     }
  //   } catch (FileNotFoundException fnf) {
  //     System.out.println(fnf.getMessage());
  //   }
  // }
  
  /**
   * Helper method for Scanner to skip over empty lines.
   */
  private static String nextNonEmptyLine(Scanner in) {
    String line = null;

    while (in.hasNextLine() && line == null) {
      line = in.nextLine();
      if (line.trim().length() == 0) {
        line = null;
      }
    }

    return line;
  }
}
