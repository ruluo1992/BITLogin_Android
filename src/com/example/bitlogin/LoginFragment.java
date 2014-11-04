package com.example.bitlogin;

import java.util.List;

import android.R.integer;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LoginFragment extends Fragment {
	public  UpdateHandler handler;
	private final int UPDATE_PROCESS = 1;
	private final int UPDATE_STATUS = 2;
	private TextView username;
	private TextView password;
	private TextView status;
	private Button login;
	private SharedPreferences preferences;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragement_login, container, false);
		handler = new UpdateHandler();
		username = (TextView)rootView.findViewById(R.id.username);
		password = (TextView)rootView.findViewById(R.id.password);
		status = (TextView)rootView.findViewById(R.id.status);
		login = (Button)rootView.findViewById(R.id.login);
		preferences = getActivity().getSharedPreferences("conf", 0);
		
		//显示上次登陆成功的账号密码
		int index = preferences.getInt("index", 0);
		List<String> users = Common.getSortedUserList(getActivity());
		String[] parts = users.get(index).split(":");
		username.setText(parts[0]);
		password.setText(parts[1]);
		
		//注册按钮点击事件
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
								
			}
		});		
		return rootView;
	}
	
	private void startLogin(){
		LoginThread thread = new LoginThread(start, inList);
	}
	
	class UpdateHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			String content = (String)msg.obj;
			switch (msg.what) {
			//更新尝试进度
			case UPDATE_PROCESS:
				String[] parts = content.split(":");
				username.setText(parts[0]);
				password.setText(parts[1]);
				break;
			//更新登陆状态
			case UPDATE_STATUS:
				status.setText(content);
				if(content.startsWith("S")){
					Editor editor = preferences.edit();
					editor.putInt("index", msg.arg1);				
				}
			default:
				break;
			}
		}		
	}
	class LoginThread extends Thread{
		
		int startIndex;
		List<String> users;
		public LoginThread(int start, List<String> inList){
			startIndex = start;
			users = inList;
		}

		@Override
		public void run() {
			int i = startIndex;
			while(true){
				i = (i + 1) % users.size();
				if(i == startIndex)
					break;
				Message msg = new Message();
				msg.obj = users.get(i);
				msg.what = UPDATE_PROCESS;
				handler.sendMessage(msg);
				//登陆成功，返回成功的索引
				if(Common.connect(users.get(i)) == true){
					msg = new Message();
					msg.obj = "Successed!";
					msg.arg1 = i;
					msg.what = UPDATE_STATUS;
					handler.sendMessage(msg);
					return ;
				}
			}
			//登陆失败，发送失败消息
			Message msg = new Message();
			msg.obj = "Failed!";
			msg.what = UPDATE_STATUS;
			handler.sendMessage(msg);
		}		
	}
}
