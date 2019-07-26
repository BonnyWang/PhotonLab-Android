package xyz.photonlab.photonlabandroid;

import java.util.ArrayList;
import java.util.List;

public class dlLoadedAdapter extends dlRvAdapter {
    public dlLoadedAdapter(List<theme_Class> mdlthemes, dlListener mlistener, ArrayList<theme_Class> mthemes) {
        super(mthemes, mlistener, mthemes);
    }

    @Override
    public int getItemCount() {
        return mdlthemes.size();
    }
}
