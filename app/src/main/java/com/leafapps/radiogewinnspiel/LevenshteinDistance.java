package com.leafapps.radiogewinnspiel;

import android.content.Context;
import android.widget.Toast;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.*;

public class LevenshteinDistance {
    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
    private static Context context;


    public static void setContext(Context con){
        context = con;
    }

    public static int CompareStrings(String userstring, String inetstring) {
        CharSequence lhs = (CharSequence) userstring;
        CharSequence rhs = (CharSequence) inetstring;
/*
        if ( userstring.isEmpty()) {
            Toast.makeText(context, "Geben Sie einen Text zum suchen ein.", Toast.LENGTH_SHORT).show();
        }
*/
        if ( !userstring.isEmpty() && !inetstring.isEmpty()) {
           /*
            int dist = computeLevenshteinDistance(lhs, rhs);
            int lower = min(lhs.length(), rhs.length());
            int upper = max(lhs.length(), rhs.length());
            if (rhs.toString().toUpperCase().contains(lhs.toString().toUpperCase()) || dist <= upper - lower + 2)
            */
            if (rhs.toString().toUpperCase().contains(lhs.toString().toUpperCase()))
                return 1;
            else
                return 2;
        }
        return 2;
    }
    /*
    public static int computeLevenshteinDistance(CharSequence lhs, CharSequence rhs) {
            int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];
            for (int i = 0; i <= lhs.length(); i++)
                distance[i][0] = i;
            for (int j = 1; j <= rhs.length(); j++)
                distance[0][j] = j;

            for (int i = 1; i <= lhs.length(); i++)
                for (int j = 1; j <= rhs.length(); j++)
                    distance[i][j] = minimum(
                            distance[i - 1][j] + 1,
                            distance[i][j - 1] + 1,
                            distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

            return distance[lhs.length()][rhs.length()];

        }

*/

}
