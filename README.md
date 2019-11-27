# atddi_10p

#### Referencias
https://www.baeldung.com/hibernate-criteria-queries

#### Acceso al material del curso
* URL: http://tinyurl.com/TDDAvanzadoNov2019
* Clave: SuperTDDJP

#### Claves de soluciones
* Clave solucion 1: import1
* Clave solucion 2: solucion2
* Clave solucion 3: 3pasos
* Clave solucion 4: porfin
* Clave solucion 5 (sin BD): final

## Import Customer
---------------

### Dia 3

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

```java
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

```java
session.getTransaction().commit();
session.close();
```
4) Para solucionar los errores escondidos, agregar pruebas de unidad adicionales. El motodo de prueba podria ser el siguente:

```java
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
```java
newCustomer.setIdentificationNumber(customerData[3]);
```

Error 2
```java
newAddress.setProvince(addressData[3]);
```

Refactor -> Change Signature... y agregar un argumento de tipo CustomerImporter, inicializar a new CustomerImporter()
(otra opciones es generar un atributo de instancia de tipo CustomerImporter)

Luego pasar a CustomerImporter la session y el reader
Luego mover session y reader a constructor de CustomerImporter: crear constructor y usar Refactor -> Change Signature...
convertir argumentos del constructor a variables de instancia: Refactor -> Extract -> Field...

### Dia 4

Asegurar que existe un method objet llamado CustomerImporter que incluye un metodo llamado importCustomers(Session, Reader) que realiza la importación de los registros obtenidos desde del Reader en la base de datos.
Luego aplicar refactors automaticos para mejorar la implementación de CustomerImporter, para que resulte mas simple y clara. Como puede ser la siguiente:
```java
public void importCustomers(Session session, Reader reader) throws IOException {
    lineReader = new LineNumberReader(reader);
    nextRecord();

    while (hasRecord()) {
        createCustomer(session);
        nextRecord();
    }

    reader.close();
}
```
Agregar pruebas de unidad para comenzar a validar los registros y sus contenidos, como cantidad de datos por registro y tipos de datos y valores esperados:
1) Primer registro de dirección
2) Registro de cliente identificado con más de una letra C
3) Registro de cliente con menos de 5 campos
4) Registro de cliente con mas de 5 campos
5) Registro de cliente identificado con más de una letra C
6) Registro de direccion con menos o más de 6 campos
7) Otro tipo de letra o letras que identifican tipo de registro que no son las soportadas
...

El test de unidad CustomerImporterTest tiene como miembro la Session de Hibernate que mantiene el acceso a la base de datos. Es necesario abstraer al test de la Session, y se logra mediante la abstracción System:

                      +-------------------------+
                      |      CustomerSystem     |
                      +-------------------------+
                      |                         |
                      +-------------------------+
                                   ^
                                   |
                    +--------------+---------------+
                    |                              |
       +------------+------------+    +------------+-------------+
       | TransientCustomerSystem |    | PersistentCustomerSystem |
       +-------------------------+    +--------------------------+
       |                         |    |                          |
       +-------------------------+    +--------------------------+
       
Dado que la implementación de CustomerSystem a usar dependen del ambiente en cual se ejecutará el código, por ejemplo ambiente de desarrollo o de producción, se deberá disponer de otra abstracción para esto que también cambia, llamada Environment.

### Dia 5

Mover Session en CustomerImporterTest a una nueva clase, PersistentCustomerSystem. Utilizar refactor automatizado de IntelliJ: Refactor -> Extract -> Delegate... sobre Session selecionado. Definir los siguientes datos para completar el refactor:
* Name of the new class: PersistentCustomerSystem
* Target destination directory: ...\src\main\java\ar\net\mgardos
* Members to extract: debe estar seleccionada la variable session
* Visibility: Private
* Generate accessors: debe estar tildado

Mover la configuracion de Hibernate y creación de session en metodo setUp del test de unidad a PersistentCustomerSystem:

```java
@Before
public void setUp() {
    Configuration configuration = new Configuration();
    configuration.addAnnotatedClass(Customer.class);
    configuration.addAnnotatedClass(Address.class);
    configuration.configure();

    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    persistentCustomerSystem.setSession(sessionFactory.openSession());
    persistentCustomerSystem.getSession().beginTransaction();
}
```

Seleccionar todas excepto la ultima linea de código del método setUp y emplear Refactor -> Extract -> Method... creando el nuevo método initialize. Luego seleccionar la variable persistentCustomerSystem dentro del método initialize y emplear Refactor -> Extract -> Parameter.... Para mover el método initialize a PersistentCustomerSystem, seleccionar el nombre del método y aplicar Refactor -> Move Instance Method... y de las opciones dentro del marco Select an instance expression elegir PersistentCustomerSystem persistentCustomerSystem identificado con el ícono que incluye la letra P y seleccionar Visibility Public.

Realizar el procedimiento anterior con la última línea de código que inicia una transacción, generando finalmente el método start en PersistentCustomerSystem.

El código resultante en el test de unidad será el siguiente:

```java
@Before
public void setUp() {
    persistentCustomerSystem.initialize();
    persistentCustomerSystem.start();
}
```

Respecto a método tearDown en el test de unidad, es necesario seguir el mismo procedimiento que para refactorizar en forma automatica el método setUp:

```java
@After
public void tearDown() {
    persistentCustomerSystem.getSession().getTransaction().commit();
    persistentCustomerSystem.getSession().close();
}
```

Luego continuear refactorizando en forma automatica para mover del test de unidad a PersistentCustomerSystem todo código destinado a acceso a base de datos.

Nuevo requerimiento a implementar con TDD
-----------------------------------------

El sistema ahora pasa a ser ERP, no solo importador de clientes. Se recibiran registros para proveedor o Supplier que tendrá asociados uno o mas clientes. El cliente se identifica en el registro con códigos NC (New Customer) y EC (Existing Customer).

Registro de ejemplo de proveedor:
```
S,Supplier1,D,123
NC,Pepe,Sanchez,D,123456789
EC,D,987654321
```

Surge la necesidad de disponer del ERPSystem que se encarga de administrar la Session y conoce al CustomerSystem y SupplierSystem. Este nuevo sistema, ERPSystem, pasa a ser la raíz de los diversos sub-sistemas y cuando se requiere algún sub-sistema, se le debe solicitar a ERPSystem. 
