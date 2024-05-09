/**
   * Commands class
   *
   * COMP 2150 SECTION A01
   * INSTRUCTOR    Lauren Himbeaul
   * ASSIGNMENT    Assignment 2, question 1
   * @author       Ochiagha ifeanyi, 7900465
   * @version      26th oct 2023
   *
   * REMARKS: this keeps track of a list of all the commands done by the user and stores it for saving changes made
   */


import java.util.ArrayList;

public class Commands {
    private ArrayList<String> commands = new ArrayList<>();

    public void addCommand(String command){
        commands.add(command);
    }

    public int getSize(){
        return commands.size();
    }

    public String getCommads(int get){
        return commands.get(get);
    }
}
