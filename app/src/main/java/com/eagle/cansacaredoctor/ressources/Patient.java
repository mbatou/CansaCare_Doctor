package com.eagle.cansacaredoctor.ressources;

public class Patient {

    String email;
    String firstname;
    String lastname;
    String password;
    String typeofcancer;

    public String getTypeofcancer() {
        return typeofcancer;
    }

    public void setTypeofcancer(String typeofcancer) {
        this.typeofcancer = typeofcancer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
