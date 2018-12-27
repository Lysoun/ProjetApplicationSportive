package enseirb.projetapplicationsportive;

import android.location.Location;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Run {
    private List<Location> path ;
    private String runner;
    private long runnerId;
    private Calendar calendar;

    public Run(List<Location> path, String runner){
        this(path, runner, -1);
    }

    public Run(List<Location> path, long runnerId){
        this(path, "", runnerId);
    }

    public Run(List<Location> path, String runner, long runnerId){
        this.path = new ArrayList<Location>(path);
        this.runner = new String(runner);
        this.runnerId = runnerId;
        calendar = Calendar.getInstance(Locale.FRANCE);
    }


    public String getRunner(){
        return new String(runner);
    }

    public List<Location> getPath(){
        return new ArrayList<Location>(path);
    }

    public long getRunnerId(){ return runnerId; }

    public String getRunListViewDisplay(){
        if(path.size() <= 0)
            return "";

        return getDay() + "\n"
                + getBeginningTime() + " - "
                + getEndTime();
        }

    private String getBeginningTime(){
        Location beg = path.get(path.size() - 1);

        calendar.setTime(new Date(beg.getTime()));

        return hourMinutesToString(calendar);
    }

    private String getEndTime(){
        Location end = path.get(0);

        calendar.setTime(new Date(end.getTime()));


        return hourMinutesToString(calendar);
    }

    private String hourMinutesToString(Calendar calendar){
        int minutes = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR);

        String minutesStr = Integer.toString(minutes);
        String hourStr;

        if(minutes <= 9)
            minutesStr = "0" + minutesStr;

        if(calendar.get(Calendar.AM_PM) == Calendar.PM)
            hourStr = Integer.toString(12 + hour);
        else
            hourStr = Integer.toString(hour);

        return hourStr + "h" + minutesStr;
    }

    private String getDay(){
        Location l = path.get(0);

        calendar.setTime(new Date(l.getTime()));
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1)+ "/" +
                (calendar.get(Calendar.YEAR));
    }

    @Override
    public String toString(){
        String res = "";

        for(Location location : path){
            res += "• Latitude : " + location.getLatitude() +
                    "\n   Longitude : " + location.getLongitude() +
                    "\n   Temps : " + new Date(location.getTime()).toString() +
                    "\n\n";

        }

        return res;
    }
}
