package sample.cafekiosk.learning;

import java.util.Objects;

public class Person {

    private final String s;

    public Person(String s) {
        this.s = s;
    }

    public String getS() {
        return s;
    }


    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(!(obj instanceof Person)) return false;
        Person person = (Person) obj;
        return s == person.s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(s);
    }
}
