package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FinanceTransaction implements Parcelable {
    private String category;
    private String description;
    private double price;
    private int id;
//    private Date date;

    public FinanceTransaction(String cat, String desc, double price, int id) {
        this.category = cat;
        this.description = desc;
        this.price = price;
        this.id = id;
    }

    public FinanceTransaction(){}

    protected FinanceTransaction(Parcel in) {
        category = in.readString();
        description = in.readString();
        price = in.readDouble();
        id = in.readInt();
    }

    public static final Creator<FinanceTransaction> CREATOR = new Creator<FinanceTransaction>() {
        @Override
        public FinanceTransaction createFromParcel(Parcel in) {
            return new FinanceTransaction(in);
        }

        @Override
        public FinanceTransaction[] newArray(int size) {
            return new FinanceTransaction[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeString(description);
        parcel.writeDouble(price);
        parcel.writeInt(id);
    }
}
