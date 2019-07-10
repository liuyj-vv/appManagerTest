
# manifest.xml文件说明
android.intent.action.MAIN	//程序入口

android.intent.category.LAUNCHER	//显示到界面上

android.intent.category.DEFAULT	//默认启动（startActivity启动时intent未带参数启动的activity）

注意：
1. 仅仅两个配置同时存在一个activity中时才有效–才会显示到界面上。
2. 一个apk中存在多个同时存在的配置，在界面上显示多个图标。


# 关键代码
```java
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
```

# 启动方法二
仅仅需要包名的启动方式，这种方式存在一个问题：
    当xml文件中存在两个或两个以上的activity同时配置了程序入口（android.intent.action.MAIN）和显示到界面上(android.intent.category.LAUNCHER)。那么这个app将会显示两个图标在界面上，**当时两个图标都仅仅只能打开第一个activity**。
```java
    Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
    startActivity(intent);
```