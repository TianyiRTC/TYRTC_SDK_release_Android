package com.rtc.sdk;


import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

import rtc.sdk.clt.RtcClientImpl;
import rtc.sdk.common.RtcConst;
import rtc.sdk.common.SdkSettings;
import rtc.sdk.core.RtcRules;
import rtc.sdk.iface.ClientListener;
import rtc.sdk.iface.Connection;
import rtc.sdk.iface.ConnectionListener;
import rtc.sdk.iface.Device;
import rtc.sdk.iface.DeviceListener;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    
    /** The Constant MSG_GETTOKEN. */
    public static final int MSG_GETTOKEN=10001;
    
    /** The m clt. */
    RtcClient mClt;
    
    /** The capabailitytoken. */
    private String capabailitytoken;
    
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
        layoutlocal = (LinearLayout) findViewById(R.id.ll_local);
        layoutremote = (LinearLayout) findViewById(R.id.ll_remote);

        tv_version = (TextView)findViewById(R.id.tv_version);
        tv_version.setText(RtcConst.sdkVersion+"; VP8; "+APP_ID);//getAppVersionName()
        updateEditInfo(0xffffffff);
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if(layoutremote!=null) {
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
        mvLocal.setVisibility(View.INVISIBLE);
        layoutlocal.addView(mvLocal);
        mvLocal.setKeepScreenOn(true);
        mvLocal.setZOrderMediaOverlay(true);
        mvLocal.setZOrderOnTop(true);

        if(mCall != null)
            mvRemote = (SurfaceView)mCall.createVideoView(false, this, true);
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
                Utils.PrintLog(5,"ClientListener","onInit,result="+result);//常见错误9003:网络异常或系统时间差的太多
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
    
    /** The m a listener. */
    DeviceListener mAListener = new DeviceListener() {
        @Override
        public void onDeviceStateChanged(int result) {
            Utils.PrintLog(5,"DeviceListener","onDeviceStateChanged,result="+result);
            setStatusText("StateChanged,result="+result);
            if(result == RtcConst.CallCode_Success) { //注销也存在此处

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
            setStatusText("pls check network");
        }
        private void ChangeNetWork() {
            Utils.PrintLog(5, LOGTAG, "ChangeNetWork");
            //自动重连接  
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
        @Override
        public void onSendIm(int status) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onReceiveIm(String from,String mime,String content) {
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

    /** The UI handler. */
    private Handler UIHandler = new Handler() {
        @Override 
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case MSG_GETTOKEN:
                    OnResponse_getToken(msg);
                    break;
                default:
                    break;
            }
        }

    };

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

