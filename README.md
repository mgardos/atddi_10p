# atddi_10p

Referencias
https://www.baeldung.com/hibernate-criteria-queries

URL: http://tinyurl.com/TDDAvanzadoNov2019
password: SuperTDDJP

Clave solucion final: import1

Import Customer
---------------
1) Verificar importacion funciona correctamente
  a) Clientes cargados en la base
  b) Direcciones cargados en la base
  c) Cada cliente con sus direcciones
2) sacar session como variable de instancia del test: Refactor -> Extract -> Field...
3) Before inicializacion de session y after para cerrar
4) solucionar dos errores
5) sacar afuera lo el archivo de input utilizando Reader y StringReader
6) mover importCustomers afuera como method object
7) soportar nuevo requerimiento, que los datos se obtengan de un socket, no solo de un archivo

Usando los refactor automatizados provistos por IntelliJ
--------------------------------------------------------
1) Mover el metodo importCustomers() de la clase Customer a la clase de prueba de unidad ImportCustomerTest:
Refactor -> Move members... method...
Convertir a metodo de instancia, remover static de la firma
Agregar un metodo que pruebe la ejecucion de metodo importCustomers().
2) ...
3) Refactor -> Extract -> Method... asignar nombre setUp, cambiar firma a public y agregar @Before de Junit como anotacion. 

```
Configuration configuration = new Configuration();
configuration.addAnnotatedClass(Customer.class);
configuration.addAnnotatedClass(Address.class);
configuration.configure();

ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
session = sessionFactory.openSession();
session.beginTransaction();
```
Hacer lo mismo con el cierre de la sesion:

```
session.getTransaction().commit();
session.close();
```
4) Para solucionar los errores escondidos, agregar pruebas de unidad adicionales. El motodo de prueba podria ser el siguente:

```
@Test
public void test() throws Exception {
    importCustomers();

    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
    cr.from(Customer.class);

    Query<Customer> query = session.createQuery(cr);
    List<Customer> customers = query.getResultList();

    assertThat(customers).hasSize(2);
}
```

Error 1
```
newCustomer.setIdentificationNumber(customerData[3]);
```

Error 2
```
newAddress.setProvince(addressData[3]);
```

Refactor -> Change Signature... y agregar un argumento de tipo CustomerImporter, inicializar a new CustomerImporter()
(otra opciones es generar un atributo de instancia de tipo CustomerImporter)

Luego pasar a CustomerImporter la session y el reader
Luego mover session y reader a constructor de CustomerImporter: crear constructor y usar Refactor -> Change Signature...
convertir argumentos del constructor a variables de instancia: Refactor -> Extract -> Field...
