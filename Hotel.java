import java.util.HashMap;

/**
 * COMP 2150 Summer 23 Assignment 1: Hotel reservations
 */

/**
 * A Hotel is represented as a map of room numbers to rooms.
 *
 * Note that the underlying structure (implementation) does not have
 * floors separated, but an interface that differentiates between
 * floors makes the reservation algorithm simpler.
 *
 * There are no "null" values permitted in the data structure.
 * 
 * @author johnxx
 */

public class Hotel {
  private HashMap<Integer, Room> rooms; // by room number
  private int[] roomNumbersPerFloor; // metadata
  
  public Hotel(String name, Room[][] roomArray) {
    rooms = new HashMap<>();
    
    roomNumbersPerFloor = new int[roomArray.length];
    for (int fNum = 0; fNum < roomArray.length; fNum++) {
      Room[] floor = roomArray[fNum];
      if (floor != null) {
        assert floor.length < 100;
        roomNumbersPerFloor[fNum] = floor.length;
        for (int rNum = 0; rNum < floor.length; rNum++) {
          Room room = floor[rNum];
          if (room != null) {
            rooms.put(room.getNumber(), room);
          }
        }
      }
    }
  }
  
  public int numFloors() {
    return roomNumbersPerFloor.length;
  }
  
  // Floor numbers start at 1
  public int roomsOnFloor(int floor) {
    return roomNumbersPerFloor[floor - 1];
  }
  
  // Room numbers start at 1
  public Room getRoom(int floor, int room) {
    if (floor < 1 || floor > numFloors())
      return null;
    if (room < 1 || room > roomsOnFloor(floor))
      return null;
    return rooms.get(roomNumber(floor, room));
  }
  
  public int getFloor(int room) {
    return room / 100;
  }
    
  public int getRoomNumber(int room) {
    return room % 100;
  }
  
  public Room getRoom(int room) {
    return getRoom(getFloor(room), getRoomNumber(room));
  }

  public int roomNumber(int floor, int room) {
    return (floor * 100 + room);
  }
  
  // Coincidentally, this is a good data structure for making reservations.
  // This is a coincidence. Really.
  public HashMap<Integer, Room> getRoomMap() {
    // Critical to make a copy as it is mutable!
    return new HashMap<Integer, Room>(rooms);
  }
  
  public String toString() {
    return rooms.toString();
  }
}