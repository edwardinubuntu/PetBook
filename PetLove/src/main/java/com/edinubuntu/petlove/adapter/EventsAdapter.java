package com.edinubuntu.petlove.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.edinubuntu.petlove.DisplayText;
import com.edinubuntu.petlove.R;
import com.edinubuntu.petlove.object.Event;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by edward_chiang on 13/10/4.
 */
public class EventsAdapter extends ArrayAdapter<Event> {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public EventsAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = convertView;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.event_list_item, null);
        }

        Event currentEvent = getItem(position);

        TextView messageTextView = (TextView)contentView.findViewById(R.id.event_message_text_view);
        messageTextView.setText(
                DisplayText.newInstance(getContext()).getMessageText(currentEvent));

        TextView dateTextView = (TextView)contentView.findViewById(R.id.event_date_text_view);

        PrettyTime prettyTime = new PrettyTime(new Date());
        prettyTime.setLocale(Locale.TRADITIONAL_CHINESE);

        dateTextView.setText(prettyTime.format(currentEvent.getCreatedDate()));

        return contentView;
    }
}
