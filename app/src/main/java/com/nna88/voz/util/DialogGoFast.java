package com.nna88.voz.util;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager.LayoutParams;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import com.nna88.voz.main.R;

public class DialogGoFast {
    public Button Cancle;
    public Button OK;
    public Dialog dialog;
    private AutoCompleteTextView edit1;
    public TextView textTitle;

    public DialogGoFast(Context context, String str) {
        this.dialog = new Dialog(context);
        this.dialog.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.go_address);
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.copyFrom(this.dialog.getWindow().getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -2;
        this.dialog.getWindow().setAttributes(layoutParams);
        this.edit1 = (AutoCompleteTextView) this.dialog.findViewById(R.id.alert_edit1);
        this.textTitle = (TextView) this.dialog.findViewById(R.id.alert_title);
        this.textTitle.setText(str);
        this.OK = (Button) this.dialog.findViewById(R.id.alert_ok);
        this.Cancle = (Button) this.dialog.findViewById(R.id.alert_cancle);
        show();
    }

    public void hide() {
        if (this.dialog.isShowing()) {
            this.dialog.hide();
        }
    }

    public void show() {
        if (!this.dialog.isShowing()) {
            this.dialog.show();
        }
    }

    public String text() {
        return this.edit1.getText().toString();
    }
}
