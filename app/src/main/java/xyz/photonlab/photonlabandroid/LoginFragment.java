package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.OnMultiClickListener;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;

    TextInputEditText r_email, r_password, r_username, l_email, l_password;
    ConstraintLayout all, register, login;
    Button l_login, l_register, r_create;
    TextView forget, back;
    View view;
    Animation fadeIn, fadeOut;
    private FragmentActivity mActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        addViewEvent();
        initTheme();
        mAuth = FirebaseAuth.getInstance();
    }

    private void addViewEvent() {
        l_register.setOnClickListener(v -> {
            login.setVisibility(View.GONE);
            register.setVisibility(View.VISIBLE);
            login.startAnimation(fadeOut);
            register.startAnimation(fadeIn);
        });

        l_login.setOnClickListener(v -> {
            String email = Objects.requireNonNull(l_email.getText()).toString().trim();
            String password = Objects.requireNonNull(l_password.getText()).toString().trim();
            if (email.equals("")) {
                l_email.setError("Email is required");
                return;
            }
            if (password.equals("")) {
                l_password.setError("Password is required");
                return;
            }
            l_login.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                Log.i(TAG, "addViewEvent: " + task);
                l_login.setEnabled(true);
                if (task.isSuccessful()) {
                    Snackbar.make(all, "Login success", Snackbar.LENGTH_SHORT).show();
                    mActivity.getSupportFragmentManager().popBackStack();
                } else {
                    Snackbar.make(all, "Login Failed:Please check your input", Snackbar.LENGTH_SHORT).show();
                }
            });
        });

        forget.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                mActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right)
                        .add(R.id.container, new fragment_explore_indiv("https://www.photonlab.xyz", "Reset password"))
                        .addToBackStack(null)
                        .commit();
            }
        });

        r_create.setOnClickListener(v -> {
            String username = Objects.requireNonNull(r_username.getText()).toString().trim();
            String email = Objects.requireNonNull(r_email.getText()).toString().trim();
            String password = Objects.requireNonNull(r_password.getText()).toString().trim();
            if (username.equals("")) {
                r_username.setError("Username is required");
                return;
            }
            if (email.equals("")) {
                r_email.setError("Email is required");
                return;
            }
            if (password.equals("")) {
                r_password.setError("Password is required");
                return;
            }
            r_create.setEnabled(false);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        r_create.setEnabled(true);
                        if (task.isSuccessful()) {
                            Snackbar.make(all, "Your account is set", Snackbar.LENGTH_SHORT).show();
                            mActivity.getSupportFragmentManager().popBackStack();
                        } else {
                            Log.w(TAG, "addViewEvent: ", task.getException());
                            Snackbar.make(all, "Unable to create account", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        });

        back.setOnClickListener(v -> {
            login.setVisibility(View.VISIBLE);
            register.setVisibility(View.GONE);
            login.startAnimation(fadeIn);
            register.startAnimation(fadeOut);
        });

        view.setOnClickListener(v -> mActivity.getSupportFragmentManager().popBackStack());

    }

    private void initView(@NonNull View contentView) {
        this.r_email = contentView.findViewById(R.id.r_in_email);
        this.r_password = contentView.findViewById(R.id.r_in_pwd);
        this.r_username = contentView.findViewById(R.id.r_in_username);
        this.register = contentView.findViewById(R.id.register_board);
        this.login = contentView.findViewById(R.id.login_board);
        this.l_login = contentView.findViewById(R.id.l_login);
        this.l_register = contentView.findViewById(R.id.l_sign_up);
        this.r_create = contentView.findViewById(R.id.r_register);
        this.forget = contentView.findViewById(R.id.l_forget);
        this.back = contentView.findViewById(R.id.r_back);
        this.l_email = contentView.findViewById(R.id.l_in_email);
        this.l_password = contentView.findViewById(R.id.l_in_password);
        this.all = contentView.findViewById(R.id.all);
        this.view = contentView.findViewById(R.id.view);

        l_password.setTransformationMethod(new PasswordTransformationMethod());

        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_scale_in);
        fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_scale_out);
    }

    private void initTheme() {
        if (Session.getInstance().isDarkMode(mActivity)) {
            all.setBackgroundTintList(ColorStateList.valueOf(Theme.Dark.MAIN_BACKGROUND));
            forget.setTextColor(Theme.Dark.SELECTED_TEXT);
            back.setTextColor(Theme.Dark.SELECTED_TEXT);
            r_email.setTextColor(Theme.Dark.SELECTED_TEXT);
            r_password.setTextColor(Theme.Dark.SELECTED_TEXT);
            r_username.setTextColor(Theme.Dark.SELECTED_TEXT);
            l_password.setTextColor(Theme.Dark.SELECTED_TEXT);
            l_email.setTextColor(Theme.Dark.SELECTED_TEXT);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

}
