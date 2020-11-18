/*
 * Copyright (c) 2020 Ubique Innovation AG <https://www.ubique.ch>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * SPDX-License-Identifier: MPL-2.0
 */
package mx.cinvestav.android.applacovid.inform;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import mx.cinvestav.android.applacovid.MainActivity;
import mx.cinvestav.android.applacovid.R;

public class GetWellFragment extends Fragment {

	public static GetWellFragment newInstance() {
		return new GetWellFragment();
	}

	public GetWellFragment() {
		super( R.layout.fragment_get_well);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		((InformActivity) requireActivity()).allowBackButton(false);

		view.findViewById(R.id.inform_get_well_button_continue).setOnClickListener(v -> {
			Intent intent = new Intent(getActivity(), MainActivity.class);
			intent.setAction(MainActivity.ACTION_STOP_TRACING);
			startActivity(intent);
			getActivity().finish();
		});
	}

}
