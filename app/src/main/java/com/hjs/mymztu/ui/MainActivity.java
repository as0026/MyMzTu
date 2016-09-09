package com.hjs.mymztu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hjs.mymztu.R;
import com.hjs.mymztu.dao.MztuDao;
import com.hjs.mymztu.entity.MzituBean;
import com.hjs.mymztu.utils.XLog;
import com.hjs.mymztu.widget.recyclerview.DividerItemDecoration;
import com.hjs.mymztu.widget.recyclerview.MainRecyclerAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_recyclerView)
    RecyclerView mainRecyclerView;
    @Bind(R.id.search_edt)
    EditText searchEdt;
    @Bind(R.id.search_btn)
    Button searchBtn;

    private int selectType = R.id.action_lofter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.search_btn)
    public void search(View view){
        getData(selectType);
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initData((List<MzituBean>)msg.obj);
        }
    };

    private void initData(final List<MzituBean> mData){
        //设置布局管理器
//        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        mainRecyclerView.setLayoutManager(layoutManager);
        //设置adapter
        MainRecyclerAdapter mAdapter = new MainRecyclerAdapter(MainActivity.this,mData);
        mAdapter.setOnItemClickLitener(new MainRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                MzituBean bean = mData.get(position);
                Intent intent = new Intent(MainActivity.this, ImagePagerActivity.class);
                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, bean.getUrls());
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mainRecyclerView.setAdapter(mAdapter);
        //设置Item增加、移除动画
        mainRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mainRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.HORIZONTAL_LIST));
        ListView list;
        //滚动加载，不滚动时不加载,提高listview效率
        mainRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                switch (scrollState){
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        XLog.d("Main","用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        Glide.with(MainActivity.this).pauseRequests();
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        XLog.d("Main", "视图已经停止滑动");
                        Glide.with(MainActivity.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        XLog.d("Main","手指没有离开屏幕，视图正在滑动");
                        Glide.with(MainActivity.this).resumeRequests();
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu  menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_lofter) {
            selectType = R.id.action_lofter;
            getData(selectType);
            return true;
        }else if(id == R.id.action_mzitu){
            selectType = R.id.action_mzitu;
            getData(selectType);
            return true;
        }else if(id == R.id.action_meizitu){
            selectType = R.id.action_meizitu;
            getData(selectType);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getData(int type){
        String searchStr = searchEdt.getText().toString().trim();
        switch(type){
            case R.id.action_lofter:
                getLofterData(searchStr);
                break;
            case R.id.action_mzitu:
                int pageNumber = 1;
                try{
                    pageNumber = Integer.parseInt(searchStr);
                }catch(Exception e){
                    pageNumber = 1;
                }
                getMzituData(pageNumber);
                break;
            case R.id.action_meizitu:
                int pageNumber1 = 1;
                try{
                    pageNumber1 = Integer.parseInt(searchStr);
                }catch(Exception e){
                    pageNumber1 = 1;
                }
                getMeizituData(pageNumber1);
                break;
        }
    }

    private void getLofterData(final String tag){
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<MzituBean> mList = new MztuDao().getLofterDatas(tag);
                Message msg = myHandler.obtainMessage();
                msg.obj = mList;
                msg.sendToTarget();
            }
        }.start();
    }

    private void getMzituData(final int pageNumber){
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<MzituBean> mList = new MztuDao().getMzituDatas(pageNumber);
                Message msg = myHandler.obtainMessage();
                msg.obj = mList;
                msg.sendToTarget();
            }
        }.start();
    }

    private void getMeizituData(final int pageNumber){
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<MzituBean> mList = new MztuDao().getMeiZiTuDatas(pageNumber);
                Message msg = myHandler.obtainMessage();
                msg.obj = mList;
                msg.sendToTarget();
            }
        }.start();
    }

    /**
     * 按键点击回调事件,当按下回退键时显示是否退出
     */
    private long exitTime;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - exitTime > 3000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

}
