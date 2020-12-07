package Population;

import City.City;
import Util.Factors;
import Util.Graph;
import Util.Move;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class PersonTest {



    @Test
    public void beInfected() {
        City city = new City(400, 400);
        Person p=new Person(city, 40, 40);
        p.beInfected();
        assertEquals(p.getState(),State.SHADOW);
    }

    @Test
    public void getDistanceTest() {
        City city = new City(400, 400);
        Person p1=new Person(city, 42, 40);
        Person p2=new Person(city, 40, 40);
        double x=p1.distance(p2);
        assertEquals(2,x,0);
    }

    @Test
    public void actionTest() {
        Factors.Quarantine_BED_COUNT=1000;
        Factors.Quarantine_WAIT_TIME=140;
        Factors.BED_COUNT=0;

        Factors.Contact_Intention=3;

        City city = new City(400, 400);
        //new person
        Person p=new Person(city, 40, 399);
        //across middle line
        Move move=new Move(40,401);
        p.setMove(move);
        p.update();
        assertEquals(true,p.getneedIsolation());
        p.update();
        assertEquals(false,p.getneedIsolation());




    }
    @Test
    public void QuarantineTest() {
        Factors.Quarantine_BED_COUNT=100;
        Factors.Quarantine_WAIT_TIME=140;

        Graph.worldTime=0;
        City city = new City(400, 400);
        //new person
        Person p2=new Person(city, 40, 399);
        //across middle line
        Move move=new Move(40,401);
        p2.setMove(move);
        p2.update();
        assertEquals(true,p2.getneedIsolation());

        p2.update();
        assertEquals(false,p2.getneedIsolation());

        //leaving Quarantine
        Graph.worldTime=150;
        p2.update();
        assertEquals(false,p2.getIsolating());



    }

    @Test
    public void CureUpdateTest() {
        Graph.worldTime=0;
        Factors.Quarantine_BED_COUNT=0;
        Factors.BED_COUNT=0;
        Factors.HARD_TIME=140;
        Factors.FATALITY_RATE=0;
        //100%cure
        Factors.CURE_RATE=1;
        //cure for State.Confirmed
        City city = new City(400, 400);
        Person p=new Person(city, 40, 40);
        p.beInfected();
        p.infectedTime=0;
        assertEquals(p.getState(),State.SHADOW);
        Graph.worldTime=150;
        p.update();
        assertEquals(State.CONFIRMED,p.getState());
        Graph.worldTime=300;
        p.update();
        assertEquals(State.NORMAL,p.getState());
        //cure for State.Freeze
        Factors.SHADOW_TIME=140;
        Factors.HOSPITAL_RECEIVE_TIME=30;
        Person p2=new Person(city, 60, 60);
        p2.setState(State.FREEZE);
        p2.infectedTime=0;
        p2.confirmedTime=30;
        p2.update();
        assertEquals(State.NORMAL,p2.getState());


    }

    @Test
    public void UpdateDeath() {
        Graph.worldTime=30;
        Factors.Quarantine_BED_COUNT=0;
        Factors.BED_COUNT=0;
        Factors.HARD_TIME=140;

        City city = new City(400, 400);
        Person p=new Person(city, 40, 40);
        p.setState(State.CONFIRMED);
        p.dieMoment=20;
        p.update();
        assertEquals(400,p.getX());
    }
}