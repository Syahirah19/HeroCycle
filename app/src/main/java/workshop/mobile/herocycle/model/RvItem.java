package workshop.mobile.herocycle.model;

public class RvItem {

    private String text;
    private int icon;

    public RvItem(String text, int icon) {
        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public int getIcon() {
        return icon;
    }
}
