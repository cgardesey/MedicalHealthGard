package com.prepeez.medicalhealthguard.util;

import android.content.Context;

public class SimpleItemDecoratorImpl extends SimpleItemDecorator {
    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public SimpleItemDecoratorImpl(Context context, int orientation) {
        super(context, orientation);
    }
}
