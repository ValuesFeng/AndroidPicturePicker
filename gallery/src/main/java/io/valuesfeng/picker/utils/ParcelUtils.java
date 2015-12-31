package io.valuesfeng.picker.utils;

import android.os.Parcel;

import java.util.Date;

/**
 */
public class ParcelUtils {
    private static final int FALSE = 0;
    private static final int TRUE = 1;
    private static final byte MARKER_NO_ELEMENT_STORED = 0;
    private static final byte MARKER_AN_ELEMENT_STORED = 1;

    private ParcelUtils() {
    }

    public static void writeBoolean(Parcel dest, boolean bool) {
        dest.writeInt(bool?1:0);
    }

    public static boolean readBoolean(Parcel source) {
        return source.readInt() == 1;
    }

    public static void writeDate(Parcel parcel, Date date) {
        if(parcel != null) {
            parcel.writeByte((byte)(date == null?0:1));
            if(date != null) {
                parcel.writeLong(date.getTime());
            }

        }
    }

    public static Date readDate(Parcel parcel) {
        byte isADateStored = parcel.readByte();
        return isADateStored == 1?new Date(parcel.readLong()):null;
    }
}
