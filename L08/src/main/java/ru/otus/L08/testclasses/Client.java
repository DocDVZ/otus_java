package ru.otus.L08.testclasses;

import java.util.Date;
import java.util.Set;

/**
 * Created by dzvyagin on 01.06.2017.
 */
public class Client {

    private String firstName;
    private String secondName;
    private Date birthDate;
    private Set<String> phones;
    private Address homeAddress;
    private Address registerAddress;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<String> getPhones() {
        return phones;
    }

    public void setPhones(Set<String> phones) {
        this.phones = phones;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(Address registerAddress) {
        this.registerAddress = registerAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (firstName != null ? !firstName.equals(client.firstName) : client.firstName != null) return false;
        if (secondName != null ? !secondName.equals(client.secondName) : client.secondName != null) return false;
        if (birthDate != null ? !birthDate.equals(client.birthDate) : client.birthDate != null) return false;
        if (phones != null ? !phones.equals(client.phones) : client.phones != null) return false;
        if (homeAddress != null ? !homeAddress.equals(client.homeAddress) : client.homeAddress != null) return false;
        return registerAddress != null ? registerAddress.equals(client.registerAddress) : client.registerAddress == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (phones != null ? phones.hashCode() : 0);
        result = 31 * result + (homeAddress != null ? homeAddress.hashCode() : 0);
        result = 31 * result + (registerAddress != null ? registerAddress.hashCode() : 0);
        return result;
    }
}
