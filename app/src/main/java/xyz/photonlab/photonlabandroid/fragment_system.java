package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class fragment_system extends FullScreenFragment {

    private static final String TAG = "fragment_system";

    Button btBack;

    ConstraintLayout clPrivacy;
    TextView tvDeviceName;


    TinyDB tinyDB;
    String ipAddr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tinyDB = new TinyDB(getContext());
        ipAddr = tinyDB.getString("LocalIp");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_system, container, false);

        btBack = view.findViewById(R.id.backButton_System);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        clPrivacy = view.findViewById(R.id.clPrivacy);
        clPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.photonlab.xyz/privacypolicy.html"));
                startActivity(browserIntent);
            }
        });

        tvDeviceName = view.findViewById(R.id.tvDeviceName);
        if(ipAddr.equals("")){
            // No change
            // No device -B
        }else{
            tvDeviceName.setText(ipAddr.trim(), TextView.BufferType.SPANNABLE);
            new JsonTask().execute("http://"+ipAddr+"/ip");
////            tvMacAddr.setText();
        }
//
        return view;
    }


    private class JsonTask extends AsyncTask<String, String, String> {

        Boolean sucess = false;

        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExecute: ");
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(5000);
                connection.connect();

                InputStream stream = connection.getInputStream();


                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line+"\n");
//                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
//
//                }
                sucess = true;
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            sucess = true;
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "onPostExecute: "+result);

            if(sucess) {
                if (result != null) {
                    String temp = result.replaceAll("\\{", "");
                    temp = temp.replaceAll("\\}", "");
                    temp = temp.replaceAll("\"", "");
                    Log.d(TAG, "onPostExecute:place " + temp);
                    String[] message = temp.split(",");
                    for (int i = 0; i < message.length; i++) {
                        Log.d(TAG, "onPostExecute: " + message[i]);
                    }

//                    try {
//                        String ipAddr = message[1].split(":")[1];
//                        Log.d(TAG, "onPostExecute: ipAddr : " + ipAddr);
//                        tinyDB.putString("LocalIp", ipAddr);
//
//                    } catch (Exception e) {
//                        Log.d(TAG, "onPostExecute: ipError");
//                        tvDeviceName.setText("Not Connected", TextView.BufferType.SPANNABLE);
//
//                    }
                } else {

                    tvDeviceName.setText("Not Connected", TextView.BufferType.SPANNABLE);
                }
            }

        }
    }
}
