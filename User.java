/**
   * User class
   *
   * COMP 2150 SECTION A01
   * INSTRUCTOR    Lauren Himbeaul
   * ASSIGNMENT    Assignment 2, question 1
   * @author       Ochiagha ifeanyi, 7900465
   * @version      26th oct 2023
   *
   * REMARKS: this is the base class of all the different types of users to be read from a file.
   */


public class User {
    private String ID;
    protected String name;
    private String password;
   // private String userType;

    public User(String name, String identification, String passWord){
        ID = identification;
        this.name = name;
        password = passWord;
        //userType = type;

    }

    public String getId(User currUser){
        return currUser.ID;
    }

    public String getPassword(User curr){
        return curr.password;
    }
    
    public void changePassword(String password){
      
        this.password = password;
    }

    public String getType(User curr){
        String result = null;
        if(curr != null){
            if(curr instanceof Customer){
                result = "Customer";
            }
            else if(curr instanceof Manager){
                result = "Manager";

            }

            else{
                result = "Adminstrator";
            }
        }
        

        return result;
    }
}
