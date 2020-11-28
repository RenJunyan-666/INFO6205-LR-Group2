package Population;

public interface State {
    int NORMAL = 0;//normal people
    int SUSPECTED = NORMAL + 1;//suspected patients
    int SHADOW = SUSPECTED + 1;//shadow patients
    int CONFIRMED = SHADOW + 1;//confirmed patients
    int FREEZE = CONFIRMED + 1;//freeze patients
    int DEATH = FREEZE + 1;//dead
    //int ISOLATION = DEATH+1;//people in quarantine
}
