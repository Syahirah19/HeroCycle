package workshop.mobile.herocycle.model;

public class RvHistory {
    private String reqDate, colDate, collPrice;

    public RvHistory(String reqDate, String colDate, String collPrice) {
        this.reqDate = reqDate;
        this.colDate = colDate;
        this.collPrice = collPrice;
    }

    public String getReqDate() {
        return reqDate;
    }

    public String getColDate() {
        return colDate;
    }

    public String getCollPrice() {
        return collPrice;
    }
}
