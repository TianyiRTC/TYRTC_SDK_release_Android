/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\work\\ctrtc_mobile\\Release\\RtcSdk_release\\src\\rtc\\sdk\\aidl\\CallNotify.aidl
 */
package rtc.sdk.aidl;
// 对呼叫需要实现的接口

public interface CallNotify extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements rtc.sdk.aidl.CallNotify
{
private static final java.lang.String DESCRIPTOR = "rtc.sdk.aidl.CallNotify";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an rtc.sdk.aidl.CallNotify interface,
 * generating a proxy if needed.
 */
public static rtc.sdk.aidl.CallNotify asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof rtc.sdk.aidl.CallNotify))) {
return ((rtc.sdk.aidl.CallNotify)iin);
}
return new rtc.sdk.aidl.CallNotify.Stub.Proxy(obj);
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
case TRANSACTION_onRing:
{
data.enforceInterface(DESCRIPTOR);
this.onRing();
reply.writeNoException();
return true;
}
case TRANSACTION_onAccept:
{
data.enforceInterface(DESCRIPTOR);
this.onAccept();
reply.writeNoException();
return true;
}
case TRANSACTION_onClose:
{
data.enforceInterface(DESCRIPTOR);
this.onClose();
reply.writeNoException();
return true;
}
case TRANSACTION_onFail:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onFail(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements rtc.sdk.aidl.CallNotify
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
// 对方振铃 

@Override public void onRing() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onRing, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 对方接听

@Override public void onAccept() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onAccept, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 对方挂断/取消/拒接

@Override public void onClose() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onClose, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
// 呼叫失败

@Override public void onFail(int nStatus) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(nStatus);
mRemote.transact(Stub.TRANSACTION_onFail, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onRing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onAccept = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onClose = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onFail = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
// 对方振铃 

public void onRing() throws android.os.RemoteException;
// 对方接听

public void onAccept() throws android.os.RemoteException;
// 对方挂断/取消/拒接

public void onClose() throws android.os.RemoteException;
// 呼叫失败

public void onFail(int nStatus) throws android.os.RemoteException;
}
