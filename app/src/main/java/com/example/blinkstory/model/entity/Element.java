package com.example.blinkstory.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Element implements Parcelable {
    @Json(name = "Id")
    private String id ;
    @Json(name = "Caption")
    private String caption ;
    @Json(name = "Url")
    private String url ;
    @Json(name = "TypeData")
    private int typeData ;
    @Json(name = "SrcThumbail")
    private String srcThumbail ;

    protected Element(Parcel in) {
        id = in.readString();
        caption = in.readString();
        url = in.readString();
        typeData = in.readInt();
        srcThumbail = in.readString();
    }

    public static final Creator<Element> CREATOR = new Creator<Element>() {
        @Override
        public Element createFromParcel(Parcel in) {
            return new Element(in);
        }

        @Override
        public Element[] newArray(int size) {
            return new Element[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(caption);
        parcel.writeString(url);
        parcel.writeInt(typeData);
        parcel.writeString(srcThumbail);
    }
}
