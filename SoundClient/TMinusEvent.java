import frc.robot.Robot;
import org.mayheminc.robot2019.Robot;

class TMinusEvent extends OneTimeEvent {
    String name;
    int time;
    final double MATCH_TIME_SEC = 150.0;

    public TMinusEvent(String S, int T) {
        name = S;
        time = T;

    }

    public String OneTimeExecute() {

        if ((MATCH_TIME_SEC - ((System.currentTimeMillis() / 1000.0) - Robot.getMatchStartTime())) > time) {
            return name;
        }
        return "";
    }

}