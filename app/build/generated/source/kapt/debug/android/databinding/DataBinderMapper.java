
package android.databinding;
import com.inexture.kotlinex.BR;
class DataBinderMapper  {
    final static int TARGET_MIN_SDK = 19;
    public DataBinderMapper() {
    }
    public android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View view, int layoutId) {
        switch(layoutId) {
                case com.inexture.kotlinex.R.layout.activity_storage_access_ex:
                    return com.inexture.kotlinex.databinding.ActivityStorageAccessExBinding.bind(view, bindingComponent);
                case com.inexture.kotlinex.R.layout.activity_coroutines_ui:
                    return com.inexture.kotlinex.databinding.ActivityCoroutinesUiBinding.bind(view, bindingComponent);
                case com.inexture.kotlinex.R.layout.activity_sealed_class:
                    return com.inexture.kotlinex.databinding.ActivitySealedClassBinding.bind(view, bindingComponent);
                case com.inexture.kotlinex.R.layout.activity_main:
                    return com.inexture.kotlinex.databinding.ActivityMainBinding.bind(view, bindingComponent);
                case com.inexture.kotlinex.R.layout.row_name:
                    return com.inexture.kotlinex.databinding.RowNameBinding.bind(view, bindingComponent);
                case com.inexture.kotlinex.R.layout.activity_calendar:
                    return com.inexture.kotlinex.databinding.ActivityCalendarBinding.bind(view, bindingComponent);
                case com.inexture.kotlinex.R.layout.activity_chart:
                    return com.inexture.kotlinex.databinding.ActivityChartBinding.bind(view, bindingComponent);
        }
        return null;
    }
    android.databinding.ViewDataBinding getDataBinder(android.databinding.DataBindingComponent bindingComponent, android.view.View[] views, int layoutId) {
        switch(layoutId) {
        }
        return null;
    }
    int getLayoutId(String tag) {
        if (tag == null) {
            return 0;
        }
        final int code = tag.hashCode();
        switch(code) {
            case -114188944: {
                if(tag.equals("layout/activity_storage_access_ex_0")) {
                    return com.inexture.kotlinex.R.layout.activity_storage_access_ex;
                }
                break;
            }
            case -1156676482: {
                if(tag.equals("layout/activity_coroutines_ui_0")) {
                    return com.inexture.kotlinex.R.layout.activity_coroutines_ui;
                }
                break;
            }
            case -1094974319: {
                if(tag.equals("layout/activity_sealed_class_0")) {
                    return com.inexture.kotlinex.R.layout.activity_sealed_class;
                }
                break;
            }
            case 423753077: {
                if(tag.equals("layout/activity_main_0")) {
                    return com.inexture.kotlinex.R.layout.activity_main;
                }
                break;
            }
            case 2061992326: {
                if(tag.equals("layout/row_name_0")) {
                    return com.inexture.kotlinex.R.layout.row_name;
                }
                break;
            }
            case 16810042: {
                if(tag.equals("layout/activity_calendar_0")) {
                    return com.inexture.kotlinex.R.layout.activity_calendar;
                }
                break;
            }
            case 159498020: {
                if(tag.equals("layout/activity_chart_0")) {
                    return com.inexture.kotlinex.R.layout.activity_chart;
                }
                break;
            }
        }
        return 0;
    }
    String convertBrIdToString(int id) {
        if (id < 0 || id >= InnerBrLookup.sKeys.length) {
            return null;
        }
        return InnerBrLookup.sKeys[id];
    }
    private static class InnerBrLookup {
        static String[] sKeys = new String[]{
            "_all"
            ,"item"};
    }
}