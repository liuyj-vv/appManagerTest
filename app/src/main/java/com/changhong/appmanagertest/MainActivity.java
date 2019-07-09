package com.changhong.appmanagertest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String TAG = "rrrrrrrrrrrrrrrrrrrrr";
    GridView gridView;


    private String[] from = { "item_img", "item_name" };
    private int[] to = { R.id.item_img, R.id.item_name };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView);

        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = this.getPackageManager().queryIntentActivities(intent, 0);

        final List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i< resolveInfoList.size(); i++) {
            Map<String, Object> map = new ArrayMap<>();

            map.put(from[0], resolveInfoList.get(i).loadIcon(packageManager)); //应用的图标
            map.put(from[1], resolveInfoList.get(i).loadLabel(packageManager).toString()); //应用的名称
            map.put("packageName", resolveInfoList.get(i).activityInfo.packageName); //应用的包名
            map.put("className", resolveInfoList.get(i).activityInfo.name); //应用的类名
            list.add(map);
        }

        ItemAdapter itemAdapter = new ItemAdapter(this, list, R.layout.layout_cell_item, from, to);
        gridView.setNumColumns(5);
        gridView.setAdapter(itemAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String packageName = (String) list.get(position).get("packageName");
                String className = (String) list.get(position).get("className");

                Log.e(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()+"["+Thread.currentThread().getStackTrace()[2].getLineNumber()+"]"
                        + "packageName: " + packageName
                        + ", className: " + className);

                ComponentName componet = new ComponentName(packageName, className);
                Intent intent = new Intent();
                intent.setComponent(componet);
                startActivity(intent);
            }
        });

    }

    private class ItemAdapter extends BaseAdapter{
        Context mContext;
        List<Map<String, Object>> mMapListData = null;
        private int mLayoutResource;
        private String[] mFrom;
        private int[] mTo;

        ItemAdapter(Context context, List<Map<String, Object>> mapListData, @LayoutRes int layout, String[] from, @IdRes int[] to) {
            mContext = context;
            mMapListData = mapListData;
            mLayoutResource = layout;
            mFrom = from;
            mTo = to;
        }

        @Override
        public int getCount() {
            return (null == mMapListData) ? 0 : mMapListData.size();
        }

        @Override
        public Object getItem(int position) {
            return (null == mMapListData) ? null : mMapListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(null == convertView){
                convertView = LayoutInflater.from(mContext).inflate(mLayoutResource, null);
            }
            ImageView imageView = convertView.findViewById(mTo[0]);
            imageView.setImageDrawable((Drawable) mMapListData.get(position).get(mFrom[0]));

            TextView textView = convertView.findViewById(mTo[1]);
            textView.setText((String) mMapListData.get(position).get(mFrom[1]));

            return convertView;
        }
    }

}
