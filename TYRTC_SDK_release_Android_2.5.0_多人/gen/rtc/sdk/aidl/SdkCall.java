/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\zzc\\qq\\494566081\\FileRecv\\RtcSdk_release\\src\\rtc\\sdk\\aidl\\SdkCall.aidl
 */
package rtc.sdk.aidl;
public interface SdkCall extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements rtc.sdk.aidl.SdkCall
{
private static final java.lang.String DESCRIPTOR = "rtc.sdk.aidl.SdkCall";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an rtc.sdk.aidl.SdkCall interface,
 * generating a proxy if needed.
 */
public static rtc.sdk.aidl.SdkCall asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof rtc.sdk.aidl.SdkCall))) {
return ((rtc.sdk.aidl.SdkCall)iin);
}
return new rtc.sdk.aidl.SdkCall.Stub.Proxy(obj);
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
case TRANSACTION_setNotify:
{
data.enforceInterface(DESCRIPTOR);
rtc.sdk.aidl.CallNotify _arg0;
_arg0 = rtc.sdk.aidl.CallNotify.Stub.asInterface(data.readStrongBinder());
this.setNotify(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_accept:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.accept(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_hangup:
{
data.enforceInterface(DESCRIPTOR);
this.hangup();
reply.writeNoException();
return true;
}
case TRANSACTION_release:
{
data.enforceInterface(DESCRIPTOR);
this.release();
reply.writeNoException();
return true;
}
case TRANSACTION_getInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getInfo();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_sendDTMF:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.sendDTMF(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_muteMic:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.muteMic(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements rtc.sdk.aidl.SdkCall
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
// 设置通知对象

@Override public void setNotify(rtc.sdk.aidl.CallNotify call) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((call!=null))?(call.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setNotify, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 接听 

@Override public int accept(int callType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(callType);
mRemote.transact(Stub.TRANSACTION_accept, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// 挂断

@Override public void hangup() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_hangup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 释放对象

@Override public void release() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_release, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 获取呼叫信息json kCallxxx

@Override public java.lang.String getInfo() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getInfo, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// 发送按键

@Override public void sendDTMF(int nChar) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(nChar);
mRemote.transact(Stub.TRANSACTION_sendDTMF, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 静音mic

@Override public void muteMic(int bMute) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(bMute);
mRemote.transact(Stub.TRANSACTION_muteMic, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_setNotify = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_accept = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_hangup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_release = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_sendDTMF = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_muteMic = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
}
// 设置通知对象

public void setNotify(rtc.sdk.aidl.CallNotify call) throws android.os.RemoteException;
// 接听 

public int accept(int callType) throws android.os.RemoteException;
// 挂断

public void hangup() throws android.os.RemoteException;
// 释放对象

public void release() throws android.os.RemoteException;
// 获取呼叫信息json kCallxxx

public java.lang.String getInfo() throws android.os.RemoteException;
// 发送按键

public void sendDTMF(int nChar) throws android.os.RemoteException;
// 静音mic

public void muteMic(int bMute) throws android.os.RemoteException;
}
