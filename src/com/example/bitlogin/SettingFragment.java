package com.example.bitlogin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IllegalFormatCodePointException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.DataSetObserver;
import android.graphics.AvoidXfermode.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingFragment extends Fragment {
	private final int FILE_SELECT_CODE = 1;
	private Button choose;
	private ListView userListView;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
		choose = (Button)rootView.findViewById(R.id.choose);
		choose.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				showFileChooser();				
			}
		});
		userListView = (ListView)rootView.findViewById(R.id.list);
		loadData();
		return rootView;
	}
	
	private void readFile(String path){
		List<String> content = new ArrayList<String>();
		try {
			FileInputStream fStream = new FileInputStream(path);
			InputStreamReader sReader = new InputStreamReader(fStream);
			BufferedReader reader = new BufferedReader(sReader);
			while (true) {
				String line = reader.readLine();
				if(line == null)
					break;
				content.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadData(){
		//SharedPreferences p = getActivity().getSharedPreferences("user", 0);
		List<String> userList = Common.getSortedUserList(getActivity());
		userListView.setAdapter(new ArrayAdapter<>(
				getActivity(), android.R.layout.simple_list_item_1, userList));
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == FILE_SELECT_CODE){
			if (resultCode == Activity.RESULT_OK) {  
	            // Get the Uri of the selected file  
	            Uri uri = data.getData();
	            String url = uri.getPath();
	            SharedPreferences preferences = getActivity().getSharedPreferences("user", 0);
	            Set<String> set = new HashSet<String>();
	            Editor editor = preferences.edit();
	            try {
					FileReader fReader = new FileReader(url);
					BufferedReader reader = new BufferedReader(fReader);
					while(true){
						String line = reader.readLine();
						if(line == null || line.length() == 0){
							break;
						}
						String[] parts = line.split(":");
						if(parts.length != 2){
							Toast.makeText(getActivity(), "文件格式有误", Toast.LENGTH_SHORT).show();
							continue;
						}
						set.add(line);
					}
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getActivity(), "打开文件错误", Toast.LENGTH_SHORT).show();
				}
	            editor.putStringSet("userSet", set);
	            editor.commit();
	            loadData();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showFileChooser(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {  
	        startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),  
	                FILE_SELECT_CODE);  
	    } catch (android.content.ActivityNotFoundException ex) {  
	        // Potentially direct the user to the Market with a Dialog  
	        Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT)  
	                .show();  
	    }
	}
	
}

