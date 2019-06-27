package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import xyz.photonlab.photonlabandroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

//Added -Bonny


public class Themes extends AppCompatActivity {
    private TextView mTextMessage;
    private CardView mCardView;

    //added for Recycler -Bonny
    Context context;
    List<theme_Class> mtheme;

    //added for setting different color for the gradient -Bonny
    ImageView imageView_Card;

    //Fragment Test - Bonny
    Fragment fragment;
    Fragment fragment_Theme;
    Button buttontest;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //to_Main();
                    fragment_Theme = new Fragment_Theme();
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    ft1.replace(R.id.fgm_Theme, fragment_Theme);
                    ft1.commit();
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("KKK");
                    fragment = new Fragment_Control();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    ft2.replace(R.id.fgm_Theme, fragment);
                    ft2.commit();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_notifications2:
                    mTextMessage.setText(R.string.title_notifications2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        //RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        //added for recycler view -Bonny
//
//        LinearLayoutManager llm = new LinearLayoutManager(context);
//        rv.setLayoutManager(llm);
//
//        imageView_Card = (ImageView)findViewById(R.id.imageView_Card);
//
//        initializeData();
//
//        RvAdapter adapter = new RvAdapter(mtheme);
//        rv.setAdapter(adapter);


    }

    public void to_Main(){
        finish();
    }



//    private void initializeData(){
//        mtheme = new ArrayList<>();
//        mtheme.add(new theme_Class("Spring", new int[] {0xff009e00, 0xfffcee21}));
//        mtheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d}));
//        mtheme.add(new theme_Class("Sky", new int[] {0xff00b7ff, 0xff00ffee}));
//        mtheme.add(new theme_Class("Spring", new int[] {0xff009e00, 0xfffcee21}));
//        mtheme.add(new theme_Class("Fizzy Peach", new int[] {0xfff24645, 0xffebc08d}));
//        mtheme.add(new theme_Class("Sky", new int[] {0xff00b7ff, 0xff00ffee}));
//    }


}

