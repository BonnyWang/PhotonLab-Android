package xyz.photonlab.photonlabandroid.model;

public class SmartHomeItem {
    private final String title;
    private final int color;
    private final String link;

    public SmartHomeItem(String title, int color, String link) {
        this.title = title;
        this.color = color;
        this.link = link;
    }

    public int getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
