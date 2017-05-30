package com.xdot.classroom;

import android.content.Context;
import android.widget.Toast;



public class CommonFunctionalities {

        public static void displayShortToast(String message, Context context) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

}
