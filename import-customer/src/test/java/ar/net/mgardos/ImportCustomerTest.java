package ar.net.mgardos;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ImportCustomerTest {
    private Session session;

    public void importCustomers() throws IOException {
        FileReader reader = new FileReader("src/main/resources/input.txt");
        LineNumberReader lineReader = new LineNumberReader(reader);

        Customer newCustomer = null;
        String line = lineReader.readLine();
        while (line!=null) {
            if (line.startsWith("C")){
                String[] customerData = line.split(",");
                newCustomer = new Customer();
                newCustomer.setFirstName(customerData[1]);
                newCustomer.setLastName(customerData[2]);
                newCustomer.setIdentificationType(customerData[3]);
                newCustomer.setIdentificationNumber(customerData[4]);
                session.persist(newCustomer);
            }
            else if (line.startsWith("A")) {
                String[] addressData = line.split(",");
                Address newAddress = new Address();

                newCustomer.addAddress(newAddress);
                newAddress.setStreetName(addressData[1]);
                newAddress.setStreetNumber(Integer.parseInt(addressData[2]));
                newAddress.setTown(addressData[3]);
                newAddress.setZipCode(Integer.parseInt(addressData[4]));
                newAddress.setProvince(addressData[5]);
            }

            line = lineReader.readLine();
        }

        reader.close();
    }

    @After
    public void tearDown() {
        session.getTransaction().commit();
        session.close();
    }

    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.configure();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @Test
    public void test_CustomersLoadedInDatabase() throws Exception {
        importCustomers();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
        Root<Customer> root = cr.from(Customer.class);
        cr.select(root);

        Query<Customer> query = session.createQuery(cr);
        List<Customer> customers = query.getResultList();

        assertThat(customers).hasSize(2);
    }

    @Test
    public void test_AddressesLoadedInDatabase() throws Exception {
        importCustomers();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Address> cr = cb.createQuery(Address.class);
        Root<Address> root = cr.from(Address.class);
        cr.select(root);

        Query<Address> query = session.createQuery(cr);
        List<Address> addresses = query.getResultList();

        assertThat(addresses).hasSize(3);
    }

    @Test
    public void test_CustomerWithProperAddresses() throws Exception {
        importCustomers();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
        Root<Customer> root = cr.from(Customer.class);

        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.equal(root.get("identificationType"), "D");
        predicates[1] = cb.equal(root.get("identificationNumber"), "22333444");
        cr.select(root).where(predicates);

        Query<Customer> query = session.createQuery(cr);
        List<Customer> customers = query.getResultList();

        assertThat(customers).hasSize(1);
        assertThat(customers.get(0).getFirstName()).isEqualTo("Pepe");
        assertThat(customers.get(0).getLastName()).isEqualTo("Sanchez");
        assertThat(customers.get(0).getIdentificationType()).isEqualTo("D");
        assertThat(customers.get(0).getIdentificationNumber()).isEqualTo("22333444");
    }
}
