package zskj.jkxt.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zskj.jkxt.R;
import zskj.jkxt.WebService;
import zskj.jkxt.domain.User;
import zskj.jkxt.ui.activity.AddUserActivity;
import zskj.jkxt.ui.activity.UpdateUserActivity;
import zskj.jkxt.ui.widget.UserDetailPopu;

/**
 * Created by WYY on 2017/3/14.
 */

public class UserManagementFragment extends Fragment {

    Context mContext;
    ImageView newUser;
    ListView lv_user_list;
    private View mProgressView;
    //data
    List<User> userList = new ArrayList<>();
    UserManagementTask mTask;
    DeleteUserTask mDeleteUserTask;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_management, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        getUserData();
    }

    private void initView() {
        newUser = (ImageView) getView().findViewById(R.id.addUser);
        lv_user_list = (ListView) getView().findViewById(R.id.lv_user_list);
        mProgressView = getView().findViewById(R.id.progress);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddUserActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    private void getUserData() {
        if (mTask != null)
            return;
        mTask = new UserManagementTask();
        mTask.execute();
    }

    private class UserManagementTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().getUserInfo();
        }

        @Override
        protected void onPostExecute(String result) {
            mTask = null;
            showProgress(false);
            dealResult(result);
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }

    private void dealResult(String result) {
        try {
            if(result != null && result.toString() != null){
                JSONObject obj = new JSONObject(result);
                int code = obj.optInt("code");
                if (code == 0) {
                    String msg = obj.optString("msg");
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                //JSONObject data = obj.optJSONObject("data");
                //JSONArray userLists = data.optJSONArray("userlist");
                JSONArray userLists = obj.optJSONArray("data");
                if (userLists != null && userLists.length() > 0) {
                    userList.clear();
                    for (int i = 0; i < userLists.length(); i++) {
                        JSONObject detail = userLists.optJSONObject(i);
                        User user = new User();
                        user.userId = i;
                        user.userName = detail.optString("userName");
                        user.userPassword = detail.optString("userPassword");
                        user.userRights = detail.optString("userRights");
                        user.userRange = detail.optString("userRange");
                        user.userLevel = detail.optString("userLevel");
                        userList.add(user);
                    }
                }
                if (userList != null && userList.size() > 0) {
                    lv_user_list.setAdapter(new userListAdapter());
                    lv_user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            User user = userList.get(position);
                            showDetailPopu(user);
                        }
                    });
                }
            }else{
                Toast.makeText(mContext, "获取用户信息失败1", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "获取用户信息失败2", Toast.LENGTH_SHORT).show();
        }

    }

    UserDetailPopu mPopu;

    private void showDetailPopu(User user) {
        if (mPopu == null)
            mPopu = new UserDetailPopu(mContext);

        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        mPopu.setWidth((int) (d.getWidth() * 0.8));

        backgroundAlpha(0.5f);
        mPopu.setUser(user);
        mPopu.showAtLocation(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), Gravity.CENTER_VERTICAL, 0, 0);

        mPopu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    //设置背景透明度，1f为不透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    class userListAdapter extends BaseAdapter {
        private int[] colors = new int[]{0x30FF0000, 0x300000FF};

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
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
                convertView = View.inflate(mContext, R.layout.item_user, null);
                holder.userId = (TextView) convertView.findViewById(R.id.userId);
                holder.userName = (TextView) convertView.findViewById(R.id.userName);
                holder.userRights = (TextView) convertView.findViewById(R.id.userRights);
                holder.userRange = (TextView) convertView.findViewById(R.id.userRange);
                holder.userLevel = (TextView) convertView.findViewById(R.id.userLevel);
                holder.updateUser = (ImageView) convertView.findViewById(R.id.updateUser);
                holder.deleteUser = (ImageView) convertView.findViewById(R.id.deleteUser);
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

            final User model = userList.get(position);
            holder.userId.setText(String.valueOf(model.userId));
            holder.userName.setText(model.userName);
            holder.userRights.setText(model.userRights);
            holder.userRange.setText(model.userRange);
            holder.userLevel.setText(model.userLevel);
            holder.updateUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, UpdateUserActivity.class);
                    intent.putExtra("user", model);
                    startActivityForResult(intent, 1);
                }
            });

            holder.deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(mContext);
                    normalDialog.setTitle("提示");
                    normalDialog.setMessage("确认删除？");
                    normalDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteUser(model.userName);
                        }
                    });
                    normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
                        }
                    });
                    normalDialog.show();
                }
            });
            return convertView;
        }

    }

    static class ViewHolder {
        TextView userId;
        TextView userName;
        TextView userRights;
        TextView userRange;
        TextView userLevel;
        ImageView updateUser;
        ImageView deleteUser;
    }

    private void deleteUser(String userName) {
        if (TextUtils.isEmpty(userName))
            return;
        if (mDeleteUserTask != null) {//不为null 说明操作正在进行，规避多次点击登录按钮操作
            Toast.makeText(mContext, "删除数据中，请稍候...", Toast.LENGTH_SHORT).show();
            return;
        }
        mDeleteUserTask = new DeleteUserTask(userName);
        mDeleteUserTask.execute();
    }

    private class DeleteUserTask extends AsyncTask<Void, Void, String> {

        String userName = "";

        DeleteUserTask(String userName) {
            this.userName = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return WebService.getInstance().deleteUser(userName);
        }

        @Override
        protected void onPostExecute(String result) {
            mDeleteUserTask = null;
            showProgress(false);
            deleteResult(result);
        }

        @Override
        protected void onCancelled() {
            mDeleteUserTask = null;
            showProgress(false);
        }

    }

    public void deleteResult(String result) {
        try {
            if(result != null && result.toString() != null){
                JSONObject obj = new JSONObject(result);
                int code = obj.optInt("code");
                if (code == 0) {
                    String msg = obj.optString("msg");
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (obj.optString("data").equals("true")) {
                    getUserData();
                } else {
                    Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(mContext, "删除用户信息失败1", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "删除用户信息失败2", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
//            String result = dataSet.getStringExtra("result");
//            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
            getUserData();
        } else {
            Toast.makeText(mContext, "无返回值", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
