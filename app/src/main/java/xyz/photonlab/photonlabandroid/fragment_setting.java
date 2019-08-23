package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;


public class fragment_setting extends Fragment implements SettingRvAdapter.OnNoteListener, Session.OnThemeChangeListener {

    Context context;
    List<setting_Content> mSettings;
    TextView tv_setting;
    Button person;
    RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        rv = view.findViewById(R.id.setRV);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);

        initializeData();

        SettingRvAdapter adapter = new SettingRvAdapter(mSettings, this);
        rv.addItemDecoration(new MyDivider(Objects.requireNonNull(getContext())));
        rv.setAdapter(adapter);
        tv_setting = view.findViewById(R.id.Setting);
        person = view.findViewById(R.id.person);
        Session.getInstance().addOnThemeChangeListener(this);
        initTheme(Session.getInstance().isDarkMode(getContext()));
        return view;
    }

    public void initializeData() {
        mSettings = new ArrayList<>();
        mSettings.add(new setting_Content("Pairing", R.drawable.setting_item_wifi));
        mSettings.add(new setting_Content("Devices", R.drawable.lightbulb));
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
                FragmentDevice fragmentDevice = new FragmentDevice();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.container, fragmentDevice).addToBackStack(null)
                        .commit();
                break;
            case 2:
                fragment_system mfragment_system = new fragment_system();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.container, mfragment_system).addToBackStack(null)
                        .commit();
                break;
            case 3:
                fragment_layout mfragment_layout = new fragment_layout();
                FragmentTransaction ft5 = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft5.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                ft5.replace(R.id.container, mfragment_layout).addToBackStack(null);
                ft5.commit();
                break;
            case 4:
                FragmentSchedule fragmentSchedule = new FragmentSchedule();
                FragmentTransaction fragmentScheduleTx = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                fragmentScheduleTx.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentScheduleTx.replace(R.id.container, fragmentSchedule).addToBackStack(null);
                fragmentScheduleTx.commit();
                break;
            case 5:
                FragmentSmartHome smartHome = new FragmentSmartHome();
                FragmentTransaction ftSh = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ftSh.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                ftSh.replace(R.id.container, smartHome).addToBackStack(null);
                ftSh.commit();
                break;
            case 6:
                fragment_motion_detect mfragment_motion_detect = new fragment_motion_detect();
                FragmentTransaction ft2 = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft2.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                ft2.replace(R.id.container, mfragment_motion_detect).addToBackStack(null);
                ft2.commit();
                break;
            case 8:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/gMS8dfWuBtd58TEM7"));
                startActivity(browserIntent);
                break;
            case 9:
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("text/plain");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@phontonlab.xyz"});
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(mailIntent, "Send Mail"));
                break;
            case 10:
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

    @Override
    public void initTheme(boolean dark) {
        Class<? extends Theme.ThemeColors> colors;
        if (dark)
            colors = Theme.Dark.class;
        else
            colors = Theme.Normal.class;
        try {
            person.setBackgroundTintList(ColorStateList.valueOf(colors.getField("TITLE").getInt(null)));
            tv_setting.setTextColor(ColorStateList.valueOf(colors.getField("TITLE").getInt(null)));
            rv.setAdapter(new SettingRvAdapter(mSettings, this));
            rv.removeItemDecoration(rv.getItemDecorationAt(0));
            rv.addItemDecoration(new MyDivider(Objects.requireNonNull(getContext())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

