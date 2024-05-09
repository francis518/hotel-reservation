/**
   * Administrator class
   *
   * COMP 2150 SECTION A01
   * INSTRUCTOR    Lauren Himbeaul
   * ASSIGNMENT    Assignment 2, question 1
   * @author       Ochiagha ifeanyi, 7900465
   * @version      5th oct 2023
   *
   * REMARKS: This is a type of user called administrator that can change passwords of other users and create more users
   */


public class Administrators extends User {
    public Administrators(String name, String identification, String passWord) {
        super(name, identification, passWord);
        
    }

    public String toString(){
        return "i am the big man on the user block";
    }
}
