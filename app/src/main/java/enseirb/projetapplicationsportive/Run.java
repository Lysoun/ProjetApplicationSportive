package enseirb.projetapplicationsportive;

import android.location.Location;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Run {
    private long id;
    private List<Location> path ;
    private String runner;
    private long runnerId;
    private Calendar calendar;
    private float meanSpeed;
    private float maxSpeed;
    private double totalDistance;

    public Run(List<Location> path, String runner){
        this(path, runner, -1, -1);
    }

    public Run(List<Location> path, long runnerId){
        this(path, "", runnerId, -1);
    }

    public Run(List<Location> path, String runner, long runnerId, long id){
        this.path = new ArrayList<Location>(path);
        this.runner = new String(runner);
        this.runnerId = runnerId;
        this.id = id;
        calendar = Calendar.getInstance(Locale.FRANCE);

        meanSpeed = (float) 0.0;
        maxSpeed = (float) 0.0;
        totalDistance = 0.0;

        for(int i = 1; i < path.size(); i++){
            float distance = (path.get(i-1).distanceTo(path.get(i)));
            totalDistance += distance;
            float time = path.get(i - 1).getTime() - path.get(i).getTime();
            float speed = 3600 * distance / time;

            if(speed > maxSpeed)
                maxSpeed = speed;

            meanSpeed += speed;
        }

        meanSpeed /= (path.size() - 1);
    }


    public String getRunner(){
        return new String(runner);
    }

    public List<Location> getPath(){
        return new ArrayList<Location>(path);
    }

    public long getRunnerId(){ return runnerId; }

    public long getId(){return id;}

    public String getStats() {
        String deltaTime = "";
        int t = (int) ((path.get(0).getTime() - path.get(path.size()-1).getTime()) / 60000) + 1;
        if(t >= 60)
            deltaTime += t / 60 + " h " + t % 60 + " min";
        else
            deltaTime += t + " min";

        String dist = "";
        float d = (int) totalDistance;
        if (d >= 1000)
            dist += d / 1000 + " km";
        else
            dist += (int) d + " m";

        return "Temps de course : " + deltaTime
                + "\nDistance totale parcourue : " + dist
                + "\nVitesse moyenne : " + meanSpeed + " km/h"
                + "\nVitesse maximale : " + maxSpeed + " km/h";
    }

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
