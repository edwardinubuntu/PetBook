package com.edinubuntu.petlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.DrawerAction;

import java.util.List;

/**
 * Created by edward_chiang on 13/10/3.
 */
public class DrawerActionsAdapter extends ArrayAdapter<DrawerAction> {
    private Context context;
    private java.util.List<DrawerAction> objectList;

    public DrawerActionsAdapter(Context context, java.util.List<DrawerAction> objectList) {
        super(context, R.layout.drawer_list_item, objectList);
        this.context = context;
        this.objectList = objectList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.drawer_list_item_text_view);

        DrawerAction drawerAction = this.objectList.get(position);

        textView.setText(drawerAction.getTitle());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.drawer_list_item_image_view);
        imageView.setImageResource(drawerAction.getImageResource());

        return rowView;
    }

    public void setObjectList(List<DrawerAction> objectList) {
        this.objectList = objectList;
    }
}
