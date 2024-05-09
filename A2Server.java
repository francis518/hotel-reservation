/**
   * A2Server class
   *
   * COMP 2150 SECTION A01
   * INSTRUCTOR    Lauren Himbeaul
   * ASSIGNMENT    Assignment 1, question 1
   * @author       Ochiagha ifeanyi, 7900465
   * @version      5th oct 2023
   *
   * REMARKS: This runs the commands passed in by the user or read from a file and builds the hotel 
   */



import java.util.Scanner;
import java.io.*;


public class A2Server implements ReservationServer {

    private User loggedUser = null;
    private UserList allUsers = new UserList();
    private Hotel currHotel =  A1.readFloorPlan("a1plan.txt");
    private ReservationManager managing = new ReservationManager(currHotel);
    private Commands manageCommands = new Commands();


    public String readUserFile(String userFilename){
        String result = "problem in user file";
        File file = new File(userFilename);
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                String[] words = line.split(",");

                if(words.length == 4){
                    String ID = words[0];
                    String name = words[1];
                    String password = words[2];
                    String type = words[3];

                    if(type.equalsIgnoreCase("customer")){
                        Customer newCustomer = new Customer(name, ID, password);
                        allUsers.addObject(newCustomer);
                    }

                    else if(type.equalsIgnoreCase("manager")){
                        Manager manager = new Manager(name, ID, password);
                        allUsers.addObject(manager);
                    }

                    else{
                        Administrators neAdministrators = new Administrators(name, ID, password);
                        allUsers.addObject(neAdministrators);
                    }

                    result = null;
                }

                
            }



        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }

        return result;
    }

    public void exit(){
        String fileName = "a2users.txt";
        try {
            FileWriter writer = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for(int i = 0; i < allUsers.getSize(); i++){
                if(allUsers.currUser(i) != null){
                    bufferedWriter.write(lineToWrite(allUsers.currUser(i)));
                    bufferedWriter.newLine();
                }
            }

            bufferedWriter.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("error occured");
        }

        writeCommands();
        
    }

    public String login(String user, String password){
        String result = "not able to login";
        User curr = allUsers.getUser(user);
        if(curr != null){
            if(curr.getPassword(curr).equalsIgnoreCase(password)){
                result = null;
                loggedUser = curr;
            }
        }
        return result;
        
    }

    public void userPassword(String id){
        User temp = allUsers.getUser(id);
        if(temp != null){
            System.out.println(temp.getPassword(temp));
        }
        else{
            System.out.println("nope");
        }
    }


    public String logout(){
        String result = "log out failed";

        if(loggedUser != null){
            loggedUser = null;
            result = null;
        }

        return result;
    }

    public String loginType(){
        String result = null;
        if(loggedUser != null){
            if(loggedUser instanceof Customer){
                result = "customer";

            }

            if(loggedUser instanceof Manager){
                result = "manager";

            }

            if(loggedUser instanceof Administrators){
                result = "administrator";
            }

        }
        return result;
    }

    public String currentUserID(){
        String result = null;
        if(loggedUser != null){
            result = loggedUser.getId(loggedUser);
        }
        return result;
    }

    public String makeReservation(String user, int numGuests, int start, int duration){
        String result = "error occured";

        if(loggedUser != null){
            if(loggedUser instanceof Customer){
                if(loggedUser.getId(loggedUser).equalsIgnoreCase(user)){
                    managing.reserve(loggedUser.name, user, loggedUser.getPassword(loggedUser), numGuests, start, duration);
                    manageCommands.addCommand("RESERVE, " + user + ", " + numGuests + "," + start + ", " + duration );
                    result = null;
                }
            }

                if(loggedUser instanceof Manager){
                    User temp = allUsers.getUser(user);
                    if(temp != null){
                        managing.reserve(temp.name, user, temp.getPassword(temp), numGuests, start, duration);
                        manageCommands.addCommand("RESERVE, " + user + ", " + numGuests + "," + start + ", " + duration );
                        result = null;
                    }
                   
                }

                if(loggedUser instanceof Administrators){
                    result = "administrator cannot make reservations";
                }
            
        }
        return result;
    }

    public String cancelReservation(String user, int start){
        String result = "error has occured";

        if(loggedUser != null){
            if(loggedUser instanceof Customer){
                if(loggedUser.getId(loggedUser).equalsIgnoreCase(user)){
                    result = managing.cancel(user, start);
                    manageCommands.addCommand("CANCEL " + user + "," + start);
                }
            }

            if(loggedUser instanceof Manager){
                result = managing.cancel(user, start);
                 manageCommands.addCommand("CANCEL " + user + "," + start);
            }
            
        }

        return result;
        
    }

    public String printCustomer(String user){
        String result = null ;

        if(loggedUser != null){
            if(loggedUser instanceof Customer){
            if(loggedUser.getId(loggedUser).equalsIgnoreCase(user)){
                managing.printCustomer(user);
                result = "success";
            }
        }

            if(loggedUser instanceof Manager){
                managing.printCustomer(user);
                result = "success";
            }
        }


        return result;
    }

    public String printDay(int day){
        String result = "error could not print servations for " + day;

        if(loggedUser != null && loggedUser instanceof Manager){
            managing.printDay(day);
            result = null;
        }

        return result;
    }

    public String printRoom(int room){
        String result = "error could not print reservations for " + room;

        if(loggedUser != null && loggedUser instanceof Manager){
            managing.printRoom(room);
            result = null;
        }

        return result;
    }


    public String loginTypeID(String user){
        String result = null;

        if(loggedUser != null && loggedUser instanceof Manager || loggedUser instanceof Administrators){
            User curr = allUsers.find(user);
            if(curr != null){
                if(curr instanceof Customer){
                    result = "customer";
                }
                else if(curr instanceof Manager){
                    result = "manager";

                }

                else{
                    result = "adminstrator";
                }
            }
        }

        return result;
    }

    public String realName(String user){
        String result = null;
        if(loggedUser != null && loggedUser instanceof Manager || loggedUser instanceof Administrators){
            User currUser = allUsers.find(user);
            result = currUser.name;
        }

        return result; 
    }

    public String createUser(String user, String name, String type, String password){
        String result = "could not create new user";

        if(loggedUser != null && loggedUser instanceof Administrators){
            if(type.equalsIgnoreCase("customer")){
                Customer newCustomer = new Customer(name, user, password);
                allUsers.addObject(newCustomer);
                result = null;
            }

            else if(type.equalsIgnoreCase("manager")){
                Manager newManager = new Manager(name, user, password);
                allUsers.addObject(newManager);
                result = null;
            }

            else{
                Administrators newAdministrators = new Administrators(name, user, password);
                allUsers.addObject(newAdministrators);
                result = null;
            }
            
        }
        return result;
    }

    public String changePassword(String user, String password){
        String result = "error was unable to change password";

        if(loggedUser != null && loggedUser instanceof Administrators){
            User currUser = allUsers.find(user);
            if(currUser != null){
                currUser.changePassword(password);
                result = null;
            }

            else{
                result += "user could not be found";
            }
        }

        return result;
    }

    public String deleteUser(String user){
        String result = "could not delete user";

        if(loggedUser != null && loggedUser instanceof Administrators){
            User currUser = allUsers.find(user);
            if(currUser != null){
                allUsers.remove(currUser);
                result = null;
            }

            else{
                result += "user could not be found to be deleted";
            }
        }

        return result;
    }

    private String lineToWrite(User curr){
        String result = "";
        if(curr != null){
            result += curr.getId(curr) + "," + curr.name + "," + curr.getPassword(curr) + "," +  curr.getType(curr);
        }
        return result;
    }

    public void hotelSpec(){
        System.out.println(currHotel.numFloors());
    }

    private void writeCommands(){
        String fileName = "newFile.txt";

        try {
          
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for(int i = 0; i < manageCommands.getSize(); i++){
                if(manageCommands.getCommads( i) != null){
                    bufferedWriter.write(manageCommands.getCommads(i));
                    System.out.println(manageCommands.getCommads(i));
                    bufferedWriter.newLine();
                }
            }
          
            bufferedWriter.close();

            System.out.println("New file created and written successfully.");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
