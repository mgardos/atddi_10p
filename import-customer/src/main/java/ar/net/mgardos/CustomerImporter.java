package ar.net.mgardos;

import org.hibernate.Session;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public class CustomerImporter {
    private Customer newCustomer;
    private String record;
    private LineNumberReader lineReader;

    public CustomerImporter() {
    }

    public void importCustomers(Session session, Reader reader) throws IOException {
        lineReader = new LineNumberReader(reader);
        nextRecord();

        while (hasRecord()) {
            createCustomer(session);
            nextRecord();
        }

        reader.close();
    }

    private void nextRecord() throws IOException {
        record = lineReader.readLine();
    }

    private boolean hasRecord() {
        return record != null;
    }

    private void createCustomer(Session session) {
        if (record.startsWith("C")) {
            String[] customerData = parseRecord();
            newCustomer = new Customer();
            newCustomer.setFirstName(customerData[1]);
            newCustomer.setLastName(customerData[2]);
            newCustomer.setIdentificationType(customerData[3]);
            newCustomer.setIdentificationNumber(customerData[4]);
            session.persist(newCustomer);
        } else if (record.startsWith("A")) {
            String[] addressData = parseRecord();
            Address newAddress = new Address();

            newCustomer.addAddress(newAddress);
            newAddress.setStreetName(addressData[1]);
            newAddress.setStreetNumber(Integer.parseInt(addressData[2]));
            newAddress.setTown(addressData[3]);
            newAddress.setZipCode(Integer.parseInt(addressData[4]));
            newAddress.setProvince(addressData[5]);
        }
    }

    private String[] parseRecord() {
        return record.split(",");
    }
}