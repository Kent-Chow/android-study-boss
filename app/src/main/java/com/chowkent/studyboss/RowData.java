package com.chowkent.studyboss;

import android.os.Parcel;
import android.os.Parcelable;

class RowData implements Parcelable {
    public String timerValue;
    public boolean isStartButtonEnabled;
    public boolean isPauseButtonEnabled;
    public boolean isResetButtonEnabled;

    RowData(String timerValue) {
        this.timerValue = timerValue;
        isStartButtonEnabled = true;
        isPauseButtonEnabled = false;
        isResetButtonEnabled = true;
    }

    protected RowData(Parcel in) {
        timerValue = in.readString();
        isStartButtonEnabled = in.readByte() != 0;
        isPauseButtonEnabled = in.readByte() != 0;
        isResetButtonEnabled = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(timerValue);
        dest.writeByte((byte) (isStartButtonEnabled ? 1 : 0));
        dest.writeByte((byte) (isPauseButtonEnabled ? 1 : 0));
        dest.writeByte((byte) (isResetButtonEnabled ? 1 : 0));
    }

    public String toString() {
        return timerValue;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RowData> CREATOR = new Parcelable.Creator<RowData>() {
        @Override
        public RowData createFromParcel(Parcel in) {
            return new RowData(in);
        }

        @Override
        public RowData[] newArray(int size) {
            return new RowData[size];
        }
    };
}
