package Population;

import City.*;
import Hospital.*;
import Util.*;

import java.util.List;
import java.util.Random;

public class Person extends Point implements State{
    private City city;
    private Move move;
    private Boolean Super=false;//whether super infector
    private Boolean Cure=false;//whether cure from this disease
    private Boolean needIsolation=false;//
    private Boolean Isolating=false;//

    int sigma = 1;
    /**
     * Gaussian distribution--N(u,sigma)
     */
    double targetXU;//mean of x-axis
    double targetYU;//mean of y-axis
    double targetSig = 50;//variance

    public Person(City city, int x, int y) {
        super(x, y);
        this.city = city;

        //init people position
        targetXU = MathUtil.stdGaussian(100, x);
        targetYU = MathUtil.stdGaussian(100, y);

    }
    public Boolean getSuper() {
        return Super;
    }
    public Boolean getCure() {
        return Cure;
    }
    public Boolean getneedIsolation() {
        return needIsolation;
    }
    public Boolean getIsolating() {
        return Isolating;
    }

    /**
     * people prefer to move when stdGaussian is positive
     * <p>
     * define X matches Gaussian distribution--N(u,sigma)
     * Sigma-->affect the distribution of the overall population mobility intention
     * u-->the axis of normal distribution is to make more people prefer to flow or to be lazy.
     * <p>
     * process：
     * StdX = (X-u)/sigma
     * X = sigma * StdX + u
     */
    public boolean wantMove() {
        return MathUtil.stdGaussian(sigma, Factors.Contact_Intention) > 0;
    }

    private int state = State.NORMAL;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    int infectedTime = 0;//time of being infected
    int confirmedTime = 0;//time of being confirmed
    int dieMoment = 0;//time of death
    int isolationMoment = 0;//time of Isolation

    public boolean isInfected() {
        return state >= State.SHADOW;
    }

    public void beInfected() {
        float fate = new Random().nextFloat();
        if (fate < Factors.K ){
            Super=true;
        }
        state = State.SHADOW;
        infectedTime = Graph.worldTime;//time start from becoming shadow patient
    }

    //distance between two different people points
    public double distance(Person person) {
        /*for(Block b : city.getBlockList()){
            if((getX() >= b.getBlockX() && getX() <= b.getBlockX()+20) && (getY() >= b.getBlockY() && getY() <= b.getBlockY()+20)){
                //the person is blocked
                return SAFE_DIST-0.1;//the person must be close to each other
            }
        }*/
        return Math.sqrt(Math.pow(getX() - person.getX(), 2) + Math.pow(getY() - person.getY(), 2));
    }

    //put patient in the hospital
    private void freezy() {
        state = State.FREEZE;
    }

    //action of people in different states
    private void action() {
        if (state == State.FREEZE || state == State.DEATH||Isolating) {
            return;//cannot move
        }
        if (!wantMove()) {
            return;
        }
        if (move == null || move.isArrived()) {
            //set next target position randomly
            double targetX = MathUtil.stdGaussian(targetSig, targetXU);
            double targetY = MathUtil.stdGaussian(targetSig, targetYU);
            move = new Move((int) targetX, (int) targetY);
        }
        //if across the boundary of city,people need Isolation
        if((getY()-400)*(move.getY()-400)<0){
            needIsolation=true;
            if(move.getY()<400){
                city=new City(200,200);
            }else{
                city=new City(500,500);
            }
        }
        int dX = move.getX() - getX();
        int dY = move.getY() - getY();
        double length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));//distance between start point and target point

        //arrive target point
        if (length < 1) {
            move.setArrived(true);
            return;
        }

        int udX = (int) (dX / length);//move in X-axis
        if (udX == 0 && dX != 0) {
            if (dX > 0) {
                udX = 1;
            } else {
                udX = -1;
            }
        }

        int udY = (int) (dY / length);//move in Y-axis
        if (udY == 0 && dY != 0) {
            if (dY > 0) {
                udY = 1;
            } else {
                udY = -1;
            }
        }

        //move beyond the boundary(X-axis)
        if (getX() > Factors.CITY_WIDTH || getX() < 0) {
            move = null;
            if (udX > 0) {
                udX = -udX;
            }
        }
        //move beyond the boundary(Y-axis)
        if (getY() > Factors.CITY_HEIGHT || getY() < 0) {
            move = null;
            if (udY > 0) {
                udY = -udY;
            }
        }
        moveTo(udX, udY);
    }

    public Bed useBed;

    private float SAFE_DIST = 1.8f;//safe distance

    //have mask or not
    public boolean mask(){
        float possibility = new Random().nextFloat();//the possibility of mask is 50%
        if(possibility <= Factors.MASK_RATE)
            return true;
        else return false;
    }

    //update state of people
    public void update() {
        //if (state == State.FREEZE || state == State.DEATH) {
        if (state == State.DEATH) {

            return;//don't need to update
        }
        //update people for Quarantine
        if(needIsolation){
            Bed bed = Quarantine.getInstance().pickBed();//find empty beds
            if (bed == null) {
                //System.out.println("No beds!");
            } else {
                useBed = bed;
                needIsolation=false;
                Isolating=true;
                isolationMoment=Graph.worldTime;
                setX(bed.getX());
                setY(bed.getY());
                bed.setEmpty(false);
            }
        }
        //leave Quarantine,only the person who appear normal and wait enough time can leave


        if(Isolating){
            if(Graph.worldTime-isolationMoment>=Factors.Quarantine_WAIT_TIME &&state!=State.CONFIRMED){
                Isolating=false;
                Quarantine.getInstance().returnBed(useBed);
                Random random=new Random();
                //leave area to  city
                    int x=(int) (100 * random.nextGaussian() + city.getCenterX());
                    int y=(int) (100 * random.nextGaussian() + city.getCenterX());
                    if (x > 700) x = 700;
                    setX(x);
                    setY(y);

                }
            }



        City city1 = new City(200, 200);
        City city2 = new City(500, 500);
        //cure chance for confirmed after hard time
        if(Graph.worldTime-confirmedTime>=Factors.HARD_TIME){
        if (state == State.CONFIRMED&& dieMoment == 0 ) {
            float destiny=new Random().nextFloat();
            if(destiny<=Factors.CURE_RATE){
                state = State.NORMAL;
                dieMoment = 0;
                Super=false;
                Cure=true;


            }
        }}
        //cure chance for freeze after  hard time
        if(Graph.worldTime-confirmedTime>=(Factors.HARD_TIME)){
        if (state == State.FREEZE ) {
            float destiny=new Random().nextFloat();
            if(destiny<=Factors.CURE_RATE){
                state = State.NORMAL;
                dieMoment = 0;
                Super=false;
                Cure=true;
                Hospital.getInstance().returnBed(useBed);
                Random random=new Random();
                if(Graph.worldTime%2==0){ //leave hospital to random city
                int x=(int) (100 * random.nextGaussian() + city1.getCenterX());
                int y=(int) (100 * random.nextGaussian() + city1.getCenterX());
                    if (x > 700) x = 700;
                setX(x);
                setY(y);
                }else{
                    int x=(int) (100 * random.nextGaussian() + city2.getCenterX());
                    int y=(int) (100 * random.nextGaussian() + city2.getCenterX());
                    if (x > 700) x = 700;
                    setX(x);
                    setY(y);
                }
            }
        }}

        //update patient
        if (state == State.CONFIRMED && dieMoment == 0) {

            int destiny = new Random().nextInt(10000) + 1;//random number[1,10000]
            if (1 <= destiny && destiny <= (int) (Factors.FATALITY_RATE * 10000)) {
                //fate is death
                int dieTime = (int) MathUtil.stdGaussian(Factors.DIE_VARIANCE, Factors.DIE_TIME);
                dieMoment = confirmedTime + dieTime;
            } else {
                dieMoment = -1;//survive
            }
        }

        //update patient for hospitalization
        if (state == State.CONFIRMED && Graph.worldTime - confirmedTime >= Factors.HOSPITAL_RECEIVE_TIME) {
            Bed bed = Hospital.getInstance().pickBed();//find empty beds
            if (bed == null) {
               // System.out.println("No beds!");
            } else {
                useBed = bed;
                state = State.FREEZE;
                setX(bed.getX());
                setY(bed.getY());
                bed.setEmpty(false);
            }
        }

        //update dead
        if ((state == State.CONFIRMED || state == State.FREEZE) && Graph.worldTime >= dieMoment && dieMoment > 0) {
            state = State.DEATH;
            Hospital.getInstance().returnBed(useBed);
            setX(400);
            setY(400);
        }

        //update shadow patients
        double stdRnShadowtime = MathUtil.stdGaussian(25, Factors.SHADOW_TIME / 2);
        if (Graph.worldTime - infectedTime > stdRnShadowtime && state == State.SHADOW) {
            state = State.CONFIRMED;
            confirmedTime = Graph.worldTime;
        }

        //update people's action
        action();

        //update infection
        List<Person> people = Population.getInstance().personList;
        if (state >= State.SHADOW) {
            return;
        }

        //people below safe distance with each other can be infected randomly
        for (Person person : people) {
            //Normal,hospital,Isolating and death people never infect others
            if (person.getState() == State.NORMAL||person.getState() == State.DEATH||person.getIsolating()||person.getState() == State.FREEZE) {
                continue;
            }
/*
            //without mask factor
            float fate = new Random().nextFloat();
            if (fate < Factors.BROAD_RATE && distance(person) < SAFE_DIST) {
                this.beInfected();
                break;
            }*/

            /**
             * usage mask
             * <p>
             * usually broad rate of virus is 0.8
             * one person with mask will be infected with 0.2 possibility
             * <p>
             * so 0.2 * 0.8 --> one with mask and another without mask
             * 0.2 * 0.2 --> both with mask
             */

            float fate = new Random().nextFloat();
            float risk= Factors.BROAD_RATE;
            if(person.getSuper()){//whether super Infector
                risk=Factors.BROAD_RATE_SUPER;
            }

            if(this.Cure){//Only 20 percent of those who recover would infected again
                risk=risk*Factors.RE_INFECTED_RATE;
            }
            if (this.mask()){
                risk=risk*Factors.MASK;
            }
            if(person.mask()){
                risk=risk*Factors.MASK;
            }

            if (fate < risk && distance(person) < SAFE_DIST) {
                this.beInfected();
                break;
            }


            /*float fate = new Random().nextFloat();
            if(this.mask()){
                if(person.mask()){//both with mask
                    if (fate < Factors.MASK * Factors.MASK && distance(person) < SAFE_DIST) {
                        this.beInfected();
                        break;
                    }
                }
                else {//one with mask
                    if (fate < Factors.MASK * Factors.BROAD_RATE && distance(person) < SAFE_DIST) {
                        this.beInfected();
                        break;
                    }
                }
            }
            else {
                if (fate < Factors.BROAD_RATE && distance(person) < SAFE_DIST) {
                    this.beInfected();
                    break;
                }
            }*/
        }
    }
}
