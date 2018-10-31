package com.example.loska.seatfinder;

import android.content.Context;

public class SeatFinderUtils {
    public static String genSeatFloor(Context ctx, int seat) {
        return ctx.getString(R.string.seat) + " " + (seat%1000) + " " +
                ctx.getString(R.string.floor) + " " + (seat/1000);
    }

    public static int getFloor(Context ctx, int seat) {
        return (seat/1000);
    }

    public static int getSeat(Context ctx, int seat) {
        return (seat%1000);
    }
}
