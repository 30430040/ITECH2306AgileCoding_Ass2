package assignment2;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * Represents a business entity that books hotel rooms for its employees.
 * Contains business name, a contact person, and a list of business guests.
 */
public class Business implements Serializable {

    private String name;                          // Name of the business.
    private String contactPerson;                 // Contact person for the business.
    private ArrayList<BusinessGuest> employees;   // List of employees associated with the business.

    /**
     * Constructs a Business with the specified name and contact person.
     *
     * @param name the name of the business
     * @param contactPerson the contact person for the business
     */
    public Business(String name, String contactPerson) {
        this.name = name;
        this.contactPerson = contactPerson;
        this.employees = new ArrayList<>();
    }

    /**
     * Adds a business guest (employee) to the list of employees.
     *
     * @param guest the business guest to be added
     */
    public void addEmployee(BusinessGuest guest) {
        employees.add(guest);
    }

    /**
     * Returns the name of the business.
     *
     * @return the business name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the contact person of the business.
     *
     * @return the contact person
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Returns the list of business guests (employees).
     *
     * @return a list of employees
     */
    public ArrayList<BusinessGuest> getEmployees() {
        return employees;
    }
}
