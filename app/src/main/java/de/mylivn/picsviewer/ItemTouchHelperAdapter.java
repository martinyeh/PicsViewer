package de.mylivn.picsviewer;

/**
 * Created by martinyeh on 2018/3/7.
 */

interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
