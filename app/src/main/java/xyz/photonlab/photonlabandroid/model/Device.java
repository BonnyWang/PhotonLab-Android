package xyz.photonlab.photonlabandroid.model;

/**
 * created by KIO on 2019/8/21
 */
public class Device {

    private String name;
    private String ip;
    private String mac;

    public Device(String name, String ip, String mac) {
        this.name = name;
        this.ip = ip;
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

}