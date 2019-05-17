package freedom.com.freedom_e_learning.model;

public class ChartData {

    private String date;
    private String percent;

    public ChartData() {
    }

    public ChartData(String date, String percent) {
        this.date = date;
        this.percent = percent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
