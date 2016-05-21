# android-viewinjector
android view auto inject to activity, and simple to use inject view.


# get started.

# 1. create an activity .
        package com.hianzuo.viewinject.simple;
        
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;
        import android.widget.Toast;
        
        import com.hianzuo.viewinject.Injector;
        import com.hianzuo.viewinject.ViewHolder;
        import com.hianzuo.viewinject.ViewInjectClick;
        import com.hianzuo.viewinject.ViewInjectItemClick;
        
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        
        public class MainActivity extends AppCompatActivity {
            private Holder holder;
        
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                holder = Injector.inject(this, Holder.class);
                List<HashMap<String, String>> data = new ArrayList<>();
                data.add(newHashMapData("click me item 1"));
                data.add(newHashMapData("click me item 2"));
                data.add(newHashMapData("click me item 3"));
                data.add(newHashMapData("click me item 4"));
                data.add(newHashMapData("click me item 5"));
                data.add(newHashMapData("click me item 6"));
                holder.lv_list.setAdapter(new SimpleAdapter(this, data, android.R.layout.simple_list_item_1,
                        new String[]{"text1"}, new int[]{android.R.id.text1}));
            }
        
            private HashMap<String, String> newHashMapData(final String value) {
                return new HashMap<String, String>() {{
                    put("text1", value);
                }};
            }
        
            public void on_btn_test_click() {
                holder.tv_text.setText("aaaaaaaaaaaaaaaa");
            }
        
        
            public void onBtnTest2Click(View view) {
                holder.tv_text2.setText("bbbbbbbbb:" + view.getTag());
            }
        
            public void on_lv_list_item_click(int position) {
                Toast.makeText(this, "position : " + position + " is clicked.", Toast.LENGTH_SHORT).show();
            }
        
            private static class Holder implements ViewHolder {
                public TextView tv_text;
                public TextView tv_text2;
                @ViewInjectClick
                public Button btn_test;
                @ViewInjectClick
                public Button btn_test2;
                @ViewInjectItemClick
                public ListView lv_list;
            }
        }


# 2.the layout file is here.
        <?xml version="1.0" encoding="utf-8"?>
        <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:paddingBottom="@dimen/activity_vertical_margin"
            android:paddingLeft="@dimen/activity_horizontal_margin"
            android:paddingRight="@dimen/activity_horizontal_margin"
            android:paddingTop="@dimen/activity_vertical_margin">
        
            <TextView
                android:id="@+id/tv_text"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="this text will be change." />
        
            <TextView
                android:id="@+id/tv_text2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="25dp"
                android:text="this text will be change." />
        
            <Button
                android:id="@+id/btn_test"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="click me"
                android:layout_marginTop="50dp"
                />
            <Button
                android:id="@+id/btn_test2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="click me2"
                android:tag="btn_test2"
                android:layout_marginTop="100dp"
                />
            <ListView
                android:id="@+id/lv_list"
                android:layout_width="fill_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="150dp"
                >
        
            </ListView>
        </RelativeLayout>

