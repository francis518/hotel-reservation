import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * COMP 2150 Summer 23 Assignment 1: Hotel reservations
 */

/**
 * Store a list of customers.
 * Customers don't need an entire class, since they're only a name.
 * We can reconstruct other info from other data structures
 * (this may not be good enough, at some point).
 * 
 * @author john
 */

public class CustomerList implements Iterable<Customer> {
  private ArrayList<Customer> customers;
  
  public CustomerList() {
    customers = new ArrayList<Customer>();
  }
  
  public boolean contains(String name) {
    for (Customer c: this.customers) {
      if(c.getName().equalsIgnoreCase(name)) {
        return true;
      }
    }
    return false;
  }
  
  public Customer add(String name, String id, String password) {
    Customer cust = find(name);
    if (cust == null) {
      cust = new Customer(name, id, password);
      customers.add(cust);
      Collections.sort(customers);
    }
    return cust;
  }

  public Customer find(String name) {
    for (Customer c: this.customers) {
      if(c.getId(c).equalsIgnoreCase(name)) { return c; }
    }
    return null;
  }

  public Iterator<Customer> iterator() {
    return customers.iterator();
  }
  
  public String toString() {
    return customers.toString();
  }
}