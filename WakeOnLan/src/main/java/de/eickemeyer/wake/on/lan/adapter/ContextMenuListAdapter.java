package de.eickemeyer.wake.on.lan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.entities.DialogContextMenuItem;

public class ContextMenuListAdapter extends ArrayAdapter<DialogContextMenuItem> {

    private final Context context;
    private final int layoutResourceId;
    private List<DialogContextMenuItem> dialogContextMenuItems = null;

    public ContextMenuListAdapter(Context context, int layoutResourceId, List<DialogContextMenuItem> dialogContextMenuItems) {
        super(context, layoutResourceId, dialogContextMenuItems);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.dialogContextMenuItems = dialogContextMenuItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        } else {
            holder = (ListHolder) row.getTag();
        }

        DialogContextMenuItem currentItem = dialogContextMenuItems.get(position);
        holder.txtTitle.setText(currentItem.getText());
        holder.imgIcon.setImageDrawable(currentItem.getDrawable());

        return row;
    }

    private static class ListHolder {
        ImageView imgIcon;
        TextView txtTitle;
    }
}