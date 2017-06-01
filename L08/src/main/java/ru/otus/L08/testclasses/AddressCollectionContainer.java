package ru.otus.L08.testclasses;

import java.util.List;

/**
 * Created by DocDVZ on 01.06.2017.
 */
public class AddressCollectionContainer {

    private List<Address> addresses;
    private Address[] addressesArr;
    private String name;

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address[] getAddressesArr() {
        return addressesArr;
    }

    public void setAddressesArr(Address[] addressesArr) {
        this.addressesArr = addressesArr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressCollectionContainer that = (AddressCollectionContainer) o;

        if (addresses != null ? !addresses.equals(that.addresses) : that.addresses != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = addresses != null ? addresses.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
