/*
 * this is a file from the A1 model assignment
 */

public class Customer extends User implements Comparable<Customer>{
   // private String name;

    private RewardCard rewards;

    public Customer(String name, String identification, String password) {
        super(name, identification, password);
       // super(name, identification, passWord,type);
        this.rewards = new RewardCard();
    }

    public void roomBooked(int duration) {
        rewards.pointUpdate(duration);
    }

    public String getName() {
        return this.name;
    }
    
    public int getPoints() {
        return this.rewards.getPoints();
    }

    public boolean equals(String name) {
        return this.name.equalsIgnoreCase(name);
    }

    public int compareTo(Customer obj) {
        return this.getName().compareTo((obj).getName());
    }

    public String toString() {
        return this.name + " " + this.rewards;
    }

    public void roomCancelled(int duration) {
        this.rewards.revokePoints(duration);
    }
    
}
