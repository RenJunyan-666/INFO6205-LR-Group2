package Util;

import javax.swing.*;
import java.util.List;
import java.util.Random;
import Hospital.Hospital;
import Population.Person;
import Population.Population;

public class Main {
    public static void main(String[] args) {
        initHospital();
        initGraph();
        initInfected();
    }

    /**
     * init hospital
     */
    private static int hospitalWidth;
    private static void initHospital() {
        hospitalWidth = Hospital.getInstance().getWidth();
    }

    /**
     * init graph
     */
    private static void initGraph() {
        Graph p = new Graph();
        Thread panelThread = new Thread(p);
        JFrame frame = new JFrame();
        frame.add(p);
        frame.setSize(Factors.CITY_WIDTH + hospitalWidth + 300, Factors.CITY_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("Spread of SARS-CoV-2 simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelThread.start();
    }

    /**
     * init infection
     */
    private static void initInfected() {
        /*List<Person> people = Population.getInstance().getPersonList();
        for (int i = 0; i < Factors.ORIGINAL_COUNT; i++) {
            Person person;
            do {
                person = people.get(new Random().nextInt(people.size() - 1));//choose a healthy person randomly
            } while (person.isInfected());
            person.beInfected();//he will be infected
        }*/

        //set infection source on average
        List<Person> people = Population.getInstance().getPersonList();
        //left section
        for (int i = 0; i < 20; i++) {
            Person person;
            do {
                person = people.get(new Random().nextInt(people.size() - 1));
            } while (person.isInfected() && (person.getX()>0 && person.getX()<230));
            person.beInfected();
        }
        //central section
        for (int i = 0; i < 20; i++) {
            Person person;
            do {
                person = people.get(new Random().nextInt(people.size() - 1));
            } while (person.isInfected() && (person.getX()>230 && person.getX()<460));
            person.beInfected();
        }
        //right section
        for (int i = 0; i < 20; i++) {
            Person person;
            do {
                person = people.get(new Random().nextInt(people.size() - 1));
            } while (person.isInfected() && (person.getX()>460 && person.getX()<700));
            person.beInfected();
        }
    }

}
