package org.joescaos.model;

public class Customer {
    private int id;
    private String dni;
    private String first_name;
    private String last_name;
    private String phone;
    private String email;
    private String address;

    public int getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return first_name + " " + last_name + " (" + dni + ")";
    }
}
