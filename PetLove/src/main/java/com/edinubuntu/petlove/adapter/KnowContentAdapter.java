package com.edinubuntu.petlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.KnowContent;

import java.util.List;

/**
 * Created by edward_chiang on 13/10/9.
 */
public class KnowContentAdapter extends ArrayAdapter<KnowContent> {

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public KnowContentAdapter(Context context, int resource, List<KnowContent> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = convertView;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.know_content_list_item, null);
        }

        KnowContent knowContent = getItem(position);

        TextView textView = (TextView)contentView.findViewById(R.id.know_content_text_view);
        textView.setText(knowContent.getTitle());

        return contentView;
    }
}
