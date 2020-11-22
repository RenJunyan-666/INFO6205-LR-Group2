package Util;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import Hospital.Hospital;
import Population.*;

public class Graph extends JPanel implements Runnable {
    public Graph() {
        super();
        this.setBackground(new Color(0x444444));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(new Color(0x00ff00));//hospital boundary color
        //plot hospital boundary
        g.drawRect(Hospital.getInstance().getX(), Hospital.getInstance().getY(), Hospital.getInstance().getWidth(), Hospital.getInstance().getHeight());
        g.setFont(new Font("Tahoma", Font.BOLD, 10));
        g.setColor(new Color(0x00ff00));
        g.drawString("Hospital", Hospital.getInstance().getX() + Hospital.getInstance().getWidth() / 4, Hospital.getInstance().getY() - 16);
        //plot people points
        java.util.List<Person> people = Population.getInstance().getPersonList();
        if (people == null) {
            return;
        }
        for (Person person : people) {
            switch (person.getState()) {
                case Person.NORMAL: {
                    //normal people
                    g.setColor(new Color(0xdddddd));
                    break;
                }
                case Person.SHADOW: {
                    //people with shadow period
                    g.setColor(new Color(0xffee00));
                    break;
                }

                case Person.CONFIRMED: {
                    //confirmed patients
                    g.setColor(new Color(0xff0000));
                    break;
                }
                case Person.FREEZE: {
                    //isolators
                    g.setColor(new Color(0x48FFFC));
                    break;
                }
                case Person.DEATH: {
                    //dead
                    g.setColor(new Color(0x000000));
                    break;
                }
            }
            person.update();//update states of people
            g.fillOval(person.getX(), person.getY(), 3, 3);
        }

        int captionStartOffsetX = 700 + Hospital.getInstance().getWidth() + 40;
        int captionStartOffsetY = 40;
        int captionSize = 24;

        //show data
        g.setColor(Color.WHITE);
        g.drawString("total people: " + Factors.CITY_PERSON_SIZE, captionStartOffsetX, captionStartOffsetY);
        g.setColor(new Color(0xdddddd));
        g.drawString("normal people: " + Population.getInstance().getPeopleSize(Person.NORMAL), captionStartOffsetX, captionStartOffsetY + captionSize);
        g.setColor(new Color(0xffee00));
        g.drawString("shadow patients: " + Population.getInstance().getPeopleSize(Person.SHADOW), captionStartOffsetX, captionStartOffsetY + 2 * captionSize);
        g.setColor(new Color(0xff0000));
        g.drawString("confirmed patients: " + Population.getInstance().getPeopleSize(Person.CONFIRMED), captionStartOffsetX, captionStartOffsetY + 3 * captionSize);
        g.setColor(new Color(0x48FFFC));
        g.drawString("isolators: " + Population.getInstance().getPeopleSize(Person.FREEZE), captionStartOffsetX, captionStartOffsetY + 4 * captionSize);
        g.setColor(new Color(0x00ff00));
        g.drawString("empty beds: " + Math.max(Factors.BED_COUNT - Population.getInstance().getPeopleSize(Person.FREEZE), 0), captionStartOffsetX, captionStartOffsetY + 5 * captionSize);
        //define the number of demand of beds equals confirmed patients - isolators
        int needBeds = Population.getInstance().getPeopleSize(Person.CONFIRMED) - Population.getInstance().getPeopleSize(Person.FREEZE);
        g.setColor(new Color(0xE39476));
        g.drawString("needed beds: " + (needBeds > 0 ? needBeds : 0), captionStartOffsetX, captionStartOffsetY + 6 * captionSize);
        g.setColor(new Color(0xccbbcc));
        g.drawString("dead: " + Population.getInstance().getPeopleSize(Person.DEATH), captionStartOffsetX, captionStartOffsetY + 7 * captionSize);
        g.setColor(new Color(0xffffff));
        g.drawString("world time(day): " + (int) (worldTime / 10.0), captionStartOffsetX, captionStartOffsetY + 8 * captionSize);

    }

    public static int worldTime = 0;
    public java.util.Timer timer = new Timer();

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Graph.this.repaint();
            worldTime++;
        }
    }

    @Override
    public void run() {
        timer.schedule(new MyTimerTask(), 0, 100);
    }
}