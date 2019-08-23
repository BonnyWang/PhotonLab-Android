package xyz.photonlab.photonlabandroid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import javax.xml.transform.sax.TemplatesHandler;

import xyz.photonlab.photonlabandroid.model.Session;
import xyz.photonlab.photonlabandroid.model.Theme;

class MyDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;

    private final Rect mBounds = new Rect();

    private Context context;

    public MyDivider(Context context) {
        this.context = context;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        drawVertical(c, parent);
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if (Session.getInstance().isDarkMode(context))
            mDivider.setTint(Theme.Dark.SELECTED_TEXT);
        else
            mDivider.setTint(Theme.Normal.SELECTED_TEXT);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            View icon = child.findViewById(R.id.title_icon);
            View title = child.findViewById(R.id.SettingSub);
            View arrow = child.findViewById(R.id.arrow);

            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - mDivider.getIntrinsicHeight();

            int[] iconLocation = new int[2];
            icon.getLocationInWindow(iconLocation);
            int[] titleLocation = new int[2];
            title.getLocationInWindow(titleLocation);
            int[] arrowLocation = new int[2];
            arrow.getLocationInWindow(arrowLocation);

            if (i == 0) {
                left = iconLocation[0];
                right = arrowLocation[0] + arrow.getMeasuredWidth();
                mDivider.setBounds(left, top - child.getMeasuredHeight() - 49, right, top - child.getMeasuredHeight() - 46);
                mDivider.draw(canvas);
            } else if (i == 3 || i == 7) {
                left = titleLocation[0];
                right = arrowLocation[0] + arrow.getMeasuredWidth();
                mDivider.setBounds(left, top - 49, right, bottom - 49);
                mDivider.draw(canvas);
            } else if (i == 10) {
                left = iconLocation[0];
                right = arrowLocation[0] + arrow.getMeasuredWidth();
                mDivider.setBounds(left, top - 49, right, bottom - 49);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        outRect.set(0, 0, 0, 0);
        int position = parent.indexOfChild(view);
        if (position == 3 || position == 7 || position == 10) {
            outRect.set(0, 0, 0, 100);
        }
    }
}
