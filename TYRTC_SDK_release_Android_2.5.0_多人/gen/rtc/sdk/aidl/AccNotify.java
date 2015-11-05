/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\zzc\\qq\\494566081\\FileRecv\\RtcSdk_release\\src\\rtc\\sdk\\aidl\\AccNotify.aidl
 */
package rtc.sdk.aidl;
// 对帐户需要实现的接口

public interface AccNotify extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements rtc.sdk.aidl.AccNotify
{
private static final java.lang.String DESCRIPTOR = "rtc.sdk.aidl.AccNotify";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an rtc.sdk.aidl.AccNotify interface,
 * generating a proxy if needed.
 */
public static rtc.sdk.aidl.AccNotify asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof rtc.sdk.aidl.AccNotify))) {
return ((rtc.sdk.aidl.AccNotify)iin);
}
return new rtc.sdk.aidl.AccNotify.Stub.Proxy(obj);
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
case TRANSACTION_onRegister:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
this.onRegister(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_onNewCall:
{
data.enforceInterface(DESCRIPTOR);
rtc.sdk.aidl.SdkCall _arg0;
_arg0 = rtc.sdk.aidl.SdkCall.Stub.asInterface(data.readStrongBinder());
this.onNewCall(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onRspGroupCall:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
this.onRspGroupCall(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onGroupCall:
{
data.enforceInterface(DESCRIPTOR);
rtc.sdk.aidl.SdkCall _arg0;
_arg0 = rtc.sdk.aidl.SdkCall.Stub.asInterface(data.readStrongBinder());
this.onGroupCall(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onNtyGroupCall:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onNtyGroupCall(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements rtc.sdk.aidl.AccNotify
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
// 注册结果通知

@Override public void onRegister(int nStatus, java.lang.String desp, int expire) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(nStatus);
_data.writeString(desp);
_data.writeInt(expire);
mRemote.transact(Stub.TRANSACTION_onRegister, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 来电通知

@Override public void onNewCall(rtc.sdk.aidl.SdkCall call) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((call!=null))?(call.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_onNewCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 群组

@Override public void onRspGroupCall(int action, java.lang.String parameters) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(action);
_data.writeString(parameters);
mRemote.transact(Stub.TRANSACTION_onRspGroupCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onGroupCall(rtc.sdk.aidl.SdkCall call) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((call!=null))?(call.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_onGroupCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onNtyGroupCall(java.lang.String parameters) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(parameters);
mRemote.transact(Stub.TRANSACTION_onNtyGroupCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onRegister = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onNewCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onRspGroupCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onGroupCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onNtyGroupCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
// 注册结果通知

public void onRegister(int nStatus, java.lang.String desp, int expire) throws android.os.RemoteException;
// 来电通知

public void onNewCall(rtc.sdk.aidl.SdkCall call) throws android.os.RemoteException;
// 群组

public void onRspGroupCall(int action, java.lang.String parameters) throws android.os.RemoteException;
public void onGroupCall(rtc.sdk.aidl.SdkCall call) throws android.os.RemoteException;
public void onNtyGroupCall(java.lang.String parameters) throws android.os.RemoteException;
}
