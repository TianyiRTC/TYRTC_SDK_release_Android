/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\work\\ctrtc_mobile\\Release\\RtcSdk_release\\src\\rtc\\sdk\\aidl\\SdkAcc.aidl
 */
package rtc.sdk.aidl;
public interface SdkAcc extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements rtc.sdk.aidl.SdkAcc
{
private static final java.lang.String DESCRIPTOR = "rtc.sdk.aidl.SdkAcc";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an rtc.sdk.aidl.SdkAcc interface,
 * generating a proxy if needed.
 */
public static rtc.sdk.aidl.SdkAcc asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof rtc.sdk.aidl.SdkAcc))) {
return ((rtc.sdk.aidl.SdkAcc)iin);
}
return new rtc.sdk.aidl.SdkAcc.Stub.Proxy(obj);
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
rtc.sdk.aidl.AccNotify _arg0;
_arg0 = rtc.sdk.aidl.AccNotify.Stub.asInterface(data.readStrongBinder());
this.setNotify(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_registerFull:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.registerFull(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_release:
{
data.enforceInterface(DESCRIPTOR);
this.release();
reply.writeNoException();
return true;
}
case TRANSACTION_newCall:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
rtc.sdk.aidl.SdkCall _result = this.newCall(_arg0);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_hangupAll:
{
data.enforceInterface(DESCRIPTOR);
this.hangupAll();
reply.writeNoException();
return true;
}
case TRANSACTION_optGroupCall:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
this.optGroupCall(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements rtc.sdk.aidl.SdkAcc
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

@Override public void setNotify(rtc.sdk.aidl.AccNotify acc) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((acc!=null))?(acc.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setNotify, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 扩充参数注册

@Override public boolean registerFull(java.lang.String cfg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(cfg);
mRemote.transact(Stub.TRANSACTION_registerFull, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// 释放用户对象

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
// 创建新呼叫

@Override public rtc.sdk.aidl.SdkCall newCall(java.lang.String cfg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
rtc.sdk.aidl.SdkCall _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(cfg);
mRemote.transact(Stub.TRANSACTION_newCall, _data, _reply, 0);
_reply.readException();
_result = rtc.sdk.aidl.SdkCall.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
// 断开所有呼叫

@Override public void hangupAll() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_hangupAll, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 操作群组

@Override public void optGroupCall(int action, java.lang.String parameters) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(action);
_data.writeString(parameters);
mRemote.transact(Stub.TRANSACTION_optGroupCall, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_setNotify = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_registerFull = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_release = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_newCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_hangupAll = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_optGroupCall = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
// 设置通知对象

public void setNotify(rtc.sdk.aidl.AccNotify acc) throws android.os.RemoteException;
// 扩充参数注册

public boolean registerFull(java.lang.String cfg) throws android.os.RemoteException;
// 释放用户对象

public void release() throws android.os.RemoteException;
// 创建新呼叫

public rtc.sdk.aidl.SdkCall newCall(java.lang.String cfg) throws android.os.RemoteException;
// 断开所有呼叫

public void hangupAll() throws android.os.RemoteException;
// 操作群组

public void optGroupCall(int action, java.lang.String parameters) throws android.os.RemoteException;
}
