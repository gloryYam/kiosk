package sample.cafekiosk.learning;

import java.util.HashMap;
import java.util.Map;

public class Study {


    public static void main(String[] args) {
        String s1 = new String("김영광");
        String s2 = new String("김영광");
        System.out.println(s1.equals(s2));

        Person p1 = new Person("김영광");
        Person p2 = new Person("김영광");
        System.out.println(p1.equals(p2));
        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());

        Map<Person, String> map = new HashMap<>();
        map.put(p1, "김영광");

        System.out.println(map.get(p2));
    }
}