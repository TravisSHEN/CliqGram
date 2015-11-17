package cliq.com.cliqgram.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by litaoshen on 17/11/2015.
 */
public class PermissionUtil {

    private static final String FRAGMENT_DIALOG = "dialog";

    private static final int REQUEST_PERMISSION = 1;

    /**
     * request camera permission if neccessary
     * @param activity
     */
    public static void requestCameraPermission(AppCompatActivity activity,
                                               String permission_content,
                                               String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest
                .permission.CAMERA)) {

            ConfirmationDialog.newInstance(permission_content, permission)
                    .show(activity.getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest
                            .permission
                            .CAMERA},
                    REQUEST_PERMISSION);
        }
    }

    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static class ConfirmationDialog extends DialogFragment {

        /**
         * the message showed in dialog
         */
        private static String ARG_PERMISSION_CONTENT = "permission_content";
        /**
         * the permission requested
         */
        private static String ARG_PERMISSION = "permission";

        /**
         * generate instance of ConfirmationDialog
         * @param permission_content
         * @param permission
         * @return
         */
        public static ConfirmationDialog newInstance(String permission_content,
                                                     String permission) {

            Bundle args = new Bundle();
            args.putString(ARG_PERMISSION_CONTENT, permission_content);
            args.putString(ARG_PERMISSION, permission);


            ConfirmationDialog fragment = new ConfirmationDialog();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            Bundle args = this.getArguments();
            final String permission_content = args.getString
                    (ARG_PERMISSION_CONTENT, "The application requires " +
                            "permission to run.");
            final String permission = args.getString(ARG_PERMISSION, "");

            final Activity activity = getActivity();
            return new AlertDialog.Builder(getActivity())
                    .setMessage( permission_content )
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{permission},
                                    REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Activity activity = parent.getActivity();
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                }
                            })
                    .create();
        }
    }

}
