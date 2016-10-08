package upgrade.ntv.bangsoccer.booking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jose Rivera on 9/28/2016.
 */
public class MyMatcher {
    public static int MatchHourToInt(String hora) {

        String line = hora;
        String pattern = "(\\d+)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            return Integer.parseInt(m.group(0));

        }else {
            System.out.println("NO MATCH");
            return -1;
        }

    }
}
