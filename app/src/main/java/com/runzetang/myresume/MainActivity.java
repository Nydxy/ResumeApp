package com.runzetang.myresume;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import java.util.ArrayList;
import layout.fragment_contact;
import layout.fragment_home;
import layout.fragment_media;
import layout.fragment_works;
import static android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;

/**
 * MainActicity
 * 为了使用4个自定义的fragment，必须实现每个fragment的OnFragmentInteractionListener接口
 */
public class MainActivity extends AppCompatActivity
        implements fragment_home.OnFragmentInteractionListener,
        fragment_works.OnFragmentInteractionListener,
        fragment_media.OnFragmentInteractionListener,
        fragment_contact.OnFragmentInteractionListener
{

    private ViewPager mViewPager;  //ViewPager管理器
    private BottomNavigationView bottomNavigationView;  //底部导航栏
    ArrayList<Fragment> fragments = new ArrayList<Fragment>();  //管理4个fragment的容器
    private MediaPlayer mediaPlayer;
    private FloatingActionButton fab;
    SensorManagerHelper sensorHelper;
    private boolean NetworkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab=(FloatingActionButton)findViewById(R.id.btnMusicPlay);
        sensorHelper = new SensorManagerHelper(this);
        //初始化ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InitViewPages();
        InitBottomNavigationView();
        InitEventListener();

        //播放音乐
        //mediaPlayer=new MediaPlayer();
        mediaPlayer=MediaPlayer.create(this,R.raw.background);
        //mediaPlayer=MediaPlayer.create(this,Uri.parse("http://www.nydxy.me/background.mp3"));
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //检查网络连接
        NetworkStatus=isNetworkAvailable();
        if (!NetworkStatus) Toast.makeText(getApplicationContext(),"网络未连接！将无法播放视频！",Toast.LENGTH_LONG).show();
    }


    /**
     * 按下播放键
     * @param v
     */
    public void VideoPlayOnClick(View v)
    {
        if (!NetworkStatus)
        {
            Toast.makeText(getApplicationContext(),"网络未连接！无法播放视频！",Toast.LENGTH_LONG).show();
            return;
        }
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            fab.setImageResource(R.drawable.play);
        }

        VideoView mVideoView = (VideoView) findViewById(R.id.videoView);

        //添加播放控制条,还是自定义好点
        mVideoView.setMediaController(new MediaController(mVideoView.getContext()));
        //设置视频源播放res/raw中的文件;
        //Uri rawUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.xixihaha);
        //mVv.setVideoURI(rawUri);

        // 播放在线视频
        Uri mVideoUri = Uri.parse(getString(R.string.videoUrl));
        mVideoView.setVideoPath(mVideoUri.toString());
        mVideoView.start();
        Toast.makeText(getApplicationContext(),"正在加载在线视频，请稍候约10秒！",Toast.LENGTH_LONG).show();
    }


    //初始化ViewPages
    private void InitViewPages()
    {
        //将4个fragment添加到fragments
        fragments.add(new fragment_home());
        fragments.add(new fragment_works());
        fragments.add(new fragment_media());
        fragments.add(new fragment_contact());

        //ViewPager
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //滑动屏幕切换时的BottomNavigationView联动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageSelected(int position)
            {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    //初始化BottomNavigationView
    private void InitBottomNavigationView()
    {
        //BottomNavigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);  //使4个标签以上的BottomNavigationView能够正常显示
        //点击BottomNavigationView的切换监听
        OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.menu_works:
                        mViewPager.setCurrentItem(1);
                        return true;
                    case R.id.menu_media:
                        mViewPager.setCurrentItem(2);
                        return true;
                    case R.id.menu_contact:
                        mViewPager.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //初始化事件监听器
    private void InitEventListener()
    {
        //fab的点击事件
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    fab.setImageResource(R.drawable.play);
                }
                else
                {
                    mediaPlayer.start();
                    fab.setImageResource(R.drawable.pause);
                }

            }
        });
        //摇晃手机监听
        sensorHelper.setOnShakeListener(new SensorManagerHelper.OnShakeListener()
        {
            @Override
            public void onShake()
            {
                mViewPager.setCurrentItem((mViewPager.getCurrentItem()+1)%4);
                Toast.makeText(MainActivity.this, "你在摇哦", Toast.LENGTH_SHORT).show();
            }
        });

    }



    //初始化menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    //menu的选择响应监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        //分享功能
        if (id == R.id.menu_share) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareText));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, getTitle()));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //实现onFragmentInteraction接口
    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }




    //实现PageAdapter抽象类
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        ArrayList<Fragment> Fragments;
        public SectionsPagerAdapter(FragmentManager fm,ArrayList<Fragment> fragments)
        {
            super(fm);
            Fragments=fragments;
        }

        //实例化一个fragment
        @Override
        public Fragment getItem(int position)
        {
            return Fragments.get(position);
        }

        //fragment的个数
        @Override
        public int getCount()
        {
            return fragments.size();
        }

    }

    //Contact页面跳转到我的微博
    public void Contact2btn_OnClick(View v)
    {
        Toast.makeText(getApplicationContext(),"正在跳转到微博",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(getString(R.string.weiboUrl)); //微博客户端
        //如果没有安装微博，跳转到网页版
        if (!isAppInstalled(this,"com.sina.weibo"))
        {
            Toast.makeText(getApplicationContext(),"未安装微博客户端，正在跳转到网页版",Toast.LENGTH_SHORT).show();
            content_url = Uri.parse(getString(R.string.weiboUrl2));  //微博网页版
        }
        intent.setData(content_url);
        startActivity(intent);
    }

    /**
    * Contact页面跳转到我的QQ
    */
    public void Contact1btn_OnClick(View v)
    {
        //如果没有安装QQ
        if (!isAppInstalled(this,"com.tencent.mobileqq"))
        {
            Toast.makeText(getApplicationContext(),"您未安装手机QQ！",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getApplicationContext(),"正在跳转到QQ",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(getString(R.string.qqUrl));
        intent.setData(content_url);
        startActivity(intent);
    }

    /**
    * 检查网络是否连通
    */
    public boolean isNetworkAvailable() {
        ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(wifi|internet) return true;
        else return false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        mediaPlayer.stop(); //必须重写onDestroy，并使音乐停止，否则音乐播放会冲突。
    }

    /**
    * 检查APP是否安装
    */
    private boolean isAppInstalled(Context context, String packagename)
    {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        }catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return (packageInfo!=null);
    }

}



