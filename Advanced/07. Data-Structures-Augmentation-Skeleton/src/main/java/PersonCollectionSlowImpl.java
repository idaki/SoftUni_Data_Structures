import java.util.*;

public class PersonCollectionSlowImpl {
    private final Set<Person> persons = new HashSet<>();
    private final Map<String, Person> emailToPersonMap = new HashMap<>();
    private final TreeMap<String, List<Person>> emailDomainToPersonsMap = new TreeMap<>();
    private final TreeMap<String, List<Person>> nameAndTownToPersonsMap = new TreeMap<>();
    private final TreeMap<Integer, List<Person>> ageRangeToPersonsMap = new TreeMap<>();

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (emailToPersonMap.containsKey(email)) {
            return false; // Already exists
        }

        Person person = new Person(email, name, age, town);
        persons.add(person);
        emailToPersonMap.put(email, person);

        // Update additional maps
        emailDomainToPersonsMap.computeIfAbsent(getEmailDomain(email), k -> new ArrayList<>()).add(person);
        nameAndTownToPersonsMap.computeIfAbsent(getNameAndTownKey(name, town), k -> new ArrayList<>()).add(person);
        ageRangeToPersonsMap.computeIfAbsent(getAgeRangeKey(age), k -> new ArrayList<>()).add(person);

        return true;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public boolean delete(String email) {
        Person person = emailToPersonMap.get(email);
        if (person == null) {
            return false; // Not found
        }

        persons.remove(person);
        emailToPersonMap.remove(email);

        // Update additional maps
        emailDomainToPersonsMap.getOrDefault(getEmailDomain(email), Collections.emptyList()).remove(person);
        nameAndTownToPersonsMap.getOrDefault(getNameAndTownKey(person.getName(), person.getTown()), Collections.emptyList()).remove(person);
        ageRangeToPersonsMap.getOrDefault(getAgeRangeKey(person.getAge()), Collections.emptyList()).remove(person);

        return true;
    }

    @Override
    public Person find(String email) {
        return emailToPersonMap.get(email);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        return emailDomainToPersonsMap.getOrDefault(emailDomain, Collections.emptyList());
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        return nameAndTownToPersonsMap.getOrDefault(getNameAndTownKey(name, town), Collections.emptyList());
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        return ageRangeToPersonsMap.subMap(startAge, true, endAge, true).values().stream()
                .flatMap(List::stream)
                .toList();
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        return ageRangeToPersonsMap.subMap(startAge, true, endAge, true).values().stream()
                .flatMap(List::stream)
                .filter(person -> person.getTown().equals(town))
                .toList();
    }

    private String getEmailDomain(String email) {
        int atIndex = email.indexOf('@');
        return atIndex != -1 ? email.substring(atIndex + 1) : "";
    }

    private String getNameAndTownKey(String name, String town) {
        return name + "|" + town;
    }

    private int getAgeRangeKey(int age) {
        // Adjust this method if you want to bucket ages differently
        return age;
    }
}
