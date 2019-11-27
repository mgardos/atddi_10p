package ar.net.mgardos;

import java.util.List;

public interface CustomerSystem {
    void initialize();

    void start();

    void close();

    List<Customer> allCustomer();

    void add(Customer newCustomer);

    List<Address> allAddresses();

    List<Customer> customersWith(Criteria criteria);
}
