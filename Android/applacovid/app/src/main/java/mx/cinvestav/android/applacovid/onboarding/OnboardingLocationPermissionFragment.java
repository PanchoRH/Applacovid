/*
 * Copyright (c) 2020 Ubique Innovation AG <https://www.ubique.ch>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * SPDX-License-Identifier: MPL-2.0
 */
package mx.cinvestav.android.applacovid.onboarding;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import mx.cinvestav.android.applacovid.R;
import mx.cinvestav.android.applacovid.onboarding.util.PermissionButtonUtil;
import mx.cinvestav.android.applacovid.util.DeviceFeatureHelper;

public class OnboardingLocationPermissionFragment extends Fragment {

	public static final int REQUEST_CODE_ASK_PERMISSION_FINE_LOCATION = 123;

	private Button locationButton;
	private Button continueButton;

	public static OnboardingLocationPermissionFragment newInstance() {
		return new OnboardingLocationPermissionFragment();
	}

	public OnboardingLocationPermissionFragment() {
		super( R.layout.fragment_onboarding_permission_location);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		locationButton = view.findViewById(R.id.onboarding_location_permission_button);
		locationButton.setOnClickListener(v -> {
			String[] permissions = new String[] { Manifest.permission.ACCESS_FINE_LOCATION};
			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
				permissions = new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION }; // TODO: (gusorh) Test this code. Modified in order to support BACKGROUND LOCATION ACCESS PERMISSION for Android 10+. A bug was reported: system suddenly stops tracking in background

			requestPermissions(permissions, REQUEST_CODE_ASK_PERMISSION_FINE_LOCATION);
		});
		continueButton = view.findViewById(R.id.onboarding_location_permission_continue_button);
		continueButton.setOnClickListener(v -> {
			((OnboardingActivity) getActivity()).continueToNextPage();
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		updateFragmentState();
	}

	private void updateFragmentState() {
		boolean locationPermissionGranted = DeviceFeatureHelper.isLocationPermissionGranted(requireContext());
		if (locationPermissionGranted) {
			PermissionButtonUtil.setButtonOk(locationButton, R.string.android_onboarding_bt_permission_button_allowed);
		} else {
			PermissionButtonUtil.setButtonDefault(locationButton, R.string.android_onboarding_bt_permission_button);
		}
		continueButton.setVisibility(locationPermissionGranted ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		// TODO: (gusorh) Test this code. Modified in order to support BACKGROUND LOCATION ACCESS PERMISSION for Android 10+. A bug was reported: system suddenly stops tracking in background
		if (requestCode == REQUEST_CODE_ASK_PERMISSION_FINE_LOCATION) {
			boolean anyPermissionWasDenied=false;
			for(int gr : grantResults) if(gr != PackageManager.PERMISSION_GRANTED ) anyPermissionWasDenied = true;
			if (anyPermissionWasDenied) {
				if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
					new AlertDialog.Builder(requireActivity())
							.setTitle(R.string.android_button_permission_location)
							.setMessage(R.string.android_foreground_service_notification_error_location_permission)
							.setPositiveButton(getString(R.string.android_button_ok),
									(dialogInterface, i) -> {
										DeviceFeatureHelper.openApplicationSettings(requireActivity());
										dialogInterface.dismiss();
									})
							.create()
							.show();
				}
			}
			((OnboardingActivity) getActivity()).continueToNextPage();
		}
	}

}