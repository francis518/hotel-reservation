import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

/**
 * COMP 2150 Summer 23 Assignment 1: Hotel reservations
 */

/**
 * ReservationManager class: A class that puts together a hotel and its
 * reservations, including the algorithm for making reservations.
 */

public class ReservationManager {
  private Hotel hotel;
  private CustomerList customers;
  private ReservationsList reservations;
  
  public ReservationManager(Hotel hotel) {
    this.hotel = hotel;
    this.customers = new CustomerList();
    this.reservations = new ReservationsList();
  }
  
  /**
   * Attempt to make a reservation based on the criteria described in the assignment.
   *
   * @return a string containing an error message or null on success
   */
  public String reserve(String customerName, String id, String password, int numGuests, int start, int duration) {
    Customer customer = customers.add(customerName, id, password);
    Reservation res = new Reservation(customer, numGuests, start, duration);

    res = bestReservation(res);
    if (res == null) {
      return "No room for: " + customer + " (" + numGuests + " guests, from day " + start + " for " + duration + " days)";
    } else {
      Reservation match = reservations.findMatchingReservation(res);
      if (match == null) {
        reservations.add(res);
      } else {
        match.merge(res);
      }
      return null;
    }
  }
  
  /**
   * Attempt to cancel a reservation as described in the assignment.
   *
   * @return a string containing an error message or null on success
   */
  public String cancel(String customerId, int start) {
    Customer customer = this.customers.find(customerId);
    ReservationsList onDay = reservations.allConflicts(new Reservation(null, 0, start, 1));
    
    for (Reservation r : onDay) {
      if(customer.equals(r.getCustomer())) {
        if (r.cancel(start)) {
          reservations.remove(r);
        }
        return null;
      }
    }
    
    return "No matching reservation to remove: " + customerId + "," + start;
  }

  public void printDay(int day) {
    ArrayList<Integer> roomNumbers = new ArrayList<>(hotel.getRoomMap().keySet());
    Collections.sort(roomNumbers);
    ReservationsList onDay = reservations.allConflicts(new Reservation(null, 0, day, 1));
    
    System.out.println("\nRooms on day " + day + ":");
    for (int num : roomNumbers) {
      ReservationsList inRoom = onDay.reservationsForRoom(num);
      System.out.print(" " + num + ": ");
      assert inRoom.size() <= 1;
      if (inRoom.size() == 0) {
        System.out.println("vacant");
      } else {
        System.out.println(inRoom.iterator().next().getCustomer());
      }
    }
  }
  
  public void printRoom(int room) {
    Room rm = hotel.getRoom(room);
    
    if (rm == null) {
      System.out.println("\nNo room " + room + ".");

    } else {
      ReservationsList list = reservations.reservationsForRoom(room);

      if (!list.iterator().hasNext()) {
        System.out.println("\nNo reservations for room " + room + ".");
      } else {

        System.out.println("\nReservations for room " + room + ":");
        for (Reservation r: list) {
          System.out.println(" " + r.toRoomString());
        }
      }
    } 
  }
  
  public void printCustomer(String name) {
    Customer customer = this.customers.find(name);
    if (customer == null) {
      System.out.println("\nNo customer " + name + ".");

    } else {
      ReservationsList list = reservations.reservationsForCustomer(name);

      if (!list.iterator().hasNext()) {
        System.out.println("\nNo reservations for " + name + ".");

      } else {
        System.out.println("\nReservations for " + name + ":");
        for (Reservation r: list) {
          System.out.println(" " + r.toCustomerString());
        }
        System.out.println("Reward Points Balance: " + customer.getPoints());
      }
    }
  }
  
  public void printCustomers() {
    System.out.println("\nCustomers:");
    for (Customer c: customers) {
      int reservationCount = reservations.countForCustomer(c);
      String reservedWord = " reservation";

      if (reservationCount == 0) {
        reservedWord = "no" + reservedWord;
      } else {
        reservedWord = reservationCount + reservedWord;
      }
      if (reservationCount != 1) {
        reservedWord += "s";
      }

      System.out.println(" " + c + " (" + reservedWord + ")");
    }
  }

  /**
   * Return the best reservation.
   *
   * Criteria (ordered):
   *  1. Select only rooms that are available
   *  2. Prefer rooms on the same floor
   *  3. Find the lowest-cost option
   *
   * @param res contains a reservation with no room numbers populated
   * @return a new reservation, fully populated, or null if no available reservation can be found.
   */
  private Reservation bestReservation(Reservation res) {
    ReservationsList conflicts = reservations.allConflicts(res);
    HashMap<Integer, Room> rooms = hotel.getRoomMap();
    int guestsLeft, currentGuestsLeft;
    
    // Make result fill up with guests until it reaches res.getNumGuests()
    Reservation result = new Reservation(res, 0), current, best = null;
    
    // Remove unavailable rooms
    for (Reservation c : conflicts) {
      for (Room room : c) {
        rooms.remove(room.getNumber());
      }
    }
    
    if (rooms.size() == 0) {
      return null;
    }
    
    // Fill up floors first, if necessary
    current = null;
    guestsLeft = res.getNumGuests();
    while (current == null) {
      current = fillEntireFloor(result, guestsLeft, rooms);

      if (current == null) {
        // No room left
        return null;
      } else if (current.getNumGuests() == 0) {
        // No more floors to fill, end the loop
        current = result;
      } else {
        // Now with a floor filled
        result = current;
      }
    }
    
    if (result.getNumGuests() == res.getNumGuests()) {
      // Finished early!
      return result;
    }

    // The remainder of the reservation will be on a single floor

    // Determine if there are still any guests that need a room
    if (res.getNumGuests() > result.getNumGuests()) {

      // Find the least-cost subset of rooms on each floor with enough space for remaining guests    
      for (int floor = 1; floor <= hotel.numFloors(); floor++) {
        current = new Reservation(result, 0);
        guestsLeft = res.getNumGuests() - result.getNumGuests();
      
        // Assemble a list of available rooms on this floor
        int floorCapacity = 0;
        ArrayList<Room> floorRooms = new ArrayList<>();
        for (int room = 1; room <= hotel.roomsOnFloor(floor); room++) {
          Room roomRef = rooms.get(hotel.roomNumber(floor, room));
          if (roomRef != null) {
            floorRooms.add(roomRef);
            floorCapacity += roomRef.getCapacity();
          }
        }
      
        if (floorCapacity >= guestsLeft) {
          current = bestRoomsOnFloor(new Reservation(null, 0, 0, 0), res.getNumGuests() - result.getNumGuests(), floorRooms);
        
          if (best == null || current != null && (current.totalCost() < best.totalCost())) {
            best = current;
          }
        }
      }
    }

    if (best != null) {
      result = new Reservation(result, res.getNumGuests());
      for (Room r : best) {
        result.roomBooked();
        result.add(r);
      }
    } else {
      result = null;
    }

    return result;
  }

  /**
   * Fill up floors from a reservation, by finding the floor that
   * fits as many of numGuests as possible.
   *
   * If a reservation is made, it will remove the rooms from the rooms map.
   *
   * @return an updated reservation that includes an entire floor; or
   *         a reservation with zero guests if the entire reservation fits on at least one floor (no reservation made)
   *         null if no fit was found (not enough space)
   */
  Reservation fillEntireFloor(Reservation res, int numGuests, HashMap<Integer, Room> rooms) {
    int bestFloor = 0, bestFloorCap = 0, bestFloorRate = 0;

    for (int floor = 1; floor <= hotel.numFloors(); floor++) {
      int floorCap = 0, floorRate = 0;

      for (int room = 1; room <= hotel.roomsOnFloor(floor); room++) {
        Room roomRef = rooms.get(hotel.roomNumber(floor, room));
        if (roomRef != null) {
          floorCap += roomRef.getCapacity();
          floorRate += roomRef.getRate();
        }
      }

      if (floorCap > numGuests) {
        // Entire reservation will fit on a floor
        return new Reservation(res, 0);
      }
      
      // Prefer more guests to best price
      if (floorCap != 0 && (bestFloor == 0 || bestFloorCap < floorCap || (bestFloorCap == floorCap && bestFloorRate > floorRate))) {
        // Better choice
        bestFloor = floor;
        bestFloorCap = floorCap;
      }
    }
    
    if (bestFloor == 0) {
      // No room at the inn
      return null;
    }

    // If we made it this far we can reserve the entire best floor
    res = new Reservation(res, bestFloorCap + res.getNumGuests());
    for (int room = 1; room <= hotel.roomsOnFloor(bestFloor); room++) {
      Room roomRef = rooms.get(hotel.roomNumber(bestFloor, room));
      if (roomRef != null) {
        rooms.remove(roomRef.getNumber());
        res.add(roomRef);
      }
    }
    
    return res;
  }
  
  /**
   * Find the best subset of the rooms on the given floor to fit numGuests.
   *
   * @return an updated reservation with the rooms
   */
  Reservation bestRoomsOnFloor(Reservation res, int numGuests, ArrayList<Room> rooms) {
    Collections.sort(rooms);

    while (numGuests > 0) {
      // Look for the cheapest fit, or the cheapest cost per guest
      boolean fit = false;
      int pos = 0, bestPos = -1;
      double bestCostPerGuest = Double.MAX_VALUE;

      while (!fit && pos < rooms.size()) {
        double costPerGuest = 1.0 * rooms.get(pos).getRate() / rooms.get(pos).getCapacity();
        if (rooms.get(pos).getCapacity() >= numGuests && costPerGuest < bestCostPerGuest) {
          // Found a cheap fit
          fit = true;
        } else {
          if (costPerGuest < bestCostPerGuest) {
            // Found a better value
            bestPos = pos;
            bestCostPerGuest = costPerGuest;
          }
          pos++;  
        }
      }

      if (!fit) {
        // Choose the best-value room
        pos = bestPos;
        fit = true;
      }

      res.add(rooms.get(pos));
      numGuests -= rooms.get(pos).getCapacity();
      rooms.remove(pos);
    }
  
    return res;
  }
}