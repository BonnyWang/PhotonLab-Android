package xyz.photonlab.photonlabandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import xyz.photonlab.photonlabandroid.model.Device;
import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

public class FragmentDevice extends Fragment implements DeviceAdapter.OnItemClickListener {

    private ArrayList<Device> devices;

    private Button bt_exit;
    private RecyclerView rv_devices;
    private DeviceAdapter rv_devices_adapter;

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
        if (!Session.getInstance().getMac(getContext()).equals(""))
            devices.add(new Device("Default Device", Session.getInstance().getLocalIP(getContext()), new TinyDB(getContext()).getString("lightMac")));
        this.rv_devices_adapter = new DeviceAdapter(devices, this);
        rv_devices.setAdapter(rv_devices_adapter);
    }

    private void initView(View contentView) {
        bt_exit = contentView.findViewById(R.id.exit);
        rv_devices = contentView.findViewById(R.id.rv_devices);
        rv_devices.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void addViewEvent() {
        bt_exit.setOnClickListener(v -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
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

    private static final int VIEW_TYPE_NORMAL = 0;
    private static final int VIEW_TYPE_NO_DEVICE = 1;

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
        View itemView;
        if (viewType == VIEW_TYPE_NO_DEVICE)
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_adpter_no_device_item, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_device_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position != 0 || devices.size() != 0) {
            holder.deviceTitle.setText(devices.get(position).getName());
            if (darkMode)
                holder.deviceTitle.setTextColor(Theme.Dark.SELECTED_TEXT);
            else
                holder.deviceTitle.setTextColor(Theme.Normal.SELECTED_TEXT);
            holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
        } else {
            holder.deviceTitle.setText("No Device");
            if (darkMode)
                holder.deviceTitle.setTextColor(Theme.Dark.UNSELECTED_TEXT);
            else
                holder.deviceTitle.setTextColor(Theme.Normal.UNSELECTED_TEXT);
        }
    }

    @Override
    public int getItemCount() {
        if (devices.size() == 0)
            return 1;
        return devices.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && devices.size() == 0)
            return VIEW_TYPE_NO_DEVICE;
        return VIEW_TYPE_NORMAL;
    }

    public void enableDarkMode() {
        this.darkMode = true;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
