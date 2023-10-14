package ru.javawebinar.topjava.to;

public class FilterTo implements Emptyable {
    private final String startDate;
    private final String endDate;
    private final String startTime;
    private final String endTime;

    public FilterTo(String startDate, String endDate, String startTime, String endTime) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public boolean isEmpty() {
        return (startDate == null || startDate.isEmpty())
                && (endDate == null || endDate.isEmpty())
                && (startTime == null || startTime.isEmpty())
                && (endTime == null || endTime.isEmpty());
    }
}
