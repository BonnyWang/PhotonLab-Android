package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;
import xyz.photonlab.photonlabandroid.utils.OnMultiClickListener;
import xyz.photonlab.photonlabandroid.utils.ViewGroupDisableHelper;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;

    ImageView logo;

    TextInputEditText r_email, r_password, r_username, l_email, l_password;
    TextInputLayout r_p_email, r_p_password, r_p_username, l_p_email, l_p_password;
    ConstraintLayout all, register, login;
    Button l_login, l_register, r_create;
    TextView forget, back;
    View view;
    Animation fadeIn, fadeOut;
    ProgressBar loading;
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
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
            l_email.clearFocus();
            l_password.clearFocus();
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
            ViewGroupDisableHelper.disableViewGroup(all);
            loading.setVisibility(View.VISIBLE);
            view.setEnabled(true);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                Log.i(TAG, "addViewEvent: " + task);
                ViewGroupDisableHelper.enableViewGroup(all);
                loading.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    showSnack(0xff88cc88, "Login Success");
                    mActivity.getSupportFragmentManager().popBackStack();
                } else {
                    String msg = "Unknown Error!";
                    try {
                        msg = Objects.requireNonNull(task.getException()).getMessage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    showSnack(0xffff6666, "Login Failed:" + msg);
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
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
            r_username.clearFocus();
            r_password.clearFocus();
            r_email.clearFocus();
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
            ViewGroupDisableHelper.disableViewGroup(all);
            loading.setVisibility(View.VISIBLE);
            view.setEnabled(true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        ViewGroupDisableHelper.enableViewGroup(all);
                        loading.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            showSnack(0xff88cc88, "Your account is set");
                            mActivity.getSupportFragmentManager().popBackStack();
                        } else {
                            Log.w(TAG, "addViewEvent: ", task.getException());
                            String msg = "Unknown Error!";
                            try {
                                msg = Objects.requireNonNull(task.getException()).getMessage();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            showSnack(0xffff6666, "Create Failed:" + msg);
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

    private void showSnack(@ColorInt int color, String tip) {
        try {
            Snackbar snackbar = Snackbar.make(all, tip, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(color);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        this.logo = contentView.findViewById(R.id.imageView3);
        this.r_p_email = contentView.findViewById(R.id.r_e_mail);
        this.r_p_username = contentView.findViewById(R.id.r_usr_name);
        this.r_p_password = contentView.findViewById(R.id.r_pwd);
        this.l_p_email = contentView.findViewById(R.id.l_email);
        this.l_p_password = contentView.findViewById(R.id.l_password);
        this.loading = contentView.findViewById(R.id.loading_l);

        l_password.setTransformationMethod(new PasswordTransformationMethod());

        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_scale_in);
        fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_scale_out);
    }

    private void initTheme() {
        if (Session.getInstance().isDarkMode(mActivity)) {
            ColorStateList defaultHint = new ColorStateList(
                    new int[][]{{android.R.attr.state_focused}, {}},
                    new int[]{Theme.Dark.SELECTED_TEXT, Theme.Dark.UNSELECTED_TEXT});
            all.setBackgroundTintList(ColorStateList.valueOf(Theme.Dark.MAIN_BACKGROUND));
            forget.setTextColor(0xffcccccc);
            back.setTextColor(0xffcccccc);
            r_email.setTextColor(Theme.Dark.SELECTED_TEXT);
            r_p_email.setDefaultHintTextColor(defaultHint);
            setDefaultBoxStrokeColor(r_p_email);
            r_p_email.setBoxStrokeColor(Theme.Dark.SELECTED_TEXT);

            r_password.setTextColor(Theme.Dark.SELECTED_TEXT);
            r_p_password.setDefaultHintTextColor(defaultHint);
            setDefaultBoxStrokeColor(r_p_password);
            r_p_password.setBoxStrokeColor(Theme.Dark.SELECTED_TEXT);

            r_username.setTextColor(Theme.Dark.SELECTED_TEXT);
            r_p_username.setDefaultHintTextColor(defaultHint);
            setDefaultBoxStrokeColor(r_p_username);
            r_p_username.setBoxStrokeColor(Theme.Dark.SELECTED_TEXT);

            l_password.setTextColor(Theme.Dark.SELECTED_TEXT);
            l_p_password.setDefaultHintTextColor(defaultHint);
            l_password.setBackgroundTintList(defaultHint);

            l_email.setTextColor(Theme.Dark.SELECTED_TEXT);
            l_p_email.setDefaultHintTextColor(defaultHint);
            l_email.setBackgroundTintList(defaultHint);
            l_email.setHighlightColor(Color.WHITE);

            logo.setImageResource(R.drawable.photonlab_light);
        }
    }

    private void setDefaultBoxStrokeColor(TextInputLayout target) {
        try {
            Field filed = target.getClass().getDeclaredField("defaultStrokeColor");
            filed.setAccessible(true);
            filed.set(target, Theme.Dark.UNSELECTED_TEXT);
            Method method = target.getClass().getDeclaredMethod("updateTextInputBoxState");
            method.setAccessible(true);
            method.invoke(target);
        } catch (Exception e) {
            e.printStackTrace();
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
