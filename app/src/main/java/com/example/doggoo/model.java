package com.example.doggoo;

import android.os.Parcel;
import android.os.Parcelable;

public class model implements Parcelable {
    private String name;
    private String description;
    private String price;
    private int quantity;  // Changed from 'Quantity' to 'quantity' to follow Java naming conventions
    private byte[] image;


    public model(String name, String description, String price, byte[] image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = 1;  // Set default quantity to 1
    }

    protected model(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readString();
        image = in.createByteArray();
        quantity = in.readInt();  // Read quantity from the parcel
    }

    public static final Creator<model> CREATOR = new Creator<model>() {
        @Override
        public model createFromParcel(Parcel in) {
            return new model(in);
        }

        @Override
        public model[] newArray(int size) {
            return new model[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public byte[] getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(price);
        dest.writeByteArray(image);
        dest.writeInt(quantity);  // Write quantity to the parcel
    }
}
