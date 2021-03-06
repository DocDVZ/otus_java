package ru.otus.L08.testclasses;

/**
 * Created by dzvyagin on 01.06.2017.
 */
public class Address {

    private String countryISOCode;
    private String zipCode;
    private String city;
    private String street;
    private String house;
    private Integer someRandomInteger;
    private long someRandomLong;

    public String getCountryISOCode() {
        return countryISOCode;
    }

    public void setCountryISOCode(String countryISOCode) {
        this.countryISOCode = countryISOCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public Integer getSomeRandomInteger() {
        return someRandomInteger;
    }

    public void setSomeRandomInteger(Integer someRandomInteger) {
        this.someRandomInteger = someRandomInteger;
    }

    public long getSomeRandomLong() {
        return someRandomLong;
    }

    public void setSomeRandomLong(long someRandomLong) {
        this.someRandomLong = someRandomLong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (someRandomLong != address.someRandomLong) return false;
        if (countryISOCode != null ? !countryISOCode.equals(address.countryISOCode) : address.countryISOCode != null)
            return false;
        if (zipCode != null ? !zipCode.equals(address.zipCode) : address.zipCode != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (house != null ? !house.equals(address.house) : address.house != null) return false;
        return someRandomInteger != null ? someRandomInteger.equals(address.someRandomInteger) : address.someRandomInteger == null;
    }

    @Override
    public int hashCode() {
        int result = countryISOCode != null ? countryISOCode.hashCode() : 0;
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (house != null ? house.hashCode() : 0);
        result = 31 * result + (someRandomInteger != null ? someRandomInteger.hashCode() : 0);
        result = 31 * result + (int) (someRandomLong ^ (someRandomLong >>> 32));
        return result;
    }
}
