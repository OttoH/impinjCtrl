package model;

public class Record {
    private String epc;
    private long time;
    private short ant;
    private double signal;
    //private double angle;

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long timestamp) {
        this.time = timestamp;
    }

    public short getAnt() {
        return ant;
    }

    public void setAnt(short ant) {
        this.ant = ant;
    }

    public double getSignal() {
        return signal;
    }

    public void setSignal(double signal) {
        this.signal = signal;
    }

    //public double getAngle() { return angle; }

    //public void setAngle(double angle) { this.angle = angle; }
}
