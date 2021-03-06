package zskj.jkxt.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zskj.jkxt.R;
import zskj.jkxt.WebService;
import zskj.jkxt.domain.AlarmData;

/**
 * Created by WYY on 2017/3/14.
 * 实时警告
 */

public class WarnFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "WarnFragment";
    private static final int NUM_PER_PAGE = 10;
    private static final int PERIOD = 1 * 60 * 1000;//1分钟刷新一次

    //view
    Context mContext;
    ListView listview;
    Button first_page, previous_page, next_page, last_page;
    private View mProgressView;
    //dataSet
    List<AlarmData> dataSet = new ArrayList<>();
    WranTask mTask;
    String ranges;
    String level;
    private int currentPage = 0;
    //format
    SimpleDateFormat sdf_date = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdf_time = new SimpleDateFormat("HHmmssSSS");
    String sdate = sdf_date.format(new Date());
    //String sdate = "20170316";
    String last_time = "0"; //取得的最后一条报警数据的时间

    MyAdapter myAdapter;


    public static WarnFragment getInstance(String ranges, String level) {
        WarnFragment warnFragment = new WarnFragment();
        Bundle b = new Bundle();
        b.putString("ranges", ranges);
        b.putString("level", level);
        warnFragment.setArguments(b);
        return warnFragment;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmeng_warn, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        polling();
    }

    private void initView() {
        first_page = (Button) getView().findViewById(R.id.first_page);
        previous_page = (Button) getView().findViewById(R.id.previous_page);
        next_page = (Button) getView().findViewById(R.id.next_page);
        last_page = (Button) getView().findViewById(R.id.last_page);
        listview = (ListView) getView().findViewById(R.id.listview);
        mProgressView = getView().findViewById(R.id.progress);
        first_page.setOnClickListener(this);
        previous_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        last_page.setOnClickListener(this);
    }

    private void initData() {
        ranges = getArguments().getString("ranges");
        level = getArguments().getString("level");
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
    }

    private void getDetail() {
        if (TextUtils.isEmpty(sdate) || TextUtils.isEmpty(last_time))
            return;
        if (mTask != null)
            return;
        mTask = new WranTask();
        mTask.execute();
    }

    @Override
    public void onClick(View view) {
        if (dataSet.size() <= 0)
            return;
        switch (view.getId()) {
            case R.id.first_page:
                if (currentPage == 0) {
                    Toast.makeText(mContext, "已是第一页", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPage = 0;
                break;
            case R.id.previous_page:
                if (currentPage <= 0) {
                    Toast.makeText(mContext, "已是第一页", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPage--;
                break;
            case R.id.next_page:
                int nextpage = currentPage + 1;
                if (dataSet.size() <= nextpage * NUM_PER_PAGE) {
                    Toast.makeText(mContext, "已是最后一页", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPage = nextpage;
                break;
            case R.id.last_page:
                int lastpage = dataSet.size() % 10 == 0 ? dataSet.size() / 10 - 1 : dataSet.size() / 10;
                if (lastpage == currentPage) {
                    Toast.makeText(mContext, "已是最后一页", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentPage = lastpage;
                break;
        }
        myAdapter.refresh();
    }

    private class WranTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (currentPage == 0)
                showProgress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().getAlarmData(sdate, last_time, ranges, level);
        }

        @Override
        protected void onPostExecute(String result) {
            mTask = null;
            showProgress(false);
            setDataDetail(result);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }

    private void setDataDetail(String result) {
        Log.e(TAG, result);
        try {
            if(result != null && result.toString() != null){
                JSONObject obj = new JSONObject(result);
                int code = obj.optInt("code");
                if (code == 0) {
                    String msg = obj.optString("msg");
//                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONArray data = obj.optJSONArray("data");
                if (data != null && data.length() > 0) {
                    dataSet.clear();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject detail = data.optJSONObject(i);
                        AlarmData alarmData = new AlarmData();
                        alarmData.alarm_num = i + "";
                        alarmData.alarm_station = detail.optString("station");
                        alarmData.alarm_type = detail.optString("type");
                        alarmData.alarm_date = detail.optString("date");
                        alarmData.alarm_time = detail.optString("time");
                        alarmData.alarm_content = detail.optString("content");
                        if (alarmData.alarm_time.compareTo(last_time) > 0) {
                            last_time = alarmData.alarm_time;
                        }
                        dataSet.add(alarmData);
                    }
                }
                currentPage = dataSet.size() % 10 == 0 ? dataSet.size() / 10 - 1 : dataSet.size() / 10;
                myAdapter.refresh();
            }else{
                Toast.makeText(mContext, "获取告警信息失败1", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "获取告警信息失败2", Toast.LENGTH_SHORT).show();
        }

    }

    //-----------------------------------------------定时操作BEGIN-------------------------------------------------------
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getDetail();
        }
    };

    Timer mTimer;

    private void polling() {
        destoryTimer();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 0, PERIOD);
    }

    private void destoryTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destoryTimer();
    }

    //------------------------------------------定时操作 END -----------------------------------------------

    class MyAdapter extends BaseAdapter {
        private int[] colors = new int[]{0x30FF0000, 0x300000FF};
        List<AlarmData> list = new ArrayList<>();

        public void refresh() {
            if (dataSet.size() <= 0)
                return;
            list = dataSet.subList(currentPage * NUM_PER_PAGE, dataSet.size() > (currentPage + 1) * NUM_PER_PAGE ? (currentPage + 1) * NUM_PER_PAGE : dataSet.size());
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public List<AlarmData> getData() {
            return list;
        }

        public int getMinNum() {
            return Integer.valueOf(list.get(0).alarm_num);
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_alarm, null);
                holder.alarm_num = (TextView) convertView.findViewById(R.id.alarm_num);
                holder.alarm_station = (TextView) convertView.findViewById(R.id.alarm_station);
                holder.alarm_type = (TextView) convertView.findViewById(R.id.alarm_type);
                holder.alarm_time = (TextView) convertView.findViewById(R.id.alarm_time);
                holder.alarm_content = (TextView) convertView.findViewById(R.id.alarm_content);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            int colorPos = position % colors.length;
            if (colorPos == 1) {
                convertView.setBackgroundColor(getResources().getColor(R.color.paleblue));
            } else {
                convertView.setBackgroundColor(getResources().getColor(R.color.pale));
            }
            final AlarmData model = list.get(position);
            holder.alarm_num.setText(model.alarm_num);
            holder.alarm_station.setText(model.alarm_station);
            holder.alarm_type.setText(model.alarm_type);
            holder.alarm_content.setText(model.alarm_content);
//            holder.alarm_time.setText(model.alarm_time);
            //TODO 乱七八糟什么东西
            SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss:SSS");
            String formatStr = null;
            String s = model.alarm_time;
            String str = "000000000";
            try {
//                formatStr = formatter2.format(formatter.parse(String.format("%1$0"+(9-s.length())+"d",0)+s));
                formatStr = formatter2.format(formatter.parse(str.substring(0, 9 - s.length()) + s));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.alarm_time.setText(formatStr);

            return convertView;
        }

    }

    static class ViewHolder {
        TextView alarm_num;
        TextView alarm_station;
        TextView alarm_type;
        TextView alarm_time;
        TextView alarm_content;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }


}
