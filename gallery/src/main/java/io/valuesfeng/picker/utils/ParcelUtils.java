package io.valuesfeng.picker.utils;

import android.os.Parcel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        dest.writeInt(bool ? 1 : 0);
    }

    public static boolean readBoolean(Parcel source) {
        return source.readInt() == 1;
    }

    public static void writeDate(Parcel parcel, Date date) {
        if (parcel != null) {
            parcel.writeByte((byte) (date == null ? 0 : 1));
            if (date != null) {
                parcel.writeLong(date.getTime());
            }
        }
    }

    public static Date readDate(Parcel parcel) {
        byte isADateStored = parcel.readByte();
        return isADateStored == 1 ? new Date(parcel.readLong()) : null;
    }

    public static java.lang.Object byteToObject(byte[] bytes) {
        java.lang.Object obj = null;
        try {
            //bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);
            obj = oi.readObject();

            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    public byte[] ObjectToByte(java.lang.Object obj)
    {
        byte[] bytes = null;
        try {
            //object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        }
        catch(Exception e) {
            System.out.println("translation"+e.getMessage());
            e.printStackTrace();
        }
        return(bytes);
    }
}
