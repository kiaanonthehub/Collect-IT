package Model;

import java.util.ArrayList;
import java.util.List;

public class Global {

    // variables
    public static String userID;
    public static String userEmail;
    public static String path;
    public static String category;
    public static String item;

    // collection - arraylists
    public static ArrayList<String> lstStrings;
    public static ArrayList<String> lstViewCategory;
    public static ArrayList<String> lstViewItems;
    public static ArrayList<Category> lstCategory;
    public static ArrayList<Item> lstItems;

    public static String getUsername(String email) {
        // get substring of @ and use as username for user
        String[] split = email.split("@");

        return split[0];
    }


}
