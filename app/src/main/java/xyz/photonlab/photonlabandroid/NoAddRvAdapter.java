package xyz.photonlab.photonlabandroid;

import java.util.List;

import xyz.photonlab.photonlabandroid.model.MyTheme;

public class NoAddRvAdapter extends RvAdapter {

    public NoAddRvAdapter(List<MyTheme> mthemes, OnNoteListener onNoteListener) {
        super(mthemes, onNoteListener);
    }

    @Override
    public int getItemCount() {
        return mthemes.size();
    }
}
