package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;


public class fragment_setting extends Fragment implements SettingRvAdapter.OnNoteListener {

    Context context;
    List<setting_Content> mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        final RecyclerView rv = view.findViewById(R.id.setRV);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        initializeData();

        SettingRvAdapter adapter = new SettingRvAdapter(mSettings, this);
        rv.addItemDecoration(new MyDivider(getContext()));
        rv.setAdapter(adapter);
        return view;
    }

    public void initializeData() {
        mSettings = new ArrayList<>();
        mSettings.add(new setting_Content("Pairing", R.drawable.setting_item_wifi));
        mSettings.add(new setting_Content("System", R.drawable.ic_settings_setting));
        mSettings.add(new setting_Content("Layout Manager", R.drawable.ic_setting_poly));
        //====================
        mSettings.add(new setting_Content("Schedules", R.drawable.setting_item_schedules));
        mSettings.add(new setting_Content("Smart Home", R.drawable.setting_item_smarthome));
        mSettings.add(new setting_Content("Motion Detect", R.drawable.ic_settings_motion));
        mSettings.add(new setting_Content("Other Utilities", R.drawable.ic_settings_tool));
        //====================
        mSettings.add(new setting_Content("Feedback", R.drawable.setting_item_feedback));
        mSettings.add(new setting_Content("Contact Support", R.drawable.setting_item_contact));
        mSettings.add(new setting_Content("About", R.drawable.setting_item_about));
    }

    @Override
    public void onNoteClick(int position) {
        switch (position) {
            case 0:
                if (Session.getInstance().isPermissionFlag()) {
                    FragmentPair fragment_pair = new FragmentPair();
                    FragmentTransaction ft0 = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    ft0.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                    ft0.replace(R.id.container, fragment_pair).addToBackStack(null);
                    ft0.commit();
                } else {
                    Session.getInstance().setPermissionFlag(true);
                    ((MainActivity) Objects.requireNonNull(getActivity())).getPermissions();
                }
                break;

            case 1:
                fragment_system mfragment_system = new fragment_system();
                FragmentTransaction ft6 = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft6.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                ft6.replace(R.id.container, mfragment_system).addToBackStack(null);
                ft6.commit();
                break;
            case 2:
                fragment_layout mfragment_layout = new fragment_layout();
                FragmentTransaction ft5 = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft5.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                ft5.replace(R.id.container, mfragment_layout).addToBackStack(null);
                ft5.commit();
                break;
            case 4:
                FragmentSmartHome smartHome = new FragmentSmartHome();
                FragmentTransaction ftSh = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ftSh.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                ftSh.replace(R.id.container, smartHome).addToBackStack(null);
                ftSh.commit();
                break;
            case 5:
                fragment_motion_detect mfragment_motion_detect = fragment_motion_detect.getInstance();
                FragmentTransaction ft2 = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft2.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                ft2.replace(R.id.container, mfragment_motion_detect).addToBackStack(null);
                ft2.commit();
                break;
            case 7:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/gMS8dfWuBtd58TEM7"));
                startActivity(browserIntent);
                break;
            case 8:
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("text/plain");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@phontonlab.xyz"});
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(mailIntent, "Send Mail"));
                break;
            case 9:
                FragmentAbout fragmentAbout = new FragmentAbout();
                FragmentTransaction txAbout = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                txAbout.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                txAbout.replace(R.id.container, fragmentAbout).addToBackStack(null);
                txAbout.commit();
                break;

            default:
                fragment_Comming fragment_comming = new fragment_Comming(mSettings.get(position).subtitle);
                FragmentTransaction ft1 = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft1.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                ft1.replace(R.id.container, fragment_comming).addToBackStack(null);
                ft1.commit();


        }

    }
}

