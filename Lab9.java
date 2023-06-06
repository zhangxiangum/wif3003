import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Lab9 {
    public static void main(String[] args) {
        System.out.println("------------q1----------");
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("z", "Female", 22));
        employees.add(new Employee("x", "Male", 20));
        employees.add(new Employee("a", "Female", 21));
        employees.add(new Employee("h", "Male", 50));
        employees.add(new Employee("h", "Female", 22));

        employees.stream()
                .filter(employee -> employee.getGender().equals("Female") && employee.getAge() >= 21)
                .forEach(employee -> System.out.println(employee.getName()));

        System.out.println("------------q2---------");
        IntStream.rangeClosed(1, 50)
        .parallel()
        .filter(Lab9::isPrime)
        .forEach(number -> System.out.println("Thread: " + Thread.currentThread().getName() + ", Number: " + number));
    }
    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}

class Employee {
    private String name;
    private String gender;
    private int age;

    public Employee(String name, String gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }
}