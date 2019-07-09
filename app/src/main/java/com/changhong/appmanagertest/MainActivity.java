package com.changhong.appmanagertest;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String TAG = "rrrrrrrrrrrrrrrrrrrrr";
    GridView gridView;

    List<ApplicationInfo> applicationInfoList;
    private String[] from = { "item_img", "item_name" };
    private int[] to = { R.id.item_img, R.id.item_name };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView);

        PackageManager packageManager = this.getPackageManager();
        applicationInfoList = this.getPackageManager().getInstalledApplications(PackageManager.CERT_INPUT_RAW_X509);

        List<Map<String, Object>> list = new ArrayList<>();

        for (int i=0; i<applicationInfoList.size(); i++) {
            Map<String, Object> map = new ArrayMap<>();
            Drawable drawable = applicationInfoList.get(i).loadIcon(packageManager);
            map.put(from[0], drawable);
            map.put(from[1], applicationInfoList.get(i).loadLabel(packageManager).toString());
            list.add(map);
        }

        ItemAdapter itemAdapter = new ItemAdapter(this, list, R.layout.layout_cell_item, from, to);
        gridView.setNumColumns(5);
        gridView.setAdapter(itemAdapter);
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
