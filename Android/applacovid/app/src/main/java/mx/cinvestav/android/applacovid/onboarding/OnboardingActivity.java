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

import android.os.Bundle;

import org.dpppt.android.sdk.DP3T;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import mx.cinvestav.android.applacovid.R;


public class OnboardingActivity extends FragmentActivity {

	private ViewPager2 viewPager;
	private FragmentStateAdapter pagerAdapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_onboarding);

		viewPager = findViewById(R.id.pager);
		viewPager.setUserInputEnabled(false);
		pagerAdapter = new OnboardingSlidePageAdapter(this);
		viewPager.setAdapter(pagerAdapter);
	}

	public void continueToNextPage() {
		int currentItem = viewPager.getCurrentItem();
		if (currentItem < pagerAdapter.getItemCount() - 1) {
			viewPager.setCurrentItem(currentItem + 1, true);
		} else {
			DP3T.start(this);
			setResult(RESULT_OK);
			finish();
		}
	}
}
