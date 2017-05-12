package com.example.user.alumni.adapter;

/**
 * Created by User on 02-02-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.user.alumni.R;

import java.util.List;
import java.util.Map;

public class ExpandableList extends BaseExpandableListAdapter {
    private Activity context;
    private Map<String, List<String>> ParentListItems;
    private List<String> Items;

    public ExpandableList(Activity context, List<String> Items,
                          Map<String, List<String>> ParentListItems) {
        this.context = context;
        this.ParentListItems = ParentListItems;
        this.Items = Items;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return ParentListItems.get(Items.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View ListView, ViewGroup parent) {
        final String CoursesName = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (ListView == null) {
            ListView = inflater.inflate(R.layout.list_item, null);
        }

        TextView item = (TextView) ListView.findViewById(R.id.lblListItem);

        item.setText(CoursesName);
        return ListView;
    }

    public int getChildrenCount(int groupPosition) {
        return ParentListItems.get(Items.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return Items.get(groupPosition);
    }

    public int getGroupCount() {
        return Items.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View ListView, ViewGroup parent) {
        String CoursesFull = (String) getGroup(groupPosition);
        if (ListView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ListView = infalInflater.inflate(R.layout.list_group,null);
        }
        TextView item = (TextView) ListView.findViewById(R.id.lblListHeader);
        item.setText(CoursesFull);
        return ListView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}


