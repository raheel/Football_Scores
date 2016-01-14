package barqsoft.footballscores;

import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities
{
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;

    public static String getLeague(Context context, int league_num)
    {
        switch (league_num)
        {
            case SERIE_A : return context.getString(R.string.seriaa);
            case PREMIER_LEGAUE : return context.getString(R.string.premierleague);
            case CHAMPIONS_LEAGUE : return context.getString(R.string.champions_league);
            case PRIMERA_DIVISION : return context.getString(R.string.primeradivison);
            case BUNDESLIGA : return context.getString(R.string.bundesliga);
            default: return context.getString(R.string.unknown_league);
        }
    }
    public static String getMatchDay(Context context, int match_day,int league_num)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return context.getString(R.string.matchday_text);
            }
            else if(match_day == 7 || match_day == 8)
            {
                return context.getString(R.string.first_knockout_round);
            }
            else if(match_day == 9 || match_day == 10)
            {
                return context.getString(R.string.quarter_final);
            }
            else if(match_day == 11 || match_day == 12)
            {
                return context.getString(R.string.semi_final);
            }
            else
            {
                return context.getString(R.string.final_text);
            }
        }
        else
        {
            return context.getString(R.string.matchday_text) + " : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    //changed to if statement instead of switch because using Strings from file for comparison
    public static int getTeamCrestByTeamName(Context context, String teamname) {
        if (teamname == null) {
            return R.drawable.no_icon;
        }

        if (teamname.equals(context.getString(R.string.arsenal)))
            return R.drawable.arsenal;

        if (teamname.equals(context.getString(R.string.manchester)))
            return R.drawable.manchester_united;

        if (teamname.equals(context.getString(R.string.swansea)))
            return R.drawable.swansea_city_afc;

        if (teamname.equals(context.getString(R.string.leicester)))
            return R.drawable.leicester_city_fc_hd_logo;

        if (teamname.equals(context.getString(R.string.everton)))
            return R.drawable.everton_fc_logo1;

        if (teamname.equals(context.getString(R.string.westham)))
            return R.drawable.west_ham;

        if (teamname.equals(context.getString(R.string.tottenham)))
            return R.drawable.tottenham_hotspur;

        if (teamname.equals(context.getString(R.string.westbromwich)))
            return R.drawable.west_bromwich_albion_hd_logo;

        if (teamname.equals(context.getString(R.string.sunderland)))
            return R.drawable.sunderland;

        if (teamname.equals(context.getString(R.string.stoke)))
            return R.drawable.stoke_city;

        return R.drawable.no_icon;
    }


    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public static Cursor getAllMatchesByDate(Context context, String date){
        return context.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
                null, null, new String[]{date}, null);
    }

    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        }
        else if ( julianDay == currentJulianDay -1)
        {
            return context.getString(R.string.yesterday);
        }
        else
        {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }
}
