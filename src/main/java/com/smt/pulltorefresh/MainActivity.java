package com.smt.pulltorefresh;

import java.util.ArrayList;
import java.util.List;
import com.smt.pulltorefresh.activity.PullableExpandableListViewActivity;
import com.smt.pulltorefresh.activity.PullableGridViewActivity;
import com.smt.pulltorefresh.activity.PullableImageViewActivity;
import com.smt.pulltorefresh.activity.PullableListViewActivity;
import com.smt.pulltorefresh.activity.PullableScrollViewActivity;
import com.smt.pulltorefresh.activity.PullableTextViewActivity;
import com.smt.pulltorefresh.activity.PullableWebViewActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * @author smt
 *
 * https://github.com/songmeitao
 */
public class MainActivity extends Activity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((PullToRefreshLayout) findViewById(R.id.refresh_view))
                .setOnRefreshListener(new MyListener());
        listView = (ListView) findViewById(R.id.content_view);
        initListView();
    }


    private void initListView() {
        List<String> items = new ArrayList<String>();
        items.add("ListView");
        items.add("GridView");
        items.add("ExpandableListView");
        items.add("SrcollView");
        items.add("WebView");
        items.add("ImageView");
        items.add("TextView");
        items.add("https://github.com/songmeitao");
        items.add("https://github.com/songmeitao");
        items.add("https://github.com/songmeitao");
        items.add("https://github.com/songmeitao");


        MyAdapter adapter = new MyAdapter(this, items);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(
                        MainActivity.this,
                        " LongClick on "
                                + parent.getAdapter().getItemId(position),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent it = new Intent();
                switch (position) {
                    case 0:
                        it.setClass(MainActivity.this,
                                PullableListViewActivity.class);
                        break;
                    case 1:
                        it.setClass(MainActivity.this,
                                PullableGridViewActivity.class);
                        break;
                    case 2:
                        it.setClass(MainActivity.this,
                                PullableExpandableListViewActivity.class);
                        break;
                    case 3:
                        it.setClass(MainActivity.this,
                                PullableScrollViewActivity.class);
                        break;
                    case 4:
                        it.setClass(MainActivity.this,
                                PullableWebViewActivity.class);
                        break;
                    case 5:
                        it.setClass(MainActivity.this,
                                PullableImageViewActivity.class);
                        break;
                    case 6:
                        it.setClass(MainActivity.this,
                                PullableTextViewActivity.class);
                        break;
                    case 7:

                    case 8:

                    case 9:

                    case 10:
                        it.setClass(MainActivity.this,
                                PullableWebViewActivity.class);
                        break;

                    default:
                        break;
                }
                startActivity(it);
            }
        });
    }
}
