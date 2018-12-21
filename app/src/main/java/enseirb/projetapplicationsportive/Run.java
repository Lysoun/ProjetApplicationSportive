package enseirb.projetapplicationsportive;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Run {
    private List<Location> path ;
    private String runner;
    private long runnerId;

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
    }


    public String getRunner(){
        return new String(runner);
    }

    public List<Location> getPath(){
        return new ArrayList<Location>(path);
    }

    public long getRunnerId(){ return runnerId; }
}
