package Util;

public class Factors {
    public static int ORIGINAL_COUNT = 50;//Original infection
    public static float BROAD_RATE = 0.8f;//Virus transmission rate
    public static float SHADOW_TIME = 140;//Shadow period (1 day equals value of 10)
    public static int HOSPITAL_RECEIVE_TIME = 10;//Response time
    public static int BED_COUNT = 1000;//Number of beds
    /**
     * The prevalence of testing and contact tracing range：[-0.99,0.99]
     * -0.99 people will contact with low level of intention
     * 0.99 people will contact with high level of intention
     */
    public static float Contact_Intention = -0.99f;
    public static int CITY_PERSON_SIZE = 5000;//size of people in this city
    public static float FATALITY_RATE = 0.50f;//fatality rate
    public static int DIE_TIME = 100;//from diagnosis to death
    public static double DIE_VARIANCE = 1;//variance of death time
    /**
     * range of city
     */
    public static final int CITY_WIDTH = 700;
    public static final int CITY_HEIGHT = 800;
}
