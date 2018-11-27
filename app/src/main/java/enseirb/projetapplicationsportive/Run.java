package enseirb.projetapplicationsportive;

import android.location.Location;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Run {
    private Map<Date, Location> path ;
    private String runner;

    public Run(Map<Date, Location> path, String runner){
        this.path = new HashMap<Date, Location>(path);
        this.runner = new String(runner);
    }

    public String getRunner(){
        return new String(runner);
    }

    public Map<Date, Location> getPath(){
        return new HashMap<Date, Location>(path);
    }
}
