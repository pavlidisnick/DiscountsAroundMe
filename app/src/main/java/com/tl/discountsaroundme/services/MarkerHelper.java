package com.tl.discountsaroundme.services;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.tl.discountsaroundme.R;


public class MarkerHelper {

    public Drawable getDrawableByType(Fragment fragment, String code) {
        int i = -1;
        int id;
        for (String cc : fragment.getResources().getStringArray(R.array.codes)) {
            i++;
            if (cc.equals(code))
                break;
        }
        try {
            String resource = fragment.getResources().getStringArray(R.array.names)[i];
            id = fragment.getResources().getIdentifier(resource, "drawable", fragment.getActivity().getPackageName());
            return fragment.getResources().getDrawable(id);
        } catch (Exception e) {
            return fragment.getResources().getDrawable(R.drawable.marker_shop);
        }
    }

    public BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
