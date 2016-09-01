package upgrade.ntv.bangsoccer.AppConstants;

/**
 * Created by Jose on 3/15/2015.
 */
public class FirebaseUtils {

        public static String getDivisionRefNodeForSelection(int selectedIndex){
                String division= "div";

                switch (selectedIndex){
                        case 0 :
                                division ="https://bangsoccer-1382.firebaseio.com/Div1_Calendar";
                                break;
                        case 1 :
                                division ="https://bangsoccer-1382.firebaseio.com/Div2_Calendar";
                                break;
                        case 2 :
                                division ="https://bangsoccer-1382.firebaseio.com/Div3_Calendar";
                                break;
                }
                return division;
        }

}




