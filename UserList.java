/**
   * UserList class
   *
   * COMP 2150 SECTION A01
   * INSTRUCTOR    Lauren Himbeaul
   * ASSIGNMENT    Assignment 2, question 1
   * @author       Ochiagha ifeanyi, 7900465
   * @version      26th oct 2023
   *
   * REMARKS: This keeps track of all the users that have been read in so they can be used for different operations depending on who is signed in.  
   */


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserList implements Iterable<User>{
    private ArrayList<User> list = new ArrayList<>();

    public void addObject(User temp) {
        if(contains(temp.getId(temp))){
            System.out.println("cannot add user");
        }
        else{
        list.add(temp);
        }
    }

    public void remove(User temp){
        if(list.contains(temp)){
            list.remove(temp);
        }

        else{
            System.out.println("user could not be found");
        }
    }

    public boolean contains(String currId){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getId(list.get(i)).equals(currId)){
                return true;
            }
        }

        return false;
    }

    public User getUser(String user){
        User curr = null;
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getId(list.get(i)).equalsIgnoreCase(user)){
                curr = list.get(i);
                break;
            }
        }
        return curr;
    }


    public User find(String userId) {
        for (User c: this.list) {
          if(c.getId(c).equalsIgnoreCase(userId)) { 
            return c;
          }
        }
        return null;
      }

      public int getSize(){
            return list.size();
      }

      public User currUser(int get){
        if(get > list.size()){
            return null;
        }
        return list.get(get);
      }
    @Override
    public Iterator<User> iterator() {
        
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

      
}
