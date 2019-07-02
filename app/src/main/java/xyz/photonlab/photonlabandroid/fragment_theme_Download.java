package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_theme_Download.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_theme_Download#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_theme_Download extends Fragment {


    static final String TAG = "fTheme_DownLoad";
    ArrayList<theme_Class> themeDownload;
    RecyclerView rv;

    RvAdapter adapter;

    RvAdapter.OnNoteListener onNoteListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme__download, container, false);

        rv = (RecyclerView)view.findViewById(R.id.rvDownloadTheme);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        onNoteListener = new RvAdapter.OnNoteListener() {
            @Override
            public void onNoteClick(int position) {

            }
        };

        themeDownload = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("mtheme");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                theme_Class value = dataSnapshot.getValue(theme_Class.class);
                themeDownload.add(value);
                adapter = new RvAdapter(themeDownload, onNoteListener);
                rv.setAdapter(adapter);
                Log.d(TAG, "Value is: " );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        return view;
    }


}
