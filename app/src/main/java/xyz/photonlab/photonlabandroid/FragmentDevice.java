package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Device;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

public class FragmentDevice extends NormalStatusBarFragment implements DeviceAdapter.OnItemClickListener {

    private ArrayList<Device> devices;

    private ImageButton bt_exit;
    private RecyclerView rv_devices;
    private DeviceAdapter rv_devices_adapter;
    private TextView tv_go_pair;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
        addViewEvent();
        initTheme();
    }

    private void initData() {
        devices = new ArrayList<>();
        this.rv_devices_adapter = new DeviceAdapter(devices, this);
        if (!Session.getInstance().getMac(getContext()).equals("")) {
            String ip = Session.getInstance().getLocalIP(getContext());
            if (ip.equals(""))
                ip = "Reset";

            devices.add(new Device("Device01", ip,
                    new TinyDB(getContext()).getString("lightMac")));
            rv_devices.setAdapter(rv_devices_adapter);
        } else {
            rv_devices.setVisibility(View.GONE);
        }

    }

    private void initView(View contentView) {
        bt_exit = contentView.findViewById(R.id.exit);
        rv_devices = contentView.findViewById(R.id.rv_devices);
        rv_devices.setLayoutManager(new LinearLayoutManager(getContext()));
        tv_go_pair = contentView.findViewById(R.id.tvGotoSetup);
    }

    private void addViewEvent() {
        bt_exit.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
        tv_go_pair.setOnClickListener(v -> {
            if (Session.getInstance().isPermissionFlag()) {
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction tx = fragmentManager.beginTransaction();
                tx.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                tx.replace(R.id.container, new FragmentPair()).addToBackStack(null);
                tx.commit();
            } else {
                Session.getInstance().setPermissionFlag(true);
                ((MainActivity) Objects.requireNonNull(getActivity())).getPermissions();
            }
        });
    }

    private void initTheme() {
        if (Session.getInstance().isDarkMode(getContext())) {
            Objects.requireNonNull(getView()).setBackgroundColor(Theme.Dark.MAIN_BACKGROUND);
            rv_devices_adapter.enableDarkMode();
            ((TextView) getView().findViewById(R.id.tvDevice)).setTextColor(Theme.Dark.SELECTED_TEXT);

        }
    }

    @Override
    public void onItemClick(int position) {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container, new FragmentDeviceDetail(devices.get(position))).addToBackStack(null)
                .commit();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView deviceTitle;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        deviceTitle = itemView.findViewById(R.id.text1);
    }
}

class DeviceAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<Device> devices;
    private OnItemClickListener listener;
    private boolean darkMode = false;

    public DeviceAdapter(@NonNull ArrayList<Device> devices, @NonNull OnItemClickListener listener) {
        this.devices = devices;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_device_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.deviceTitle.setText(devices.get(position).getName());
        if (darkMode)
            holder.deviceTitle.setTextColor(Theme.Dark.SELECTED_TEXT);
        else
            holder.deviceTitle.setTextColor(Theme.Normal.SELECTED_TEXT);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        if (devices.size() == 0)
            return 1;
        return devices.size();
    }

    public void enableDarkMode() {
        this.darkMode = true;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
