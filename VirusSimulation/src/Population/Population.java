package Population;

import City.City;
import Util.Factors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
    private static Population population = new Population();

    public static Population getInstance() {
        return population;
    }

    List<Person> personList = new ArrayList<Person>();

    public List<Person> getPersonList() {
        return personList;
    }

    public int getPeopleSize(int state) {
        if (state == -1) {
            return personList.size();
        }
        int i = 0;
        for (Person person : personList) {
            if (person.getState() == state) {
                i++;
            }
        }
        return i;
    }

    private Population() {
        City city = new City(400, 400);//position of city center-->(400,400)
        //add people in this city
        for (int i = 0; i < Factors.CITY_PERSON_SIZE; i++) {
            Random random = new Random();
            //generate the position of people-->N(a,b)：Math.sqrt(b)*random.nextGaussian()+a
            int x = (int) (100 * random.nextGaussian() + city.getCenterX());
            int y = (int) (100 * random.nextGaussian() + city.getCenterY());
            if (x > 700) x = 700;//people cannot be out the range of city
            personList.add(new Person(city, x, y));
        }
    }
}
