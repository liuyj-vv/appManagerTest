
# 关键代码
```
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
```