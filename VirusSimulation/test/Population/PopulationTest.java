package Population;

import Util.Factors;
import org.junit.Test;

import static org.junit.Assert.*;

public class PopulationTest {

    @Test
    public void getPeopleSize() {
        Factors.CITY_NUMBER=1;
        Factors.CITY_PERSON_SIZE=5000;
       int i= Population.getInstance().getPeopleSize(State.NORMAL);
       assertEquals(5000,i);
    }


}