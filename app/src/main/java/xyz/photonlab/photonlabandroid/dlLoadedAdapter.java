package xyz.photonlab.photonlabandroid;

import java.util.ArrayList;
import java.util.List;

import xyz.photonlab.photonlabandroid.model.MyTheme;

public class dlLoadedAdapter extends dlRvAdapter {
    public dlLoadedAdapter(List<MyTheme> mdlthemes, dlListener mlistener, ArrayList<MyTheme> mthemes) {
        super(mdlthemes, mlistener, mthemes);
    }

    @Override
    public int getItemCount() {
        return mdlthemes.size();
    }
}
