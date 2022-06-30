package Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.kiaan.collect_it.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity myActivity) {

        activity = myActivity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dailog, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog()
    {
        alertDialog.dismiss();
    }
}
