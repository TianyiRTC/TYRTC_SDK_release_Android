/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\zzc\\qq\\494566081\\FileRecv\\RtcSdk_release\\src\\rtc\\sdk\\aidl\\SdkEnv.aidl
 */
package rtc.sdk.aidl;
// SDK Environment

public interface SdkEnv extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements rtc.sdk.aidl.SdkEnv
{
private static final java.lang.String DESCRIPTOR = "rtc.sdk.aidl.SdkEnv";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an rtc.sdk.aidl.SdkEnv interface,
 * generating a proxy if needed.
 */
public static rtc.sdk.aidl.SdkEnv asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof rtc.sdk.aidl.SdkEnv))) {
return ((rtc.sdk.aidl.SdkEnv)iin);
}
return new rtc.sdk.aidl.SdkEnv.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_createAccount:
{
data.enforceInterface(DESCRIPTOR);
rtc.sdk.aidl.SdkAcc _result = this.createAccount();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements rtc.sdk.aidl.SdkEnv
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
// 创建一个新帐户对象

@Override public rtc.sdk.aidl.SdkAcc createAccount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
rtc.sdk.aidl.SdkAcc _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_createAccount, _data, _reply, 0);
_reply.readException();
_result = rtc.sdk.aidl.SdkAcc.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_createAccount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
// 创建一个新帐户对象

public rtc.sdk.aidl.SdkAcc createAccount() throws android.os.RemoteException;
}
