package com.example.exercise1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exercise1.Fragments.DownloadFragment;
import com.example.exercise1.Fragments.ImageFragment;
import com.example.exercise1.Receiver.MyAlarmReceiver;
import com.example.exercise1.Others.MyImages;
import com.example.exercise1.Adapters.MyViewPagerAdapter;
import com.example.exercise1.Receiver.ReceiveReply;
import com.example.exercise1.Asyncs.Retrieve;
import com.google.android.material.tabs.TabLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements ImageFragment.onItemSelectListener{
    static final String URL = "http://10.50.201.128/myimages.xml";
    static final String KEY_ITEM = "item"; // parent node
    static final String KEY_NAME = "name";
    static final String KEY_COST = "url";
    static final String DATA = "If friends had one more character who would you choose??";
    static final int REQUEST_CODE = 100;
    int mSelecteditem;
    String mUrl;
    int mCode = 197;

    List<MyImages> mImages = new ArrayList<MyImages>();

    //Views
    TabLayout mTabLayout;
    ViewPager mViewPager;
    ImageView mSelectedImage;
    Button mBroadcast;
    Button mAlarmButton;
    EditText mTimeOfAlarm;
    Button mSecondActivity ;

    //Interfaces
    FragemntWire mFw;
    FragemntWire mFv;
    //Fragments
    FragmentManager fm;
    FragmentTransaction ft;
    DownloadFragment mDownloadFragment;

    public TextView mSelected;
    public static Context mContext;

    public interface FragemntWire{
        void passchangedselection(int selection, String url, String name);
    }

    public void initialize(){
        mSelected = (TextView) findViewById(R.id.selected);
        mSelectedImage = (ImageView) findViewById(R.id.selectedimage);
        mAlarmButton = findViewById(R.id.setalarm);
        mTimeOfAlarm = findViewById(R.id.time);
        mSecondActivity = findViewById(R.id.secondactivity);
        mBroadcast = findViewById(R.id.activity);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
    }

    public void updatelistener(FragemntWire fw){
        this.mFw = fw;
    }
    public void updatedownloadlistener(FragemntWire fv){ this.mFv = fv;}

    @RequiresApi(api = Build.VERSION_CODES.O)
    void settabs(){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        mDownloadFragment = new DownloadFragment(
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE));
        ft.add(R.id.fragment_3,mDownloadFragment,null).commit();
        mTabLayout.addTab(mTabLayout.newTab().setText("List"));
        mTabLayout.addTab(mTabLayout.newTab().setText("SelectedImage"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        String xml = xmlfromURL(URL);
        Document doc = DomElement(xml);
        mContext = getApplicationContext();

        NodeList nl = doc.getElementsByTagName(KEY_ITEM);
        for(int i = 0; i<nl.getLength();i++){
            Element e = (Element) nl.item(i);
            MyImages temp = new MyImages(getValue(e,KEY_NAME),getValue(e,KEY_COST));
            mImages.add(temp);
        }

        settabs();

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyViewPagerAdapter adapter = new MyViewPagerAdapter(
                    this,getSupportFragmentManager(),
                        mTabLayout.getTabCount(),
                        mImages);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 1){
                    mFw.passchangedselection(mSelecteditem + 1,
                            mUrl, mImages.get(mSelecteditem).getName());
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time =Integer.parseInt(mTimeOfAlarm.getText().toString());
                if(mTimeOfAlarm.getText().length() > 0){
                    setalarm(time);
                } else{
                    Toast.makeText(getApplicationContext(), mTimeOfAlarm.getText(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        mSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OtherActivity.class);
                intent.putExtra("data", DATA);
                startActivityForResult(intent,197);
            }
        });


        IntentFilter filter = new IntentFilter("com.example.Broadcastd");
        ReceiveReply receiver = new ReceiveReply();
        registerReceiver(receiver, filter);


    }

    public void setalarm(int time){
        Intent intent = new Intent(this, MyAlarmReceiver.class);
        PendingIntent pendingintent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingintent);
        Toast.makeText(getApplicationContext(),"Your message will be send in " + time + "seconds",Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == mCode){
            if(data.hasExtra("selected")){
                Toast.makeText(getApplicationContext(), data.getStringExtra("selected"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemclicked(int selected, String url) {
        //Toast.makeText(this, String.valueOf(selected), Toast.LENGTH_SHORT).show();
        if(url == null){
            Toast.makeText(this, "Null Value",Toast.LENGTH_SHORT);
        } else{
            Log.d("onItem: ", String.valueOf(url));
            mSelecteditem = selected;
            mUrl = url;
            mFv.passchangedselection(selected + 1, url, mImages.get(selected).getName());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.e("value", "Granted");
                } else{
                    Log.e("value", "Denied");
                }
                break;
        }
    }

    public Document DomElement(String output){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(output));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());

        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());

        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());

        }
        return doc;
    }

    public String xmlfromURL(String url){
        String output = null;

        try{
            output = new Retrieve().execute(url).get();
            Log.d("output", output);
        } catch(ExecutionException e){

        } catch (InterruptedException e){

        }
        return output;
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue( Node elem ) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
}