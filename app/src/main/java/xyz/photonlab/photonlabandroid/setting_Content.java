package xyz.photonlab.photonlabandroid;

public class setting_Content {
    String subtitle;
    int iconRes;

    public setting_Content(String subtitle,int iconRes){
        this.subtitle = subtitle;
        this.iconRes = iconRes;
    }

    public String getSubtitle(){
        return subtitle;
    }

    public int getIconRes() {
        return iconRes;
    }
}
