package ar.net.mgardos;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerImporterTest {
    private final CustomerImporter customerImporter = new CustomerImporter();
    private final PersistentCustomerSystem persistentCustomerSystem = new PersistentCustomerSystem();

    private Reader loadDataFromFile(String fileName) throws FileNotFoundException {
        return new FileReader(fileName);
    }

    private Reader loadDataFromStream() {
        StringBuilder streamBuilder = new StringBuilder();
        streamBuilder.append("C,Pepe,Sanchez,D,22333444\n");
        streamBuilder.append("A,San Martin,3322,Olivos,1636,BsAs\n");
        streamBuilder.append("A,Maipu,888,Florida,1122,Buenos Aires\n");
        streamBuilder.append("C,Juan,Perez,C,23-25666777-9\n");
        streamBuilder.append("A,Alem,1122,CABA,1001,CABA\n");

        return new StringReader(streamBuilder.toString());
    }

    @After
    public void tearDown() {
        persistentCustomerSystem.close();
    }

    @Before
    public void setUp() {
        persistentCustomerSystem.initialize();
        persistentCustomerSystem.start();
    }

    @Test
    public void test_readCustomersFromFileLoadIntoDatabase() throws Exception {
        customerImporter.importCustomers(loadDataFromFile("src/main/resources/input.txt"), persistentCustomerSystem);

        List<Customer> customers = persistentCustomerSystem.allCustomer();

        assertThat(customers).hasSize(2);
    }

    @Test
    public void test_readCustomersFromStreamLoadIntoDatabase() throws Exception {
        customerImporter.importCustomers(loadDataFromStream(), persistentCustomerSystem);

        List<Customer> customers = persistentCustomerSystem.allCustomer();

        assertThat(customers).hasSize(2);
    }

    @Test
    public void test_loadAddressesFromFileLoadIntoDatabase() throws Exception {
        customerImporter.importCustomers(loadDataFromFile("src/main/resources/input.txt"), persistentCustomerSystem);

        assertAllAddressesLoadedIntoDatabase();
    }

    @Test
    public void test_loadAddressesFromStreamLoadIntoDatabase() throws Exception {
        customerImporter.importCustomers(loadDataFromStream(), persistentCustomerSystem);

        assertAllAddressesLoadedIntoDatabase();
    }

    private void assertAllAddressesLoadedIntoDatabase() {
        List<Address> addresses = persistentCustomerSystem.allAddresses();

        assertThat(addresses).hasSize(3);
    }

    @Test
    public void test_CustomerWithProperAddresses() throws Exception {
        customerImporter.importCustomers(loadDataFromFile("src/main/resources/input.txt"), persistentCustomerSystem);

        List<Customer> customers = persistentCustomerSystem.customersWith(new Criteria("identificationType", "identificationNumber"));

        assertThat(customers).hasSize(1);
        assertThat(customers.get(0).getFirstName()).isEqualTo("Pepe");
        assertThat(customers.get(0).getLastName()).isEqualTo("Sanchez");
        assertThat(customers.get(0).getIdentificationType()).isEqualTo("D");
        assertThat(customers.get(0).getIdentificationNumber()).isEqualTo("22333444");
    }

}
