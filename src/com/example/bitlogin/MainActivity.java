package com.example.bitlogin;

import java.util.ArrayList;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	private ViewPager pager;
	private ArrayList<Fragment> fragmentList;  
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
	private int offset;// ͼƬ�ƶ���ƫ����
	private int[] position = { 0, 0};// tabλ��
	private ImageView image;
	private TextView[] viewlist = { null, null};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InitTextView();
		InitImage();
		InitViewPager();
		
//		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//            //͸��״̬��
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //͸��������
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		}		
	}
	
	private void InitViewPager(){
		pager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
		Fragment login = new LoginFragment();
		Fragment setting = new SettingFragment();
		fragmentList.add(login);
		fragmentList.add(setting);
		
		pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));  
        pager.setCurrentItem(0);//���õ�ǰ��ʾ��ǩҳΪ��һҳ  
        viewlist[currIndex].setTextColor(getResources().getColor(R.color.service_tab_select));
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	public void InitImage() {
		image = (ImageView) findViewById(R.id.cursor);
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		viewlist[0].measure(w, h);
		bmpW = image.getLayoutParams().width = viewlist[0].getMeasuredWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (int) ((screenW / 3.0 - bmpW) / 2);
		position[0] = (int)(offset + (screenW / 3.0));
		position[1] = (int) (screenW / 3.0 * 2.0) + offset;
		
		Animation animation = new TranslateAnimation(position[currIndex],
				position[currIndex], 0, 0);// ƽ�ƶ���
		animation.setFillAfter(true);// ������ֹʱͣ�������һ֡����Ȼ��ص�û��ִ��ǰ��״̬
		animation.setDuration(0);// ��������ʱ��0.2��
		image.startAnimation(animation);// ����ImageView����ʾ������
	}
	public void InitTextView() {
		viewlist[0] = (TextView) findViewById(R.id.loginTextView);
		viewlist[1] = (TextView) findViewById(R.id.settingTextView);
		 //��ť���onClick�¼�
		for (int i = 0; i < 2; i++) {
			viewlist[i].setOnClickListener(new txListener(i));
		}
	}

	
	public class MyOnPageChangeListener implements OnPageChangeListener{  
	    private int one = offset *2 +bmpW;//��������ҳ���ƫ����  
	      
	    @Override  
	    public void onPageScrolled(int arg0, float arg1, int arg2) {  
	        // TODO Auto-generated method stub  
	          
	    }  
	      
	    @Override  
	    public void onPageScrollStateChanged(int arg0) {  
	        // TODO Auto-generated method stub  
	          
	    }  
	      
	    @Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			/*
			 * Animation animation = new TranslateAnimation(currIndex * one,
			 * arg0 one, 0, 0);// ƽ�ƶ���
			 */
			int i = currIndex;
			Animation animation = new TranslateAnimation(position[currIndex],
					position[arg0], 0, 0);// ƽ�ƶ���
			currIndex = arg0;
			animation.setFillAfter(true);// ������ֹʱͣ�������һ֡����Ȼ��ص�û��ִ��ǰ��״̬
			animation.setDuration(200);// ��������ʱ��0.2��
			image.startAnimation(animation);// ����ImageView����ʾ������
			viewlist[i].setTextColor(getResources().getColor(
					R.color.service_tab_unselect));
			viewlist[arg0].setTextColor(getResources().getColor(
					R.color.service_tab_select));
		} 
	}  
	
	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pager.setCurrentItem(index);
		}
	}
}
