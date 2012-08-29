package com.wbg.petropad;

import android.content.Context;

public class PetropadValidationException extends Exception {
    public final int resId;
    public final Object[] args;

    public String getLocalizedMessage(Context context) {
        return context.getString(resId, args);
    }

    public PetropadValidationException(int resId, Object... args) {
        this.resId = resId;
        this.args = args;
    }

    public PetropadValidationException(int resId) {
        this.resId = resId;
        this.args = new Object[0];
    }
}
