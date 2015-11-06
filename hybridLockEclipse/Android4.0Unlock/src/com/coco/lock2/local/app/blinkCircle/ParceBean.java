package com.coco.lock2.local.app.blinkCircle;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class ParceBean implements Parcelable
{
	private  Bitmap dw;
//    private String name;
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public Bitmap getDw() {
        return dw;
    }

    public void setDw(Bitmap dw) {
        this.dw = dw;
    }
    
	@Override
	public int describeContents()
	{
		return 0;
	}
	
	@Override
	public void writeToParcel(
			Parcel dest ,
			int flags )
	{
//		dest.writeString(name);
        dw.writeToParcel(dest, 0);
	}
	
	public static final Parcelable.Creator<ParceBean> CREATOR = new Creator<ParceBean>() { 
        public ParceBean createFromParcel(Parcel source) { 
            ParceBean pb = new ParceBean(); 
//            pb.name = source.readString(); 
            pb.dw = Bitmap.CREATOR.createFromParcel(source);
            return pb; 
        } 
        public ParceBean[] newArray(int size) { 
            return new ParceBean[size]; 
        } 
    }; 
}
