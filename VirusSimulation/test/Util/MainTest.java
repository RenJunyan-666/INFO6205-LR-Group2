package Util;

import Population.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void InitTest() {
        Factors.INITINFECTED_MODEL=2;
        Factors.ORIGINAL_COUNT=50;
        String[] args={"1"};
        Main.main(args);

        int x =Population.getInstance().getPeopleSize(State.SHADOW);
        assertEquals(60,x);
    }

}