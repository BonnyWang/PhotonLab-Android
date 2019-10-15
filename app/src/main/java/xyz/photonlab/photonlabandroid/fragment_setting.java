package xyz.photonlab.photonlabandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.OnMultiClickListener;


public class fragment_setting extends Fragment implements Session.OnThemeChangeListener {

    Activity mActivity;
    TextView tv_setting;
    Button person;

    ImageView titleBack;
    CardView cards1, cards2, cards3;
    TextView[] tvs;
    View[] menuItems;


    private final int[] tvsId = new int[]{
            R.id.SettingSub_1,
            R.id.SettingSub_2,
            R.id.SettingSub_3,
            R.id.SettingSub_4,
            R.id.SettingSub_5,
            R.id.SettingSub_6,
            R.id.SettingSub_7,
            R.id.SettingSub_8,
            R.id.SettingSub_9,
            R.id.SettingSub_10,
            R.id.SettingSub_11
    };

    private final int[] itemIds = new int[]{
            R.id.set_item_Paring,
            R.id.set_item_Devices,
            R.id.set_item_System,
            R.id.set_item_Layout,
            R.id.set_item_Schedules,
            R.id.set_item_SmartHome,
            R.id.set_item_motion,
            R.id.set_item_other,
            R.id.set_item_Feedback,
            R.id.set_item_Contact,
            R.id.set_item_About,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_v2, container, false);

        titleBack = view.findViewById(R.id.title_back);
        cards1 = view.findViewById(R.id.set_group_1);
        cards2 = view.findViewById(R.id.set_group_2);
        cards3 = view.findViewById(R.id.set_group_3);

        tvs = new TextView[tvsId.length];
        menuItems = new View[itemIds.length];

        for (int i = 0; i < tvsId.length; i++) {
            tvs[i] = view.findViewById(tvsId[i]);
            menuItems[i] = view.findViewById(itemIds[i]);
            int finalI = i;
            menuItems[i].setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    fragment_setting.this.onNoteClick(finalI);
                }
            });
        }

        tv_setting = view.findViewById(R.id.Setting);
        person = view.findViewById(R.id.person);
        Session.getInstance().addOnThemeChangeListener(this);
        initTheme(Session.getInstance().isDarkMode(getContext()));
        return view;
    }

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
                //todo remove the comment remark
//                FragmentSchedule fragmentSchedule = new FragmentSchedule();
                Fragment fragmentSchedule = new fragment_Comming("Schedule");
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
                //todo remove the comment remark
//                fragment_motion_detect mfragment_motion_detect = new fragment_motion_detect();
                Fragment mfragment_motion_detect = new fragment_Comming("Motion Detect");
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
                fragment_Comming fragment_comming = new fragment_Comming("Other Utilities");
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

            cards1.setCardBackgroundColor(ColorStateList.valueOf(colors.getField("CARD_BACKGROUND").getInt(null)));
            cards2.setCardBackgroundColor(ColorStateList.valueOf(colors.getField("CARD_BACKGROUND").getInt(null)));
            cards3.setCardBackgroundColor(ColorStateList.valueOf(colors.getField("CARD_BACKGROUND").getInt(null)));

            for (TextView tv : tvs) {
                tv.setTextColor(ColorStateList.valueOf(colors.getField("TITLE").getInt(null)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

}

