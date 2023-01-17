package workshop.mobile.herocycle.model;

public class RvCentre {
    private String FullAdd, Region, State, ctrName;

    public RvCentre(String fullAdd, String region, String state, String ctrName) {
        FullAdd = fullAdd;
        Region = region;
        State = state;
        this.ctrName = ctrName;
    }

    public String getFullAdd() {
        return FullAdd;
    }

    public String getRegion() {
        return Region;
    }

    public String getState() {
        return State;
    }

    public String getCtrName() {
        return ctrName;
    }
}
