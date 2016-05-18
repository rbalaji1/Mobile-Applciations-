package uncc.abilash.edu;


public class app {


    public static final String IPhome = "10.0.2.2";
    public static final String IPutil = "10.0.2.2";
    private int taskid;
    private String username;
    private String appliancename;
    private int power;

    public int getStartime() {
        return startime;
    }

    public int getDeadline() {
        return deadline;
    }

    private int startime;
    private int deadline;

    public void setStartime(int startime) {
        this.startime = startime;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public int getCurrentstart() {
        return currentstart;
    }

    public void setCurrentstart(int currentstart) {
        this.currentstart = currentstart;
    }

    public int getCurrentend() {
        return currentend;
    }

    public void setCurrentend(int currentend) {
        this.currentend = currentend;
    }

    public int getHsstarttime() {
        return hsstarttime;
    }

    public void setHsstarttime(int hsstarttime) {
        this.hsstarttime = hsstarttime;
    }

    private int currentstart;
    private int currentend;
    private int hsstarttime;

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    private String jobtype;
    public int getUsstarttime() {
        return usstarttime;
    }

    public void setUsstarttime(int ussatarttime) {
        this.usstarttime = ussatarttime;
    }

    private int usstarttime;
    private int runtime;
    private int jobcompleted;
    private double projectedcomp;
    private int powerconsumed;

    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppliancename() {
        return appliancename;
    }

    public void setAppliancename(String appliancename) {
        this.appliancename = appliancename;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getJobcompleted() {
        return jobcompleted;
    }

    public void setJobcompleted(int jobcompleted) {
        this.jobcompleted = jobcompleted;
    }

    public double getProjectedcomp() {
        return projectedcomp;
    }

    public void setProjectedcomp(double projectedcomp) {
        this.projectedcomp = projectedcomp;
    }

    public int getPowerconsumed() {
        return powerconsumed;
    }

    public void setPowerconsumed(int powerconsumed) {
        this.powerconsumed = powerconsumed;
    }
}
