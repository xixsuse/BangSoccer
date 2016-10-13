package upgrade.ntv.bangsoccer.booking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Jose Rivera on 10/2/2016.
 */
public class MyDialogFragment extends DialogFragment {

    private String message;


    public MyDialogFragment(){

    }
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        message = getArguments().getString("msj_key");
        builder.setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog diag=builder.create();
        return diag;
    }

    public static MyDialogFragment NewInstance(String msj){

        MyDialogFragment dialog = new MyDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString("msj_key",msj);
       dialog.setArguments(bundle);

        return dialog;
    }
}
