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
import java.io.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerImporterTest {
    private final CustomerImporter customerImporter = new CustomerImporter();
    private Session session;

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
    public void test_readCustomersFromFileLoadIntoDatabase() throws Exception {
        customerImporter.importCustomers(session, loadDataFromFile("src/main/resources/input.txt"));

        List<Customer> customers = loadCustomerIntoDatabase();

        assertThat(customers).hasSize(2);
    }

    @Test
    public void test_readCustomersFromStreamLoadIntoDatabase() throws Exception {
        customerImporter.importCustomers(session, loadDataFromStream());

        List<Customer> customers = loadCustomerIntoDatabase();

        assertThat(customers).hasSize(2);
    }

    private List<Customer> loadCustomerIntoDatabase() {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
        Root<Customer> root = cr.from(Customer.class);
        cr.select(root);

        Query<Customer> query = session.createQuery(cr);
        return query.getResultList();
    }

    @Test
    public void test_loadAddressesFromFileLoadIntoDatabase() throws Exception {
        customerImporter.importCustomers(session, loadDataFromFile("src/main/resources/input.txt"));

        assertAllAddressesLoadedIntoDatabase();
    }

    @Test
    public void test_loadAddressesFromStreamLoadIntoDatabase() throws Exception {
        customerImporter.importCustomers(session, loadDataFromStream());

        assertAllAddressesLoadedIntoDatabase();
    }

    private void assertAllAddressesLoadedIntoDatabase() {
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
        customerImporter.importCustomers(session, loadDataFromFile("src/main/resources/input.txt"));

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