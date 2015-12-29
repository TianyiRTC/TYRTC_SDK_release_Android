package com.rtc.sdk;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rtc.sdk.clt.RtcClientImpl;
import rtc.sdk.common.RtcConst;
import rtc.sdk.common.SdkSettings;
import rtc.sdk.core.RtcRules;
import rtc.sdk.iface.ClientListener;
import rtc.sdk.iface.GroupMgr;
import rtc.sdk.iface.Connection;
import rtc.sdk.iface.ConnectionListener;
import rtc.sdk.iface.Device;
import rtc.sdk.iface.DeviceListener;
import rtc.sdk.iface.GroupCallListener;
import rtc.sdk.iface.RtcClient;
import jni.http.HttpManager;
import jni.http.HttpResult;
import jni.http.RtcHttpClient;
import jni.util.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class DemoApp.
 */
public class DemoApp extends Activity {

    public static  String APP_ID = "123";// 
    public static  String APP_KEY ="123456";// 

    /** The LOGTAG. */
    private String LOGTAG= "DemoApp";
    
    /** The Username. */
    public  String Username="";

    /** The Constant MSG_SERADDR. */
    public static final int MSG_SERADDR=10000;
    
    /** The Constant MSG_GETTOKEN. */
    public static final int MSG_GETTOKEN=10001;
    
    /** The Constant MSG_GRP_HANGUP. */
    public static final int MSG_GRP_HANGUP = 10002;
    
    /** The Constant MSG_GRP_BEI_CREATE. */
    public static final int MSG_GRP_BEI_CREATE = 10003;
    
    /** The Constant MSG_GRP_ZHU_CREATE. */
    public static final int MSG_GRP_ZHU_CREATE = 10004;
    
    /** The Constant MSG_GRP_BEI_ACCEPT. */
    public static final int MSG_GRP_BEI_ACCEPT = 10005;
    
    /** The Constant MSG_GRP_OPT_GETMEMBERLIST. */
    public static final int MSG_GRP_OPT_GETMEMBERLIST=10006;

    /** The Constant MSG_GRP_OPT_GETFROUPLIST. */
    public static final int MSG_GRP_OPT_GETFROUPLIST=10007;
    
    /** The sp_codec. */
    private Spinner sp_codec;
    
    /** The adapter_codec. */
    private ArrayAdapter adapter_codec;
    
    /** The btngrpv_hangup. */
    private Button btngrp_accept,btngrp_hangup;
    
    /** The calltype. */
    private int calltype = 1;
    
    /** The m clt. */
    RtcClient mClt;
    
    /** The m_grptype. */
    public  int  m_grptype; //当前会议类型
    
    /** The m_grpname. */
    public  String m_grpname; //当前会议组名
    
    /** The m_grpname. */
    private  boolean b_creator; //是否为创建者，供主动加入会议接口使用
    
    /** The m_grpname. */
    private  String m_grpid; //当前会议ID，供主动加入会议接口使用
    
    /** The capabailitytoken. */
    private String capabailitytoken;
    
    /** The sp_grptype. */
    private Spinner sp_grptype;
    
    /** The adapter_grptype. */
    private ArrayAdapter adapter_grptype;
    
    /** The tv_version. */
    private TextView tv_version;

    /** The m handler. */
    Handler mHandler = new Handler() {

    };
    
    /**
     * Sets the status text.
     *
     * @param sText the new status text
     */
    void setStatusText(String sText) {
        SimpleDateFormat tm = new SimpleDateFormat("hh:mm:ss");
        final String outStr = tm.format(new Date()) + ": " + sText;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                setEditText(R.id.ed_status,outStr);
            }
        });
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demoapp);
        btngrp_accept = (Button)findViewById(R.id.bt_grpcall_accept);
        btngrp_hangup = (Button)findViewById(R.id.bt_grpcall_Hangup);
        layoutlocal = (LinearLayout) findViewById(R.id.ll_local);
        layoutremote = (LinearLayout) findViewById(R.id.ll_remote);

        tv_version = (TextView)findViewById(R.id.tv_version);
        btngrp_accept.setVisibility(View.INVISIBLE);
        btngrp_hangup.setVisibility(View.INVISIBLE);
        tv_version.setText(RtcConst.sdkVersion+"; appid: "+APP_ID);//getAppVersionName()
        initSpinnerCodec();
        initSpinnerGrpType();
        updateEditInfo(0xffffffff);
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if(layoutremote!=null) {
            if(mGroupCall!=null)
                mGroupCall.resetVideoViews();
            if(mCall!=null)
                mCall.resetVideoViews();
        }
        super.onResume();
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.PrintLog(5,LOGTAG, "onDestroy()");
        if (mCall!=null) {
            mCall.disconnect();
            mCall = null;
        }
        if(layoutremote!=null)
            layoutremote.removeAllViews();
        if(layoutlocal!=null)
            layoutlocal.removeAllViews();
        mvLocal = null;
        mvRemote = null;

        if(mAcc!=null) {
            mAcc.release();
            mAcc = null;
        }
        if(mClt!=null) {
            mClt.release();
            mClt = null;
        }
    }

    /**
     * Gets the edits the text.
     *
     * @param id the id
     * @return the edits the text
     */
    String getEditText(int id) { return ((EditText)findViewById(id)).getText().toString(); }
    
    /**
     * Gets the edits the int.
     *
     * @param id the id
     * @return the edits the int
     */
    int getEditInt(int id) { return Integer.parseInt(getEditText(id)); }
    
    /**
     * Sets the edit text.
     *
     * @param id the id
     * @param txt the txt
     */
    void setEditText(int id,String txt) { ((EditText)findViewById(id)).setText(txt); }
    
    /**
     * Sets the edit int.
     *
     * @param id the id
     * @param txt the txt
     */
    void setEditInt(int id,int txt) { setEditText(id,Integer.toString(txt)); }
    
    /**
     * Sets the btn text.
     *
     * @param id the id
     * @param txt the txt
     */
    void setBtnText(int id,String txt) { ((Button)findViewById(id)).setText(txt); }
    
    /** The mv local. */
    SurfaceView mvLocal = null;
    
    /** The mv remote. */
    SurfaceView mvRemote = null;
    
    /** The layoutlocal. */
    LinearLayout layoutlocal;
    
    /** The layoutremote. */
    LinearLayout layoutremote;


    
    /**
     * Inits the video views.
     */
    void initVideoViews() {
        if (mvLocal!=null) return;
        if(mCall != null)
            mvLocal = (SurfaceView)mCall.createVideoView(true, this, true);
        else if(mGroupCall != null)
            mvLocal = (SurfaceView)mGroupCall.createVideoView(true, this, true);
        mvLocal.setVisibility(View.INVISIBLE);
        layoutlocal.addView(mvLocal);
        mvLocal.setKeepScreenOn(true);
        mvLocal.setZOrderMediaOverlay(true);
        mvLocal.setZOrderOnTop(true);

        if(mCall != null)
            mvRemote = (SurfaceView)mCall.createVideoView(false, this, true);
        else if(mGroupCall != null)
            mvRemote = (SurfaceView)mGroupCall.createVideoView(false, this, true);
        mvRemote.setVisibility(View.INVISIBLE);
        mvRemote.setKeepScreenOn(true);
        mvRemote.setZOrderMediaOverlay(true);
        mvRemote.setZOrderOnTop(true);
        layoutremote.addView(mvRemote);
    }
    
    /**
     * Sets the video surface visibility.
     *
     * @param visible the new video surface visibility
     */
    void setVideoSurfaceVisibility(int visible) {
        if(mvLocal!=null)
            mvLocal.setVisibility(visible);
        if(mvRemote!=null)
            mvRemote.setVisibility(visible);
    }

    /** The s_acc. */
    String[] s_acc = new String[] {"1002","1003","8103","8104","3005","2008"};
    
    /** The m user. */
    int mUser = 0;
    
    /** The m rem. */
    int mRem = 1;

    /**
     * On btn acc.
     *
     * @param viw the viw
     */
    public void onBtnAcc(View viw) {
        new AlertDialog.Builder(this).setTitle("选择登录帐号").setItems(s_acc,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mUser = arg1;
                updateEditInfo(0x02);
            }
        }).show();
    }
    
    /**
     * On btn remote.
     *
     * @param viw the viw
     */
    public void onBtnRemote(View viw) {
        new AlertDialog.Builder(this).setTitle("选择被叫号码").setItems(s_acc,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mRem = arg1;
                updateEditInfo(0x04);
            }
        }).show();
    }

    /**
     * Update edit info.
     *
     * @param nMask the n mask
     */
    void updateEditInfo(int nMask) {
        if ((nMask&0x02)!=0) setEditText(R.id.ed_user,s_acc[mUser]);
        if ((nMask&0x04)!=0) setEditText(R.id.ed_remoteuri,s_acc[mRem]);
    }

    /** The s_call_type. */
    String[] s_call_type = new String[] {"Audio","Video","Audio+Video","RecvOnly","SendOnly","ARVS","ARVD","ASVR","ASVD","ADVR","ADVS"};
    
    /** The m ct. */
    int mCT = RtcConst.CallType_A_V;
    
    /**
     * On btn call type.
     *
     * @param viw the viw
     */
    public void onBtnCallType(View viw) {
        final Button bt = (Button)viw;
        new AlertDialog.Builder(this).setTitle("选择呼叫类型").setItems(s_call_type,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mCT = arg1+1;
                if(arg1 == 3)
                    mCT = RtcConst.CallType_A_V_M;
                else if(arg1 == 4)
                    mCT = RtcConst.CallType_A_V_L;
                else if(arg1 == 5)
                    mCT = RtcConst.CallType_A_R_V_S;
                else if(arg1 == 6)
                    mCT = RtcConst.CallType_A_R_V_D;
                else if(arg1 == 7)
                    mCT = RtcConst.CallType_A_S_V_R;
                else if(arg1 == 8)
                    mCT = RtcConst.CallType_A_S_V_D;
                else if(arg1 == 9)
                    mCT = RtcConst.CallType_A_D_V_R;
                else if(arg1 == 10)
                    mCT = RtcConst.CallType_A_D_V_S;
                String[] ctype = new String[] {"A","V","A+V","R","S","ARVS","ARVD","ASVR","ASVD","ADVR","ADVS"};
                bt.setText(ctype[arg1]);
            }
        }).show();
    }


    /** The m init. */
    boolean mInit = false; //init 

    /**
     * On btn init.
     *
     * @param viw the viw
     */
    public void onBtnInit(View viw) {
        mInit = !mInit;
        Utils.PrintLog(5, LOGTAG, "onBtnInit mInit :"+mInit);

        if (mInit) {
             initRtcClientImpl();
        }
        else {
            if(mAcc!=null) {
                mAcc.release();
                mAcc = null;
            }
            setBtnText(R.id.bt_register,"Login");
            if(mClt!=null)
                mClt.release();
            mClt = null;
            setBtnText(R.id.bt_init,"Init");
            setStatusText("");
        }
    }

    /**
     * Inits the rtc client impl.
     */
    private void initRtcClientImpl() {
        Utils.PrintLog(5,"DemoApp", "initRtcClientImpl()");
        mClt = new RtcClientImpl();
        mClt.initialize(this.getApplicationContext(), new ClientListener() {
            @Override   //初始化结果回调
            public void onInit(int result) {
                Utils.PrintLog(5,"ClientListener","onInit,result="+result);
                setStatusText("ClientListener:onInit,result="+result);
                if(result == 0) {
                    setBtnText(R.id.bt_init,"Uninit");
                    mClt.setAudioCodec(RtcConst.ACodec_OPUS);
                    mClt.setVideoCodec(RtcConst.VCodec_VP8);
                    mClt.setVideoAttr(RtcConst.Video_SD);
                }
                else
                    mInit = false;
            }
        });
    }

    /** The m acc. */
    Device mAcc = null;  //reg
    
    /** The grpmgr. */
    GroupMgr grpmgr;
    
    /** The m a listener. */
    DeviceListener mAListener = new DeviceListener() {
        @Override
        public void onDeviceStateChanged(int result) {
            Utils.PrintLog(5,"DeviceListener","onDeviceStateChanged,result="+result);
            setStatusText("StateChanged,result="+result);
            if(result == RtcConst.CallCode_Success) { //注销也存在此处
                if(mAcc!=null) {
                    grpmgr = mAcc.getGroup();
                    grpmgr.setGroupCallListener(mGrpCallListener);
                }
            }
            else if(result == RtcConst.NoNetwork) {
                onNoNetWork();
            }
            else if(result == RtcConst.ChangeNetwork) {
                ChangeNetWork();
            }
            else if(result == RtcConst.PoorNetwork) {
                onPoorNetWork();
            }
            else if(result == RtcConst.ReLoginNetwork) {
            // 网络原因导致多次登陆不成功，由用户选择是否继续，如想继续尝试，可以重建device
                Utils.PrintLog(5,"DeviceListener","onDeviceStateChanged,ReLoginNetwork");
            }
            else if(result == RtcConst.DeviceEvt_KickedOff) {
            // 被另外一个终端踢下线，由用户选择是否继续，如果再次登录，需要重新获取token，重建device
                Utils.PrintLog(5,"DeviceListener","onDeviceStateChanged,DeviceEvt_KickedOff");
            }
            else if(result == RtcConst.DeviceEvt_MultiLogin) {
                Utils.PrintLog(5,"DeviceListener","onDeviceStateChanged,DeviceEvt_MultiLogin");
            }
            else {
                //  CommFunc.DisplayToast(MyApplication.this, "注册失败:"+result);
            }
        }
        private void onPoorNetWork() {
            Utils.PrintLog(5, LOGTAG, "onPoorNetWork");
        }
        private void onNoNetWork() {
            Utils.PrintLog(5, LOGTAG, "onNoNetWork");
            Utils.DisplayToast(DemoApp.this, "onNoNetWork");
            //断网销毁 
            if (mCall!=null) {
                mCall.disconnect();
                mCall = null;
            }
            if (mGroupCall!=null) {
                mGroupCall.disconnect();
                mGroupCall = null;
            }
            setStatusText("pls check network");
        }
        private void ChangeNetWork() {
            Utils.PrintLog(5, LOGTAG, "ChangeNetWork");
            //自动重连接  
        }

        @Override
        public void onSendIm(int nStatus) {
            if(nStatus == RtcConst.CallCode_Success)
                Utils.PrintLog(5, LOGTAG, "发送IM成功");
            else
                Utils.PrintLog(5, LOGTAG, "发送IM失败:"+nStatus);
        }
        @Override
        public void onReceiveIm(String from,String mime,String content) {
            Utils.PrintLog(5, LOGTAG, "onReceiveIm:"+from+mime+content);
        }

        @Override
        public void onNewCall(Connection call) {

            Utils.PrintLog(5,"DeviceListener","onNewCall,call="+call.info());
            setStatusText("DeviceListener:onNewCall,call="+call.info());
            if (mCall!=null) {
                call.reject();
                call = null;
                Utils.PrintLog(5,"DeviceListener","onNewCall,reject call");
                setStatusText("DeviceListener:onNewCall,reject call");
                return;
            }
            mIncoming = true;
            mCall = call;
            call.setIncomingListener(mCListener);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setBtnText(R.id.bt_Call,"Accept");
                }
            });
        }
        @Override
        public void onQueryStatus(int status, String paramers) {
            // TODO Auto-generated method stub
        }
    };

    /**
     * 终端直接从rtc平台获取token，应用产品需要通过自己的服务器来获取，rtc平台接口请参考开发文档<2.5>章节.
     */
    private  void  opt_getToken() {
        RtcConst.UEAPPID_Current = RtcConst.UEAPPID_Self;//账号体系，包括私有、微博、QQ等，必须在获取token之前确定。
        JSONObject jsonobj = HttpManager.getInstance().CreateTokenJson(0,getEditText(R.id.ed_user),RtcHttpClient.grantedCapabiltyID,"");
        HttpResult  ret = HttpManager.getInstance().getCapabilityToken(jsonobj, APP_ID, APP_KEY);
        setStatusText("获取token:"+ret.getStatus()+" reason:"+ret.getErrorMsg());
        Message msg = new Message();
        msg.what = MSG_GETTOKEN;
        msg.obj = ret;
        UIHandler.sendMessage(msg);
    }

    /**
     * Rest register.
     */
    private void restRegister() {
        new Thread(){
            public void run(){
                System.out.println("Thread is running.");
                opt_getToken();
            }
        }.start();

        //如果直接注册
    }
    

    /**
     * On btn login.
     *
     * @param viw the viw
     */
    public void onBtnLogin(View viw) {

        if (mClt==null) return;

        if(mInit==false){
            setStatusText("please first init");
            return;
        }
        Username = getEditText(R.id.ed_user);
        Utils.PrintLog(5, LOGTAG, "onBtnLogin:"+Username);
        if (mAcc==null) {
            //获取副uwqi
            restRegister();

        } else { //注销
            mAcc.release();
            mAcc = null;
            setBtnText(R.id.bt_register,"Login");
        }
    }

    /**
     * On register.
     *
     * @param sname the sname
     * @param spwd the spwd
     */
    private void OnRegister(String sname ,String spwd) {
        Utils.PrintLog(5, LOGTAG, "OnRegister:"+sname+"spwd:"+spwd);
        try {
            JSONObject jargs = SdkSettings.defaultDeviceSetting();
            jargs.put(RtcConst.kAccPwd,spwd);
            Utils.PrintLog(5, LOGTAG, "user:"+getEditText(R.id.ed_user) +"token:"+spwd);
            
            //账号格式形如“账号体系-号码~应用id~终端类型”，以下主要设置账号内各部分内容，其中账号体系的值要在获取token之前确定，默认为私有账号
            jargs.put(RtcConst.kAccAppID,APP_ID);//应用id
            //jargs.put(RtcConst.kAccName,"逍遥神龙");
            jargs.put(RtcConst.kAccUser,sname); //号码
            jargs.put(RtcConst.kAccType,RtcConst.UEType_Current);//终端类型
            
            mAcc = mClt.createDevice(jargs.toString(), mAListener); //注册
            setBtnText(R.id.bt_register,"Logout");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /** The m call. */
    Connection mCall;
    
    /** The m c listener. */
    ConnectionListener mCListener = new ConnectionListener() {
        @Override
        public void onConnecting() {
            setStatusText("ConnectionListener:onConnecting");
        }
        @Override
        public void onConnected() {
            setStatusText("ConnectionListener:onConnected");
        }
        @Override
        public void onDisconnected(int code) {
            setStatusText("ConnectionListener:onDisconnect,code="+code);
            Utils.PrintLog(5, LOGTAG, "onDisconnected timerDur"+mCall.getCallDuration());
            mCall = null;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    setBtnText(R.id.bt_Call,"Call");
                    setVideoSurfaceVisibility(View.INVISIBLE);
                }
            });
        }
        @Override
        public void onVideo() {
            setStatusText("ConnectionListener:onVideo");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    initVideoViews();
                    mCall.buildVideo(mvRemote);
                    setVideoSurfaceVisibility(View.VISIBLE);
                }
            });
        }
        @Override
        public void onNetStatus(int msg, String info) {
            // TODO Auto-generated method stub
        }
    };
    

    /** The m incoming. */
    boolean mIncoming = false;
    
    /**
     * On btn call.
     *
     * @param viw the viw
     */
    public void onBtnCall(View viw) {
        Utils.PrintLog(5,"onBtnCall", "onBtnCall(): transtype"+RtcConst.TransType);
        if (mAcc==null)
            return;
        if (mCall==null) {
            mIncoming = false;
            try {
                //;transport=tls
                String remoteuri = "";
                remoteuri = RtcRules.UserToRemoteUri_new(getEditText(R.id.ed_remoteuri), RtcConst.UEType_Any);

                JSONObject jinfo = new JSONObject();
                jinfo.put(RtcConst.kCallRemoteUri,remoteuri);
                jinfo.put(RtcConst.kCallInfo,"逍遙神龍--->"); //opt
                jinfo.put(RtcConst.kCallType,mCT);
                mCall = mAcc.connect(jinfo.toString(),mCListener);
                setBtnText(R.id.bt_Call,"Calling");
                //IM test
                //mAcc.sendIm(remoteuri, RtcConst.ImText, "聊天吧");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else { // acceptcall
            if (mIncoming) {
                mIncoming = false;
                mCall.accept(mCT); //视频来电可以选择仅音频接听
                setBtnText(R.id.bt_Call,"Calling");
                Utils.PrintLog(5,LOGTAG, "onBtnCall mIncoming accept(mCT)");
                setStatusText("ConnectionListener:onConnected");
            }
        }
        setBtnText(R.id.bt_Call,"Calling");
    }
    
    /**
     * On btn hangup.
     *
     * @param viw the viw
     */
    public void onBtnHangup(View viw) {
        if (mCall!=null) {
            mCall.disconnect();
            Utils.PrintLog(5, LOGTAG, "onBtnHangup timerDur"+mCall.getCallDuration());
            mCall = null;
            setVideoSurfaceVisibility(View.INVISIBLE);
            setBtnText(R.id.bt_Call,"Call");
        }
        setStatusText("call hangup ");
    }
    
    /**
     * Inits the spinner codec.
     */
    private void initSpinnerCodec() {
        sp_codec = (Spinner)findViewById(R.id.spinner_codec);
        //将可选内容与ArrayAdapter连接起来
        adapter_codec = ArrayAdapter.createFromResource(this, R.array.codec, android.R.layout.simple_spinner_item);
        //设置下拉列表的风格
        adapter_codec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //设置下拉列表的风格
        //将adapter2 添加到spinner中
        sp_codec.setAdapter(adapter_codec);
        //添加事件Spinner事件监听
        sp_codec.setOnItemSelectedListener(new SpinnerXMLSelectedListener_codec());
        //设置默认值
        sp_codec.setVisibility(View.VISIBLE);
    }
    //使用XML形式操作
    /**
     * The Class SpinnerXMLSelectedListener_vformat.
     */
    class SpinnerXMLSelectedListener_codec implements OnItemSelectedListener{

        /* (non-Javadoc)
         * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
         */
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            if(mClt != null)
                mClt.setVideoCodec(arg2);
            Utils.PrintLog(5, LOGTAG,"选择codec:"+adapter_codec.getItem(arg2));
        }
        
        /* (non-Javadoc)
         * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
         */
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Utils.PrintLog(5,LOGTAG, "onBackPressed()");

    }


    /**
     * On response_get token.
     *
     * @param msg the msg
     */
    private void OnResponse_getToken(Message msg) {
        HttpResult  ret = (HttpResult)msg.obj;
        Utils.PrintLog(5,LOGTAG, "handleMessage getCapabilityToken status:"+ret.getStatus());
        JSONObject jsonrsp = (JSONObject)ret.getObject();
        if(jsonrsp!=null && jsonrsp.isNull("code")==false) {
            try {
                String code = jsonrsp.getString(RtcConst.kcode);
                String reason = jsonrsp.getString(RtcConst.kreason);
                Utils.PrintLog(5, LOGTAG, "Response getCapabilityToken code:"+code+" reason:"+reason);
                if(code.equals("0")) {
                    capabailitytoken =jsonrsp.getString(RtcConst.kcapabilityToken);

                    Utils.PrintLog(5,LOGTAG, "handleMessage getCapabilityToken:"+capabailitytoken);
                    OnRegister(Username,capabailitytoken);
                }
                else {
                    Utils.DisplayToast(DemoApp.this, "获取token失败 [status:"+ret.getStatus()+"]"+ret.getErrorMsg());
                    Utils.PrintLog(5, LOGTAG, "获取token失败 [status:"+ret.getStatus()+"]"+ret.getErrorMsg());
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    ArrayList<String> groupList;
    /** The UI handler. */
    private Handler UIHandler = new Handler() {
        @Override 
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_GETTOKEN:
                    OnResponse_getToken(msg);
                    break;
                case MSG_GRP_HANGUP:
                    setVideoSurfaceVisibility(View.INVISIBLE);
                    btngrp_accept.setVisibility(View.INVISIBLE);
                    btngrp_hangup.setVisibility(View.INVISIBLE);
                    break;
                case MSG_GRP_BEI_CREATE:
                    Utils.DisplayToast(DemoApp.this, "有会议邀请加入"+m_grpname);
                    btngrp_accept.setVisibility(View.VISIBLE);
                    btngrp_hangup.setVisibility(View.VISIBLE); //可以选择拒接
                    break;
                case MSG_GRP_ZHU_CREATE:
                    btngrp_accept.setVisibility(View.INVISIBLE);
                    btngrp_hangup.setVisibility(View.VISIBLE);
                    break;
                case MSG_GRP_BEI_ACCEPT:
                    btngrp_accept.setVisibility(View.INVISIBLE);
                    btngrp_hangup.setVisibility(View.VISIBLE);
                    break;
                case MSG_GRP_OPT_GETMEMBERLIST: {
                    JSONObject jsonrsp = (JSONObject)msg.obj;
                    List<TGrpMemberInfo> list = new ArrayList<TGrpMemberInfo>();
                    JSONArray jsonArr;
                    try {
                        jsonArr = jsonrsp.getJSONArray(RtcConst.kGrpMemArray);
                        for (int i = 0; i < jsonArr.length(); i++) {
                            TGrpMemberInfo grpmemberinfo= new TGrpMemberInfo();
                            JSONObject itemObject = jsonArr.getJSONObject(i);
                            grpmemberinfo.setUserid(itemObject.getString(RtcConst.kappAccountID)); //请求的对应标识
                            grpmemberinfo.setMemberStatus(itemObject.getInt(RtcConst.kGrpMemStatus));
                            grpmemberinfo.setStartTime(itemObject.getString("startTime"));
                            grpmemberinfo.setDuration(itemObject.getInt("duration"));
                            grpmemberinfo.setupAudioState(itemObject.getInt("upAudioState"));
                            grpmemberinfo.setupVideoState(itemObject.getInt("upVideoState"));
                            grpmemberinfo.setdownAudioState(itemObject.getInt("downAudioState"));
                            grpmemberinfo.setdownVideoState(itemObject.getInt("downVideoState"));
                            grpmemberinfo.setRole(itemObject.getInt(RtcConst.kGrpRole));
                            list.add(grpmemberinfo);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                }
                case MSG_GRP_OPT_GETFROUPLIST:
                    JSONArray jsonArr;
                    JSONObject jsonrsp = (JSONObject)msg.obj;
                    if(groupList != null)
                        groupList.clear();
                    else
                        groupList = new ArrayList<String>();
                    try {
                        jsonArr = jsonrsp.getJSONArray(RtcConst.kGrpList);
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject itemObject = jsonArr.getJSONObject(i);
                            groupList.add(itemObject.getString(RtcConst.kcallId));
                        }
                        String[] groupArray = new String[groupList.size()];
                        groupList.toArray(groupArray);
                        new AlertDialog.Builder(DemoApp.this).setTitle("选择会议ID").setItems(groupArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                m_grpid = groupList.get(arg1);
                            }
                        }).show();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    };

    /** The m group call. */
    Connection mGroupCall;
    
    /** The m grp listener. */
    ConnectionListener mGrpListener = new ConnectionListener() {
        @Override
        public void onConnecting() {
            setStatusText("mGrpListener onConnecting");
            Utils.PrintLog(5, LOGTAG, "group onConnecting()");
        }
        @Override
        public void onConnected() {
            setStatusText("mGrpListener onConnected");
            Utils.PrintLog(5, LOGTAG, "group onConnected()");
        }
        @Override
        public void onDisconnected(int code) {
            setStatusText("ConnectionListener:onDisconnect grp,code="+code);
            mGroupCall = null;
            Utils.PrintLog(5, LOGTAG, "group onDisConnected()");
            Message msg = new Message();
            msg.what = MSG_GRP_HANGUP;
            UIHandler.sendMessage(msg);
        }
        @Override
        public void onVideo() {
            setStatusText("ConnectionListener:onVideo");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    initVideoViews();
                    mGroupCall.buildVideo(mvRemote);
                    setVideoSurfaceVisibility(View.VISIBLE);
                }
            });
        }
        @Override
        public void onNetStatus(int msg, String info) {
            // TODO Auto-generated method stub
        }
    };
    
    /** The m grp voice listener. */
    GroupCallListener mGrpCallListener = new GroupCallListener() {
        private void onResponse_grpcreate(String parameters) {
            //RestMgr中对应groupVoice_optCreate
            try {
                if(parameters==null || parameters.equals("")) {
                    Utils.PrintLog(5, LOGTAG, "onResponse_grpcreate resopnse strResponse null");
                    return;
                }
                JSONObject jsonrsp = new JSONObject(parameters);
                Utils.PrintLog(5, LOGTAG, "onResponse_grpcreate resopnse:"+parameters);
                int code = jsonrsp.getInt(RtcConst.kcode);
                String reason = jsonrsp.getString(RtcConst.kreason);
                Utils.PrintLog(5, LOGTAG, "onResponse_grpcreate code:"+code+" reason:"+reason);
                if(code == 0) {
                    b_creator = true;
                    m_grpid = jsonrsp.getString(RtcConst.kcallId);
                    setStatusText("会议创建成功:"+parameters);
                    Message msg = new Message();
                    msg.what = MSG_GRP_ZHU_CREATE;
                    UIHandler.sendMessage(msg);
                }
                else {
                    setStatusText("会议创建失败:"+code+" reason:"+reason);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                setStatusText("会议创建失败 JSONException:"+e.getMessage());
                e.printStackTrace();
            }
        }
        private void onResponse_grpgetMemberLis(String parameters) {
            try {
                if(parameters == null || parameters.equals("")) {
                    Utils.PrintLog(5, LOGTAG, "onResponse_grpgetMemberLis fail result: null");
                    return;
                }
                JSONObject jsonrsp = new JSONObject(parameters);
                String code = jsonrsp.getString(RtcConst.kcode);
                String reason = jsonrsp.getString(RtcConst.kreason);
                Utils.PrintLog(5, LOGTAG, "onResponse_grpgetMemberLis code:"+code+" reason:"+reason);
                if(code.equals("0")) {
                    setStatusText("获取成员列表成功:"+parameters);
                    Message msg = new Message();
                    msg.what = MSG_GRP_OPT_GETMEMBERLIST;
                    msg.obj = jsonrsp;
                    UIHandler.sendMessage(msg);
                }
                else {
                    setStatusText("获取成员列表失败:"+code+" reason:"+reason);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                setStatusText("获取成员列表失败 JSONException:"+e.getMessage());
                e.printStackTrace();
            }

        }
        private void onResponse_grpInvitedMemberList(String parameters) {
            try {
                Utils.PrintLog(5, LOGTAG, "onResponse_grpInvitedMemberList:"+parameters);
                if(parameters == null || parameters.equals(""))
                    return;
                JSONObject jsonrsp = new JSONObject(parameters);
                if(jsonrsp.isNull("code")==false) {
                    String code = jsonrsp.getString(RtcConst.kcode);
                    String reason = jsonrsp.getString(RtcConst.kreason);
                    Utils.PrintLog(5, LOGTAG, "onResponse_grpInvitedMemberList code:"+code+" reason:"+reason);
                    if(code.equals("0")) {
                        setStatusText("邀请成员参与群组会话成功:"+parameters);
                    }
                    else {
                        setStatusText("邀请成员参与群组会话失败:"+code+" reason:"+reason);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                setStatusText("邀请成员参与群组会话失败 JSONException:"+e.getMessage());
                e.printStackTrace();
            }
        }

        private void onResponse_grpkickedMemberList(String parameters) {
            try {
                if(parameters == null || parameters.equals(""))
                    return;
                JSONObject jsonrsp = new JSONObject(parameters);
                if(jsonrsp.isNull("code")==false) {
                    String code = jsonrsp.getString(RtcConst.kcode);
                    String reason = jsonrsp.getString(RtcConst.kreason);
                    Utils.PrintLog(5, LOGTAG, "onResponse_grpkickedMemberList code:"+code+" reason:"+reason);
                    if(code.equals("0") || code.equals("200")) {
                        setStatusText("踢出成员成功:"+parameters);
                    }
                    else {
                        setStatusText("踢出成员失败:"+code+" reason:"+reason);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                setStatusText("踢出成员失败 JSONException:"+e.getMessage());
                e.printStackTrace();
            }
        }
        private void onResponse_grpClose(String parameters) {
            try {
                if(parameters == null || parameters.equals(""))
                    return;
                JSONObject jsonrsp = new JSONObject(parameters);
                if(jsonrsp.isNull("code")==false) {
                    String code = jsonrsp.getString(RtcConst.kcode);
                    String reason = jsonrsp.getString(RtcConst.kreason);
                    Utils.PrintLog(5, LOGTAG, "onResponse_grpClose code:"+code+" reason:"+reason);
                    if(code.equals("0") || code.equals("200")) {
                        setStatusText("关闭群组成功:"+parameters);
                    }
                    else {
                        setStatusText("关闭群组失败:"+code+" reason:"+reason);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                setStatusText("关闭群组失败 JSONException:"+e.getMessage());
                e.printStackTrace();
            }
        }
        private void onResponse_grpStreamManagement(String parameters) {
            try {
                if(parameters == null || parameters.equals(""))
                    return;
                JSONObject jsonrsp = new JSONObject(parameters);
                if(jsonrsp.isNull("code")==false) {
                    String code = jsonrsp.getString(RtcConst.kcode);
                    String reason = jsonrsp.getString(RtcConst.kreason);
                    Utils.PrintLog(5, LOGTAG, "onResponse_grpStreamManagement code:"+code+" reason:"+reason);
                    if(code.equals("0")) {
                        setStatusText("媒体流控制成功:"+parameters);
                    }
                    else {
                        setStatusText("媒体流控制失败:"+code+" reason:"+reason);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                setStatusText("媒体流控制失败 JSONException:"+e.getMessage());
                e.printStackTrace();
            }
        }
        private void onResponse_grpJoin(String parameters) {
            try {
                Utils.PrintLog(5, LOGTAG, "onResponse_grpJoin:"+parameters);
                if(parameters == null || parameters.equals(""))
                    return;
                JSONObject jsonrsp = new JSONObject(parameters);
                if(jsonrsp.isNull("code")==false) {
                    String code = jsonrsp.getString(RtcConst.kcode);
                    String reason = jsonrsp.getString(RtcConst.kreason);
                    Utils.PrintLog(5, LOGTAG, "onResponse_grpJoin code:"+code+" reason:"+reason);
                    if(code.equals("0")) {
                        setStatusText("主动加入群组会话成功:"+parameters);
                    }
                    else {
                        setStatusText("主动加入群组会话失败:"+code+" reason:"+reason);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                setStatusText("主动加入群组会话失败 JSONException:"+e.getMessage());
                e.printStackTrace();
            }
        }
        private void onResponse_grpQList(String parameters) {
            try {
                Utils.PrintLog(5, LOGTAG, "onResponse_grpQList:"+parameters);
                if(parameters == null || parameters.equals(""))
                    return;
                JSONObject jsonrsp = new JSONObject(parameters);
                if(jsonrsp.isNull("code")==false) {
                    String code = jsonrsp.getString(RtcConst.kcode);
                    String reason = jsonrsp.getString(RtcConst.kreason);
                    Utils.PrintLog(5, LOGTAG, "onResponse_grpQList code:"+code+" reason:"+reason);
                    if(code.equals("0")||code.equals("200")) {
                        setStatusText("查询群组列表成功:"+parameters);
                        Message msg = new Message();
                        msg.what = MSG_GRP_OPT_GETFROUPLIST;
                        msg.obj = jsonrsp;
                        UIHandler.sendMessage(msg);
                    }
                    else {
                        setStatusText("查询群组列表失败:"+code+" reason:"+reason);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                setStatusText("查询群组列表失败 JSONException:"+e.getMessage());
                e.printStackTrace();
            }
        }
        private void onResponse_grpMDisp(String parameters) {
            try {
                Utils.PrintLog(5, LOGTAG, "onResponse_grpMDisp:"+parameters);
                if(parameters == null || parameters.equals(""))
                    return;
                JSONObject jsonrsp = new JSONObject(parameters);
                if(jsonrsp.isNull("code")==false) {
                    String code = jsonrsp.getString(RtcConst.kcode);
                    String reason = jsonrsp.getString(RtcConst.kreason);
                    Utils.PrintLog(5, LOGTAG, "onResponse_grpMDisp code:"+code+" reason:"+reason);
                    if(code.equals("0")||code.equals("200")) {
                        setStatusText("分屏设置成功:"+parameters);
                        Message msg = new Message();
                        msg.what = MSG_GRP_OPT_GETFROUPLIST;
                        msg.obj = jsonrsp;
                        UIHandler.sendMessage(msg);
                    }
                    else {
                        setStatusText("分屏设置失败:"+code+" reason:"+reason);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                setStatusText("分屏设置失败 JSONException:"+e.getMessage());
                e.printStackTrace();
            }
        }


        @Override //用于处理会议结果返回的提示 
        public void onResponse(int action, String parameters) {
            // TODO Auto-generated method stub
            //parameters 为请求和返回的json
            Utils.PrintLog(5, "DemoApp GroupCallListener", "onResponse action["+action +"]  parameters:"+parameters);
            switch(action) {
                case RtcConst.groupcall_opt_create:
                    onResponse_grpcreate(parameters);
                    break;
                case RtcConst.groupcall_opt_getmemberlist:
                    onResponse_grpgetMemberLis(parameters);
                    break;
                case RtcConst.groupcall_opt_invitedmemberlist:
                    onResponse_grpInvitedMemberList(parameters);
                    break;
                case RtcConst.groupcall_opt_kickedmemberlist:
                    onResponse_grpkickedMemberList(parameters);
                    break;
                case RtcConst.groupcall_opt_close:
                    onResponse_grpClose(parameters);
                    break;
                case RtcConst.groupcall_opt_strm:
                    onResponse_grpStreamManagement(parameters);
                    break;
                case RtcConst.groupcall_opt_join:
                    onResponse_grpJoin(parameters);
                    break;
                case RtcConst.groupcall_opt_qlist:
                    onResponse_grpQList(parameters);
                    break;
            }
            //
        }

        @Override //
        public void onCreate(Connection call) {
            // TODO Auto-generated method stub
            mGroupCall = call;
            mGroupCall.setIncomingListener(mGrpListener);
            Utils.PrintLog(5, LOGTAG, "GroupCallListener onCreate info:"+call.info());
            try {
                JSONObject json = new JSONObject(call.info());
                b_creator = json.getBoolean(RtcConst.kGrpInviter);
                int grptype = json.getInt(RtcConst.kGrpType);
                if(json.has(RtcConst.kGrpname))
                    m_grpname = json.getString(RtcConst.kGrpname);
                calltype = json.getInt(RtcConst.kCallType);
                m_grptype = calltype;
                m_grpid = json.getString(RtcConst.kGrpID);
                Utils.PrintLog(5, LOGTAG, "GroupCallListener onCreate m_grptype:"+m_grptype+" grptype:"+grptype);
                if(b_creator == false) {//非创建者接听选择是否接听
                    Message msg = new Message();
                    msg.what = MSG_GRP_BEI_CREATE;
                    UIHandler.sendMessage(msg);
                    setStatusText("有会议邀请,类型:"+RtcConst.getGrpType(grptype));

                    Utils.PrintLog(5,LOGTAG, "有会议邀请"+RtcConst.getGrpType(grptype)+"grpname:"+m_grpname);
                    //                    mGroupCall.accept(calltype);
                    Utils.PrintLog(5, LOGTAG,"DemoApp GroupCallListener accept"+" calltype:" +calltype);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        @Override
        public void onNotify(String parameters) {
            // TODO Auto-generated method stub
            Utils.PrintLog(5, LOGTAG, "GroupCallListener onNotify");
            try {
        	JSONArray jsonarr = new JSONArray(parameters);
                setStatusText("成员变化:"+jsonarr);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };


    private int grptype;
    
    /**
     * Inits the spinner grp type.
     */
    private void initSpinnerGrpType() {
        sp_grptype = (Spinner)findViewById(R.id.spinner_grptype);
        //将可选内容与ArrayAdapter连接起来
        adapter_grptype = ArrayAdapter.createFromResource(this, R.array.grptype, android.R.layout.simple_spinner_item);
        //设置下拉列表的风格
        adapter_grptype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //设置下拉列表的风格
        //将adapter2 添加到spinner中
        sp_grptype.setAdapter(adapter_grptype);
        //添加事件Spinner事件监听
        sp_grptype.setOnItemSelectedListener(new SpinnerXMLSelectedListener_vformat());
        grptype = 0;
        //设置默认值
        sp_grptype.setVisibility(View.VISIBLE);
    }
    //使用XML形式操作
    /**
     * The Class SpinnerXMLSelectedListener_vformat.
     */
    class SpinnerXMLSelectedListener_vformat implements OnItemSelectedListener{

        /* (non-Javadoc)
         * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
         */
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
            grptype = arg2;
            if(arg2 == 3) grptype = 9;
            else if(arg2>3 && arg2<7) grptype = arg2+16;
            else if(arg2 == 7) grptype = 29;
            Utils.PrintLog(5, LOGTAG,"选择会议类型:"+adapter_grptype.getItem(arg2)+"  grpselect:"+grptype);
        }
        
        /* (non-Javadoc)
         * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
         */
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }
    
    /**
     * On btn grp call_create.
     *
     * @param view the view
     */
    public void onBtnGrpCall_create(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_create 会议类型："+grptype);
        if(mAcc == null) {
            Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_create  mAcc == null");
            return;
        }
        grpmgr = mAcc.getGroup();
        grpmgr.setGroupCallListener(mGrpCallListener);
        //rest;
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(RtcConst.kgvctype, grptype);
            jsonObj.put(RtcConst.kgvcname, "grp组名");
            jsonObj.put(RtcConst.kgvcpassword,"空的"); //opt
            jsonObj.put(RtcConst.kgvcinvitedList, getEditText(R.id.ed_remoteuri));
            //jsonObj.put("codec", 0);
            Utils.PrintLog(5, LOGTAG, "createGroupCallJson:"+jsonObj.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(grpmgr.groupCall(RtcConst.groupcall_opt_create,jsonObj.toString()) == -1)
            setStatusText("重复创建！");
    }
    
    /**
     * On btn grp call_ accept.
     *
     * @param view the view
     */
    public void onBtnGrpCall_Accept(View view) {
        setStatusText("接受会议邀请");
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_Accept:会议接听成功");
        mGroupCall.accept(calltype);
        Message msg = new Message();
        msg.what = MSG_GRP_BEI_ACCEPT;
        UIHandler.sendMessage(msg);
    }
    
    /**
     * On btn grp call_ hangup.
     *
     * @param view the view
     */
    public void onBtnGrpCall_Hangup(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_Hangup");
        if(mGroupCall!=null) {
            mGroupCall.disconnect();
            mGroupCall = null;
            setStatusText("退出会议");
        }
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_Hangup 退出会议");
        Message msg = new Message();
        msg.what = MSG_GRP_HANGUP;
        UIHandler.sendMessage(msg);
    }
    //获取成员列表
    /**
     * On btn grp call_ g mem list.
     *
     * @param view the view
     */
    public void onBtnGrpCall_GMemList(View view) {
        if(mAcc == null)
            return;
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_GMemList");
        GroupMgr grpmgr = mAcc.getGroup();
        if(grpmgr==null)
            return;
        if(grpmgr.groupCall(RtcConst.groupcall_opt_getmemberlist, null) == -1)
            setStatusText("没有会议，操作无效");
    }
    //邀请成员列表加入
    /**
     * On btn grp call_ invited member list.
     *
     * @param view the view
     */
    public void onBtnGrpCall_InvitedMemberList(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_InvitedMemberList");
        if(mAcc == null)
            return;
        GroupMgr grpmgr = mAcc.getGroup();
        if(grpmgr==null)
            return;
        String remoteuri = getEditText(R.id.ed_remoteuri);
        if(grpmgr.groupCall(RtcConst.groupcall_opt_invitedmemberlist,remoteuri) == -1) //多人列表取被叫 逗号 间隔 
            setStatusText("没有会议，操作无效");
    }
    
    /**
     * On btn grp call_kick member list.
     *
     * @param view the view
     */
    public void onBtnGrpCall_kickMemberList(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpV_kickMemberList:");
        if(mAcc == null)
            return;
        GroupMgr grpmgr = mAcc.getGroup();
        if(grpmgr==null)
            return;
        String remoteuri = getEditText(R.id.ed_remoteuri);
        //  remoteuri =  remoteuri.replace("@", RtcConst.char_key);
        if(grpmgr.groupCall(RtcConst.groupcall_opt_kickedmemberlist,remoteuri) == -1) //多人列表取被叫 逗号 间隔 
            setStatusText("没有会议，操作无效");
    }

    //关闭群组
    /**
     * On btn grp call_ close.
     *
     * @param view the view
     */
    public void onBtnGrpCall_Close(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpV_CloseGrpv:");
        if(mAcc == null)
            return;
        GroupMgr grpmgr = mAcc.getGroup();
        if(grpmgr==null)
            return;
        if(grpmgr.groupCall(RtcConst.groupcall_opt_close,null) == -1) //多人列表取被叫 逗号 间隔 
            setStatusText("没有会议，操作无效");
    }

    //禁止群组成员发言/解除群组成员禁言(给麦/收回麦克)
    /**
     * On stream management.
     *
     * @param type the type
     */
    private void OnStreamManagement(int type) {
        Utils.PrintLog(5, LOGTAG, "OnStreamManagement:");
        if(mAcc == null)
            return;
        GroupMgr grpmgr = mAcc.getGroup();
        if(grpmgr==null)
            return;
        String[] memList = getEditText(R.id.ed_remoteuri).split(",");

        try {
            JSONArray jsonArr = new JSONArray();
            for(int i=0; i<memList.length; i++) {
                JSONObject jsonObj1 = new JSONObject();
                jsonObj1.put(RtcConst.kGrpMember, memList[i]);
                switch(type) {
                    case 0:
                        jsonObj1.put(RtcConst.kGrpUpOpType, 0+2*(mCT-1));
                        jsonObj1.put(RtcConst.kGrpDownOpType, 0+2*(mCT-1));
                        break;
                    case 1:
                        jsonObj1.put(RtcConst.kGrpUpOpType, 0+2*(mCT-1));
                        jsonObj1.put(RtcConst.kGrpDownOpType, 1+2*(mCT-1));
                        break;
                    case 2:
                        jsonObj1.put(RtcConst.kGrpUpOpType, 1+2*(mCT-1));
                        jsonObj1.put(RtcConst.kGrpDownOpType, 0+2*(mCT-1));
                        break;
                    case 3:
                        jsonObj1.put(RtcConst.kGrpUpOpType, 1+2*(mCT-1));
                        jsonObj1.put(RtcConst.kGrpDownOpType, 1+2*(mCT-1));
                        break;
                }

                jsonArr.put(i, jsonObj1);
            }
            if(grpmgr.groupCall(RtcConst.groupcall_opt_strm,jsonArr.toString()) == -1)
                setStatusText("没有会议，操作无效");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * On btn grp call_ mute.
     *
     * @param view the view
     */
    public void onBtnGrpCall_MuteFl(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_MuteFl:");
        OnStreamManagement(0);
    }
    
    /**
     * On btn grp call_ un mute.
     *
     * @param view the view
     */
    public void onBtnGrpCall_MuteUnFl(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_MuteUnFl:");
        OnStreamManagement(1);
    }
    
    public void onBtnGrpCall_FlUnMute(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_FlUnMute:");
        OnStreamManagement(2);
    }
    
    /**
     * @param view the view
     */
    public void onBtnGrpCall_UnMuteUnFl(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_UnMuteUnFl:");
        OnStreamManagement(3);
    }
    
    /**
     * On btn grp call_join.
     *
     * @param view the view
     */
    public void onBtnGrpCall_join(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_join 会议类型："+grptype);
        if(mAcc == null) {
            Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_join  mAcc == null");
            return;
        }
        grpmgr = mAcc.getGroup();
        grpmgr.setGroupCallListener(mGrpCallListener);

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(RtcConst.kGrpID, m_grpid); //yes
            jsonObj.put(RtcConst.kgvccreator, b_creator); //yes
            jsonObj.put(RtcConst.kgvcpassword, "空的"); //opt
            Utils.PrintLog(5, LOGTAG, "createGroupJoinJson:"+jsonObj.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(grpmgr.groupCall(RtcConst.groupcall_opt_join,jsonObj.toString()) == -1)
            setStatusText("重复加入！");
    }

    /**
     * On btn grp call_ q list.
     *
     * @param view the view
     */
    public void onBtnGrpCall_QList(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_QList:");
        if(mAcc == null)
            return;
        GroupMgr grpmgr = mAcc.getGroup();
        if(grpmgr==null)
            return;
        grpmgr.groupCall(RtcConst.groupcall_opt_qlist, null);
    }

    /**
     * On btn grp call_ q list.
     *
     * @param view the view
     */
    public void onBtnGrpCall_MDisp(View view) {
        Utils.PrintLog(5, LOGTAG, "onBtnGrpCall_MDisp:");
        if(mAcc == null)
            return;
        GroupMgr grpmgr = mAcc.getGroup();
        if(grpmgr==null)
            return;
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("screenSplit", 5);
            //jsonObj.put("memberList", getEditText(R.id.ed_remoteuri));
            //jsonObj.put("LV", 0);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Gets the app version name.
     *
     * @return the app version name
     */
    public String getAppVersionName() {
        String version = "1.0.0";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

}

