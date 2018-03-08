package de.mylivn.picsviewer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.arasthel.spannedgridlayoutmanager.SpanLayoutParams;
import com.arasthel.spannedgridlayoutmanager.SpanSize;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by martinyeh on 2018/3/7.
 */

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    final String LOG_TAG = "RVAdapter";
    private Context context;
    private ArrayList<ImageItem> itemsList = new ArrayList<>();
    private ArrayList<ImageItem> orderedItemsList = new ArrayList<>();
    private String mDelItemUUID;

    HashSet<Integer> firsItemPos = new HashSet<>(Arrays.asList(0,1,3,4));
    final String selectHint = "You have selected the item with identifier ";

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public GridViewItem mImageView;
        public CardView mCardView;
        public ImageButton mDeleteButton;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image);
            mCardView = itemView.findViewById(R.id.cardView);
            mDeleteButton= itemView.findViewById(R.id.deleteButton);
        }
    }


    private ArrayList<ImageItem> gridItemsReorder(ArrayList<ImageItem> itemsList){
        ArrayList<ImageItem> newOrderItemList = new ArrayList<>(itemsList);
        newOrderItemList.add(new ImageItem("0", "file:///android_asset/add-button-icon.png"));

        // put some dummy items at last row
        while(newOrderItemList.size()%3 !=0){
            newOrderItemList.add(new ImageItem("-1", ""));
        }

        //customized the order of the gridview; do swap for some rows
        for( int i=3; i<newOrderItemList.size(); i+=3){
            if( (i/3) %2 ==1 ){
                Log.i(LOG_TAG, "index:" +i);
                ImageItem imgItem = newOrderItemList.get(i);
                newOrderItemList.set(i, newOrderItemList.get(i+2));
                newOrderItemList.set(i+2, imgItem);
            }
        }

        return newOrderItemList;
    }

    public RVAdapter(Context context, ArrayList<ImageItem> itemsList){
        this.context = context;
        this.itemsList = itemsList;
        orderedItemsList = gridItemsReorder(itemsList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        Log.i(LOG_TAG, "RVAdapter onCreateViewHolder");
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_item_image, parent, false));
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    /*int rowNum = mPosition/3;
                    if(rowNum%2 ==1 && 3* (rowNum+1) <itemsList.size()){
                        //Collections.swap(itemsList, mPosition, 3* (rowNum+1));
                        itemsList.remove(mPosition);
                        itemsList.add(rowNum*3, itemsList.get(3* (rowNum+1)-1));
                        itemsList.remove(3* (rowNum+1));
                    }else{
                        itemsList.remove(mPosition);
                    }*/
                    for(int i=0; i<itemsList.size(); i++){
                        if(itemsList.get(i).uuid.equals(mDelItemUUID)){
                            itemsList.remove(i);
                        }
                    }
                    orderedItemsList = gridItemsReorder(itemsList);

                    notifyDataSetChanged();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //doNothing
                    break;
            }
        }
    };

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.i(LOG_TAG, position + " onBindViewHolder load url: "+orderedItemsList.get(position).url);

        if(position ==0 ){
            holder.itemView.setLayoutParams(new SpanLayoutParams(new SpanSize(2, 2)));
        }else{
            holder.itemView.setLayoutParams(new SpanLayoutParams(new SpanSize(1, 1)));
        }

        //dummy items
        if(orderedItemsList.get(position).uuid.equals("-1")){
            holder.itemView.setVisibility(View.INVISIBLE);
            return;
        }

        Context context = holder.itemView.getContext();

        ((ItemViewHolder)holder).mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RVAdapter.this.context, selectHint+orderedItemsList.get(position).uuid, Toast.LENGTH_SHORT).show();
            }
        });

        ((ItemViewHolder)holder).mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer msg = new StringBuffer();
                msg.append(RVAdapter.this.context.getText(R.string.warning_message));
                msg.append(orderedItemsList.get(position).uuid);
                msg.append("?");

                //mPosition = position;
                mDelItemUUID = orderedItemsList.get(position).uuid;

                AlertDialog.Builder builder = new AlertDialog.Builder(RVAdapter.this.context);
                builder.setMessage(msg)
                                .setNegativeButton("Cancel", dialogClickListener)
                                .setPositiveButton("Ok", dialogClickListener)
                                .setCancelable(false)
                                .show();

            }
        });

        Picasso.with(context).load(orderedItemsList.get(position).url).fit().into(((ItemViewHolder) holder).mImageView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, "size:" + orderedItemsList.size());
        return orderedItemsList.size();
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public void onItemDismiss(int position) {
        notifyItemRemoved(position);
    }

     @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
         if(orderedItemsList.get(fromPosition).uuid.equals("-1")||
                 orderedItemsList.get(toPosition).uuid.equals("-1")){
             return false;
         }

         Collections.swap(orderedItemsList, fromPosition, toPosition);
         notifyItemMoved(fromPosition, toPosition);
         return true;
     }

}
