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

    /**
     * request camera permission if neccessary
     * @param activity
     */
    public static void requestPermission(AppCompatActivity activity,
                                               String permission_info,
                                               String permission,
                                         int request_code) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                permission)) {

            ConfirmationDialog.newInstance(permission_info, permission, request_code)
                    .show(activity.getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest
                            .permission
                            .CAMERA},
                    request_code);
        }
    }

    /**
     * Shows OK/Cancel confirmation dialog about permission.
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
         * request code
         */
        private static String ARG_REQUEST_CODE = "request_code";

        /**
         * generate instance of ConfirmationDialog
         * @param permission_content
         * @param permission
         * @return
         */
        public static ConfirmationDialog newInstance(String permission_content,
                                                     String permission,
                                                     int request_code) {

            Bundle args = new Bundle();
            args.putString(ARG_PERMISSION_CONTENT, permission_content);
            args.putString(ARG_PERMISSION, permission);
            args.putInt(ARG_REQUEST_CODE, request_code);


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
            final int request_code = args.getInt(ARG_REQUEST_CODE, 1);

            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage( permission_content )
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{permission},
                                    request_code);
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
    /**
     * Shows an error message dialog.
     */
    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }
}
