package ar.net.mgardos;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class PersistentCustomerSystem implements CustomerSystem {
    private Session session;

    private Session getSession() {
        return session;
    }

    private void setSession(Session session) {
        this.session = session;
    }

    public PersistentCustomerSystem() {
    }

    @Override
    public void initialize() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Address.class);
        configuration.configure();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        setSession(sessionFactory.openSession());
    }

    @Override
    public void start() {
        getSession().beginTransaction();
    }

    @Override
    public void close() {
        getSession().getTransaction().commit();
        getSession().close();
    }

    @Override
    public List<Customer> allCustomer() {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
        Root<Customer> root = cr.from(Customer.class);
        cr.select(root);

        Query<Customer> query = getSession().createQuery(cr);
        return query.getResultList();
    }

    @Override
    public void add(Customer newCustomer) {
        getSession().persist(newCustomer);
    }

    @Override
    public List<Address> allAddresses() {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Address> cr = cb.createQuery(Address.class);
        Root<Address> root = cr.from(Address.class);
        cr.select(root);

        Query<Address> query = getSession().createQuery(cr);
        return query.getResultList();
    }

    @Override
    public List<Customer> customersWith(Criteria criteria) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
        Root<Customer> root = cr.from(Customer.class);

        Predicate[] predicates = new Predicate[2];
        predicates[0] = cb.equal(root.get(criteria.getIdentificationType()), "D");
        predicates[1] = cb.equal(root.get(criteria.getIdentificationNumber()), "22333444");
        cr.select(root).where(predicates);

        Query<Customer> query = getSession().createQuery(cr);
        return query.getResultList();
    }
}