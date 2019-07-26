package xyz.photonlab.photonlabandroid;

import java.util.List;

public class NoAddRvAdapter extends RvAdapter {

    public NoAddRvAdapter(List<theme_Class> mthemes, OnNoteListener onNoteListener) {
        super(mthemes, onNoteListener);
    }

    @Override
    public int getItemCount() {
        return mthemes.size();
    }
}
