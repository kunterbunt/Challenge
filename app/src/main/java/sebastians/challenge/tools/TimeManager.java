package sebastians.challenge.tools;

/**
 * Created by kunterbunt on 22.06.15.
 */
public class TimeManager {

    /**
     * Use this tag for logging purposes.
     */
    public static final String LOG_TAG = "TimeManager";

    private TimeManager() {

    }

    /**
     * @param ms
     * @return A String representing the duration in hours or days.
     */
    public static String convertMillisecondsToDateString(long ms) {
        String durationString;
        ms /= 3600;
        if (ms % 24 == 0) {
            ms /= 24;
            if (ms > 1) {
                durationString = ms + " days";
            } else {
                durationString = ms + " day";
            }
        } else {
            if (ms > 1) {
                durationString = ms + " hours";
            } else {
                durationString = ms + " hour";
            }
        }
        return durationString;
    }
}
