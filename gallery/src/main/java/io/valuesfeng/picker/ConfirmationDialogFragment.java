/*
 * Copyright (C) 2014 nohana, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.valuesfeng.picker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import io.valuesfeng.picker.utils.BundleUtils;


/**
 * @hide
 */
public class ConfirmationDialogFragment extends DialogFragment {
    public static final String TAG = ConfirmationDialogFragment.class.getSimpleName();
    private static final String ARGS_TITLE = BundleUtils.buildKey(ConfirmationDialogFragment.class, "ARGS_TITLE");
    private static final String ARGS_MESSAGE = BundleUtils.buildKey(ConfirmationDialogFragment.class, "ARGS_MESSAGE");
    private ConfirmationSelectionListener mListener;

    public ConfirmationDialogFragment() {}

    public static ConfirmationDialogFragment newInstance(int title, int message) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_TITLE, title);
        args.putInt(ARGS_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (ConfirmationSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("the host activity should implement ConfirmationSelectionListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(ARGS_TITLE);
        int message = getArguments().getInt(ARGS_MESSAGE);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onPositive();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onNegative();
                        dialog.dismiss();
                    }
                });
        return dialog.create();
    }

    public static interface ConfirmationSelectionListener {
        public void onPositive();
        public void onNegative();
    }
}
