package yamenko.abcgrocery.entities;

/**
 * Supplier entity
 *
 * @author Eugene Yamenko <yamenko@me.com>
 */
public class Supplier extends Entity {
    // Fields
    private String name;
    private String address;
    private String contactPerson;
    private String contactPhone;

    // Constructors
    public Supplier() {
        this(null, "", "", "", "");
    }

    public Supplier(Integer id, String name, String address, String contactPerson,
            String contactPhone) {
        super(id);
        this.name = name;
        this.address = address;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
    }

    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    // Override methods
    @Override
    public String toString() {
        return getId() + ": " + name;
    }

    @Override
    public boolean isValid() {
        // validate name
        if (getName().isEmpty()) {
            return false;
        }

        // validate address
        if (getAddress().isEmpty()) {
            return false;
        }

        // validate contact person
        if (getContactPerson().isEmpty()) {
            return false;
        }

        // validate contact phone
        if (getContactPhone().isEmpty()) {
            return false;
        }

        return true;
    }
}
