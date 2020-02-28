package com.example.blinkstory.model.entity;


import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Parcelable {
    @Json(name = "idCategory")
    private int idCategory;
    @Json(name = "name")
    private String name;
    @Json(name = "urlCategory")
    private String urlCategory;

    protected Category(Parcel in) {
        idCategory = in.readInt();
        name = in.readString();
        urlCategory = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idCategory);
        parcel.writeString(name);
        parcel.writeString(urlCategory);
    }
}
