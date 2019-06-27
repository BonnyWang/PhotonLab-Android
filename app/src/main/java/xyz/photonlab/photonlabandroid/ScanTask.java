package xyz.photonlab.photonlabandroid;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ScanTask extends AsyncTask<Void, String, Void> {

    ArrayList<InetAddress> inetAddresses;


    ArrayList<String> canonicalHostNames;

    public ScanTask() {

    }

    @Override
    protected void onPostExecute(Void aVoid) {

        for(int i = 0; i < inetAddresses.size(); i++){
            Log.d("Search", "onPostExecute: "+ canonicalHostNames.get(i));
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        scanInetAddresses();
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {

    }

    private void scanInetAddresses(){
        //May be you have to adjust the timeout
        final int timeout = 500;

        if(inetAddresses == null){
            inetAddresses = new ArrayList<>();
        }
        inetAddresses.clear();

        if(canonicalHostNames == null){
            canonicalHostNames = new ArrayList<>();
        }
        canonicalHostNames.clear();

        //For demonstration, scan 192.168.1.xxx only
        byte[] ip = {(byte) 192, (byte) 168, (byte) 50, 0};
        for (int j = 0; j < 50; j++) {
            Log.d("Yo", "scanInetAddresses: "+ j);
            ip[3] = (byte) j;
            try {
                InetAddress checkAddress = InetAddress.getByAddress(ip);
                publishProgress(checkAddress.getCanonicalHostName());
                if (checkAddress.isReachable(timeout)) {
                    inetAddresses.add(checkAddress);
                    canonicalHostNames.add(checkAddress.getCanonicalHostName());
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                publishProgress(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress(e.getMessage());
            }
        }
    }
}
