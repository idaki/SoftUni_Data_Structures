import java.util.ArrayList;
import java.util.List;

public class PersonCollectionImpl implements PersonCollection {

    private List<Person> personList;

    public PersonCollectionImpl() {
        this.personList = new ArrayList<>();
    }

    @Override
    public boolean add(String email, String name, int age, String town) {

        Person person = new Person(email, name, age, town);
        return personList.add(person);
    }

    @Override
    public int getCount() {

        return personList.size();
    }

    @Override
    public boolean delete(String email) {

        Person personToRemove = find(email);
        return personList.remove(personToRemove);
    }

    @Override
    public Person find(String email) {

        for (Person person : personList) {
            if (person.getEmail().equals(email)) {
                return person;
            }
        }
        return null; }

    @Override
    public Iterable<Person> findAll(String emailDomain) {

        List<Person> result = new ArrayList<>();
        for (Person person : personList) {
            if (person.getEmail().endsWith("@" + emailDomain)) {
                result.add(person);
            }
        }
        return result;
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {

        List<Person> result = new ArrayList<>();
        for (Person person : personList) {
            if (person.getName().equals(name) && person.getTown().equals(town)) {
                result.add(person);
            }
        }
        return result;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {

        List<Person> result = new ArrayList<>();
        for (Person person : personList) {
            int age = person.getAge();
            if (age >= startAge && age <= endAge) {
                result.add(person);
            }
        }
        return result;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {

        List<Person> result = new ArrayList<>();
        for (Person person : personList) {
            int age = person.getAge();
            if (age >= startAge && age <= endAge && person.getTown().equals(town)) {
                result.add(person);
            }
        }
        return result;
    }
}
