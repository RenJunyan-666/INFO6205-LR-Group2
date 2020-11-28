package Hospital;

import Util.Factors;
import Util.Point;

import java.util.ArrayList;
import java.util.List;
public class Mortuary extends Point {
    public static final int Mortuary_X = 760;
    public static final int Mortuary_Y = 80;
    private int width;
    private int height = 600;
    private static Mortuary mortuary = new Mortuary();
    private Point point = new Point(Mortuary_X, Mortuary_Y);//The fixed position of first bed
    private List<Bed> beds = new ArrayList<>();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static Mortuary getInstance() {
        return mortuary;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    private Mortuary() {
        super(Mortuary_X, Mortuary_Y + 10);//position of hospital
        //adjust the range of hospital according to beds
        if (Factors.BED_COUNT == 0) {
            width = 0;
            height = 0;
        }
        int column = Factors.BED_COUNT / 100;
        width = column * 6;
        for (int i = 0; i < column; i++) {
            for (int j = 10; j <= 606; j += 6) {
                Bed bed = new Bed(point.getX() + i * 6, point.getY() + j);
                beds.add(bed);
                if (beds.size() >= Factors.BED_COUNT) break;
            }
        }
    }

    //occupy bed position
    public Bed pickBed() {
        for (Bed bed : beds) {
            if (bed.isEmpty()) {
                return bed;
            }
        }
        return null;
    }

    //return empty bed when death or recovery
    public Bed returnBed(Bed bed) {
        if (bed != null) {
            bed.setEmpty(true);
        }
        return bed;
    }
}

