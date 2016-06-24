package com.smt.pulltorefresh.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.smt.pulltorefresh.MyListener;
import com.smt.pulltorefresh.PullToRefreshLayout;
import com.smt.pulltorefresh.R;

public class PullableExpandableListViewActivity extends Activity {
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandablelistview);
        ((PullToRefreshLayout) findViewById(R.id.refresh_view))
                .setOnRefreshListener(new MyListener());
        expandableListView = (ExpandableListView) findViewById(R.id.content_view);
        initExpandableListView();
    }


    private void initExpandableListView() {
        expandableListView.setAdapter(new ExpandableListAdapter(this));
        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        PullableExpandableListViewActivity.this,
                        " Click on group " + groupPosition + " item "
                                + childPosition, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        expandableListView
                .setOnItemLongClickListener(new OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view, int position, long id) {
                        Toast.makeText(
                                PullableExpandableListViewActivity.this,
                                "LongClick on "
                                        + parent.getAdapter().getItemId(
                                        position), Toast.LENGTH_SHORT)
                                .show();
                        return true;
                    }
                });
        expandableListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {

                    parent.collapseGroup(groupPosition);
                } else {

                    parent.expandGroup(groupPosition);
                }
                return true;
            }
        });
    }

    class ExpandableListAdapter extends BaseExpandableListAdapter {
        private String[] groupsStrings;
        private String[][] groupItems;
        private Context context;

        public ExpandableListAdapter(Context context) {
            this.context = context;
            groupsStrings = new String[8];
            for (int i = 0; i < groupsStrings.length; i++) {
                groupsStrings[i] = new String("group " + i);
            }
            groupItems = new String[8][8];
            for (int i = 0; i < groupItems.length; i++)
                for (int j = 0; j < groupItems[i].length; j++) {
                    groupItems[i][j] = new String("group " + i + "  item "
                            + j);
                }
        }

        @Override
        public int getGroupCount() {
            return groupsStrings.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groupItems[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupsStrings[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groupItems[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.list_item_layout, null);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText(groupsStrings[groupPosition]);
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.list_item_layout, null);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText(groupItems[groupPosition][childPosition]);
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

}
