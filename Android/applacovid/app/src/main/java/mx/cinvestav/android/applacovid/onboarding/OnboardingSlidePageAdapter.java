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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import mx.cinvestav.android.applacovid.R;


public class OnboardingSlidePageAdapter extends FragmentStateAdapter {

	public OnboardingSlidePageAdapter(FragmentActivity fragmentActivity) {
		super(fragmentActivity);
	}

	@NonNull
	@Override
	public Fragment createFragment(int position) {
		switch (position) {
			case 0:
				return OnboardingContentFragment.newInstance(
						R.string.onboarding_prinzip_title,
						R.string.onboarding_prinzip_heading,
						R.drawable.ill_prinzip,
						R.string.onboarding_prinzip_text1,
						R.drawable.ic_begegnungen,
						R.string.onboarding_prinzip_text2,
						R.drawable.ic_message_alert,
						R.string.onboarding_prinzip_text2,  // not showed because last argument is 2
						R.drawable.ic_message_alert,  		// not showed because last argument is 2
						false,2);
			case 1:
				return OnboardingContentFragment.newInstance(
						R.string.onboarding_privacy_title,
						R.string.onboarding_privacy_heading,
						R.drawable.ill_privacy,
						R.string.onboarding_privacy_text1,
						R.drawable.ic_begegnungen,
						R.string.onboarding_privacy_text2,
						R.drawable.ic_lock,
						R.string.onboarding_privacy_text3,  // It is showed because last argument is 3
						R.drawable.ic_key,					// It is showed because last argument is 3
						false,3);
			case 2:
				return OnboardingContentFragment.newInstance(
						R.string.onboarding_begegnungen_title,
						R.string.onboarding_begegnungen_heading,
						R.drawable.ill_bluetooth,
						R.string.onboarding_begegnungen_text1,
						R.drawable.ic_begegnungen,
						R.string.onboarding_begegnungen_text2,
						R.drawable.ic_bluetooth,
						R.string.onboarding_prinzip_text2, 	// not showed because last argument is 2
						R.drawable.ic_message_alert,		// not showed because last argument is 2
						false,2);
			case 3:
				return OnboardingLocationPermissionFragment.newInstance();
			case 4:
				return OnboardingBatteryPermissionFragment.newInstance();
			case 5:
				return OnboardingContentFragment.newInstance(
						R.string.onboarding_meldung_title,
						R.string.onboarding_meldung_heading,
						R.drawable.ill_meldung,
						R.string.onboarding_meldung_text1,
						R.drawable.ic_message_alert,
						R.string.onboarding_meldung_text2,	// not showed because last argument is 1
						R.drawable.ic_home,					// not showed because last argument is 1
						R.string.onboarding_prinzip_text2,	// not showed because last argument is 1
						R.drawable.ic_message_alert,		// not showed because last argument is 1
						false,1);
			case 6:
				return OnboardingFinishedFragment.newInstance();
		}
		throw new IllegalArgumentException("There is no fragment for view pager position " + position);
	}

	@Override
	public int getItemCount() {
		return 7;
	}

}
