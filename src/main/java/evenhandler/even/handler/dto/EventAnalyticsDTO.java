package evenhandler.even.handler.dto;

public class EventAnalyticsDTO {
    private int totalAttendees;
    private double capacityUtilization;

    public int getTotalAttendees() {
        return totalAttendees;
    }

    public void setTotalAttendees(int totalAttendees) {
        this.totalAttendees = totalAttendees;
    }

    public double getCapacityUtilization() {
        return capacityUtilization;
    }

    public void setCapacityUtilization(double capacityUtilization) {
        this.capacityUtilization = capacityUtilization;
    }

}
