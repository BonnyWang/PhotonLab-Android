package xyz.photonlab.photonlabandroid.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by KIO on 2019/8/21
 * 网络节点扫描工具，枚举IP扫描当前局域网中的所有主机及其MAC地址
 */
public class NetworkNodeScanner {

    private final String TAG = "NetworkNodeScanner";
    private final Pattern ipReg = Pattern.compile("((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)");
    private final Pattern macReg = Pattern.compile("([0-9a-fA-F]{2})(([/\\s:][0-9a-fA-F]{2}){5})");

    private String endAddress;
    private String currentAddress;
    private int nodeNum = 0;
    private volatile int overNum = 0;
    private ArrayList<String> reachableIps;

    private Map<String, String> mapToIp;


    private ExecutorService threadPool = Executors.newFixedThreadPool(5);
    private boolean hasNext = true;
    private OnSearchFinishedListener mListener;
    private OnSearchProgressChangedListener mProgressListener;
    private boolean flag = true;

    public NetworkNodeScanner(InetAddress superNet, InetAddress subMask) throws UnknownHostException {
        byte[] superNetBytes = superNet.getAddress();
        byte[] subMaskBytes = subMask.getAddress();
        byte[] startAddressB = new byte[4];
        byte[] endAddressB = new byte[4];

        for (int i = 0; i < superNetBytes.length; i++) {
            startAddressB[i] = (byte) (subMaskBytes[i] & superNetBytes[i]);//获取第一个网络地址
            endAddressB[i] = (byte) (~subMaskBytes[i] | startAddressB[i]);//获取最后一个网络地址
        }
        this.endAddress = InetAddress.getByAddress(endAddressB).getHostAddress();
        this.currentAddress = InetAddress.getByAddress(startAddressB).getHostAddress();
        reachableIps = new ArrayList<>();
        mapToIp = new HashMap<>();

        Log.i(TAG, "end address" + endAddress);
        Log.i(TAG, "current address" + currentAddress);
    }

    public void scan() {
        while (hasNext && flag) {
            nodeNum++;
            String s = currentAddress;
            threadPool.execute(() -> {
                if (!flag)
                    return;
                try {
                    Log.i(TAG, s + " 2:" + System.currentTimeMillis());
                    if (InetAddress.getByName(s).isReachable(200)) {
                        Log.e(TAG, s + ":" + InetAddress.getByName(s).getHostName());
                        reachableIps.add(s);
                    }
                    Log.i(TAG, s + " 2:" + System.currentTimeMillis());
                    synchronized (NetworkNodeScanner.this) {
                        overNum++;
                        if (this.mProgressListener != null)
                            mProgressListener.onProgressChanged(overNum * 100 / (float) nodeNum);
                        Log.i(TAG, "Progress: " + (overNum * 100 / (float) nodeNum) + "%");
                        if (overNum == nodeNum) {
                            Log.i(TAG, "ping over,overNum and nodeNum:" + overNum);
                            notify();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            nextAddress();
        }

        //start to get mac address
        new Thread(() -> {
            synchronized (NetworkNodeScanner.this) {
                try {
                    NetworkNodeScanner.this.wait();//wait for ip scan over
                    Log.i(TAG, "Now Pick Mac");
                    Log.i(TAG, "Reachable IP:" + reachableIps.size());
                    wrapIpToMacMap();

                    Log.i(TAG, "Reachable IP&MAC :" + reachableIps);
                    for (String ips : reachableIps) {
                        if (mapToIp.get(ips) != null)
                            Log.i(TAG, String.format("%30s  ||  %30s", ips, mapToIp.get(ips)));
                    }
                    mListener.onSearchFinished(mapToIp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        threadPool.shutdown();
    }

    private void wrapIpToMacMap() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcherIp = ipReg.matcher(line);
                Matcher matcherMac = macReg.matcher(line);
                if (matcherIp.find() && matcherMac.find()) {
                    String ip = matcherIp.group();
                    String mac = matcherMac.group();
                    if (mac.equals("00:00:00:00:00:00"))
                        continue;
                    mapToIp.put(ip, mac);
                    mapToIp.put(mac, ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setOnScanFinishedListener(OnSearchFinishedListener listener) {
        this.mListener = listener;
    }

    private synchronized void nextAddress() {
        if (endAddress.equals(currentAddress))
            this.hasNext = false;
        currentAddress = nextIpAddress(currentAddress);
    }

    public static String nextIpAddress(final String input) {
        final String[] tokens = input.split("\\.");
        if (tokens.length != 4)
            throw new IllegalArgumentException();
        for (int i = tokens.length - 1; i >= 0; i--) {
            final int item = Integer.parseInt(tokens[i]);
            if (item < 255) {
                tokens[i] = String.valueOf(item + 1);
                for (int j = i + 1; j < 4; j++) {
                    tokens[j] = "0";
                }
                break;
            }
        }
        return tokens[0] + '.' +
                tokens[1] + '.' +
                tokens[2] + '.' +
                tokens[3];
    }

    public void setOnSearchProgressChangedListener(OnSearchProgressChangedListener listener) {
        this.mProgressListener = listener;
    }

    public void stop() {
        this.flag = false;
    }

    public interface OnSearchFinishedListener {
        void onSearchFinished(Map<String, String> macToIp);
    }

    public interface OnSearchProgressChangedListener {
        void onProgressChanged(float progress);
    }

}
