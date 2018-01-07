import com.x.javalite.test.repository.Car;
import org.javalite.activejdbc.DB;

import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {
        standardExample();
        reflectionExample();
    }

    private static void reflectionExample() {
        try {
            System.out.println("RUNNING REFLECTION CODE");
            Class<?> db = Class.forName("org.javalite.activejdbc.DB");
            Object dbObject = db.newInstance();
            Method method = dbObject.getClass().getMethod("open", String.class, String.class, String.class, String.class);
            method.invoke(dbObject, "org.h2.Driver", "jdbc:h2:mem:cars;DB_CLOSE_DELAY=-1", "sa", "");

            method = dbObject.getClass().getMethod("exec", String.class);
            method.invoke(dbObject, "DROP TABLE IF EXISTS cars");

            method = dbObject.getClass().getMethod("exec", String.class);
            method.invoke(dbObject, "CREATE TABLE cars (id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, car_name VARCHAR(56))");

            method = dbObject.getClass().getMethod("openTransaction");
            method.invoke(dbObject);
            Car.deleteAll();
            System.out.println(Car.count());
            method = dbObject.getClass().getMethod("commitTransaction");
            method.invoke(dbObject);

            method = dbObject.getClass().getMethod("openTransaction");
            method.invoke(dbObject);
            System.out.println(Car.count());
            Car.createIt("car_name", "toyota");
            method = dbObject.getClass().getMethod("commitTransaction");
            method.invoke(dbObject);

            method = dbObject.getClass().getMethod("openTransaction");
            method.invoke(dbObject);
            System.out.println(Car.count());
            method = dbObject.getClass().getMethod("commitTransaction");
            method.invoke(dbObject);


            method = dbObject.getClass().getMethod("close");
            method.invoke(dbObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void standardExample() {
        System.out.println("RUNNING STANDARD CODE");

        DB db = new DB();
        db.open("org.h2.Driver", "jdbc:h2:mem:cars;DB_CLOSE_DELAY=-1", "sa", "");
        db.exec("DROP TABLE IF EXISTS cars");
        db.exec("CREATE TABLE cars (id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, car_name VARCHAR(56))");

        db.openTransaction();
        Car.deleteAll();
        System.out.println(Car.count());
        db.commitTransaction();

        db.openTransaction();
        System.out.println(Car.count());
        Car.createIt("car_name", "toyota");
        db.commitTransaction();

        db.openTransaction();
        System.out.println(Car.count());
        db.commitTransaction();

        db.close();
    }
}
