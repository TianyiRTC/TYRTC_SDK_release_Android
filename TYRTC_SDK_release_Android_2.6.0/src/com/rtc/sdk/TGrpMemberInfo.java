package com.rtc.sdk;

import java.io.Serializable;

public class TGrpMemberInfo implements Serializable{

    private String userid;//帐号
    private int memberStatus ;//群组成员当前状态
//    1代表准备状态（主席正在振铃）
//    2代表已加入会议
//    3代表未加入会议
//    4代表被删除出会议（被删除出会议的成员不能再加入此会议）
//    5代表振铃状态（成员振铃）
    private String startTime;//加入会议时间
    private int duration;//通话时长，以秒为单位
    private int upAudioState;//终端上行音频状态：0：关闭1：开启
    private int upVideoState; //上行视频状体：0：关闭 1： 开启
    private int downAudioState;//下行音频状态：0：关闭1：开启
    private int downVideoState; //下行视频状体：0：关闭 1： 开启
    private int role; //成员在此会议身份：0：普通参会者 1：主持人 
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public int getMemberStatus() {
        return memberStatus;
    }
    public void setMemberStatus(int memberStatus) {
        this.memberStatus = memberStatus;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public int getupAudioState() {
        return upAudioState;
    }
    public void setupAudioState(int upAudioState) {
        this.upAudioState = upAudioState;
    }
    public int getdownAudioState() {
        return downAudioState;
    }
    public void setdownAudioState(int downAudioState) {
        this.downAudioState = downAudioState;
    }
    public int getupVideoState() {
        return upVideoState;
    }
    public void setupVideoState(int upVideoState) {
        this.upVideoState = upVideoState;
    }
    public int getdownVideoState() {
        return downVideoState;
    }
    public void setdownVideoState(int downVideoState) {
        this.downVideoState = downVideoState;
    }
    public int getRole() {
        return role;
    }
    public void setRole(int role) {
        this.role = role;
    }

}
