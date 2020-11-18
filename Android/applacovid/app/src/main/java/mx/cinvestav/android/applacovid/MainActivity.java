/*
 * Copyright (c) 2020 Ubique Innovation AG <https://www.ubique.ch>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * SPDX-License-Identifier: MPL-2.0
 */
package mx.cinvestav.android.applacovid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import mx.cinvestav.android.applacovid.main.HomeFragment;
import mx.cinvestav.android.applacovid.onboarding.OnboardingActivity;
import mx.cinvestav.android.applacovid.reports.ReportsFragment;
import mx.cinvestav.android.applacovid.storage.SecureStorage;
import mx.cinvestav.android.applacovid.util.InfoDialog;
import mx.cinvestav.android.applacovid.viewmodel.TracingViewModel;


public class MainActivity extends FragmentActivity {

	public static final String ACTION_GOTO_REPORTS = "ACTION_GOTO_REPORTS";
	public static final String ACTION_STOP_TRACING = "ACTION_STOP_TRACING";
	public static final String UUID_FIELD_NAME = "UUID";
	public static final String MY_PREFS_FILE_NAME = "MySharedPrefs";

	private static final int REQ_ONBOARDING = 123;

	private static final String STATE_CONSUMED_EXPOSED_INTENT = "STATE_CONSUMED_EXPOSED_INTENT";
	private boolean consumedExposedIntent;

	private SecureStorage secureStorage;

	private TracingViewModel tracingViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_main);

		secureStorage = SecureStorage.getInstance(this);

		secureStorage.getForceUpdateLiveData().observe(this, forceUpdate -> {
			forceUpdate = forceUpdate && secureStorage.getDoForceUpdate();
			InfoDialog forceUpdateDialog =
					(InfoDialog) getSupportFragmentManager().findFragmentByTag(InfoDialog.class.getCanonicalName());
			if (forceUpdate && forceUpdateDialog == null) {
				forceUpdateDialog = InfoDialog.newInstance(R.string.force_update_text);
				forceUpdateDialog.setCancelable(false);
				forceUpdateDialog.setButtonOnClickListener(v -> {
					String packageName = getPackageName();
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=" + packageName));
					if (intent.resolveActivity(getPackageManager()) != null) {
						startActivity(intent);
					}
				});
				forceUpdateDialog.show(getSupportFragmentManager(), InfoDialog.class.getCanonicalName());
			} else if (!forceUpdate && forceUpdateDialog != null) {
				forceUpdateDialog.dismiss();
			}
		});

		if (savedInstanceState == null) {
			boolean onboardingCompleted = secureStorage.getOnboardingCompleted();
			if (onboardingCompleted) {
				showHomeFragment();
			} else {
				startActivityForResult(new Intent(this, OnboardingActivity.class), REQ_ONBOARDING);
			}
		} else {
			consumedExposedIntent = savedInstanceState.getBoolean(STATE_CONSUMED_EXPOSED_INTENT);
		}


		{ 	// (gusorh) Code for supporting visited places tracking. Backend receives a unique identifier (app instance level)
			SharedPreferences prefs = getSharedPreferences( MY_PREFS_FILE_NAME, MODE_PRIVATE );
			String uuid = prefs.getString( UUID_FIELD_NAME, "" );
			if(uuid.length()==0){
				SharedPreferences.Editor editor = getSharedPreferences( MY_PREFS_FILE_NAME, MODE_PRIVATE ).edit();
				editor.putString( UUID_FIELD_NAME, UUID.randomUUID().toString() );
				editor.apply();
				editor.commit();
			}
		}
		// ** ** ** ** ** ** **

		tracingViewModel = new ViewModelProvider(this).get(TracingViewModel.class);
		{
			// (gusorh) code to support Betty's flow
			SharedPreferences prefs = getSharedPreferences( MY_PREFS_FILE_NAME, MODE_PRIVATE );
			boolean reportedByBetty = prefs.getBoolean( "ReportedByBetty", false );
			if (reportedByBetty) {
				try {
					secureStorage.clearInformTimeAndCodeAndToken();
					tracingViewModel.setTracingEnabled( false );
				} catch (Exception ex) {
					Log.d( "BettyFlow", ex.toString() );
				}
			}
		}
		// *** ** ** **
		tracingViewModel.sync();
	}


	// Experimental code to force Sync and Add Exposed Key
	/*
	public void showSecretKey(View view){
		ExposeeRequest exposeeRequest = CryptoModule.getInstance(this).getSecretKeyForPublishing( new DayDate( Calendar.getInstance().getTime().getTime()), new ExposeeAuthMethodJson("777"));

		if(exposeeRequest != null){
			Toast.makeText(this,exposeeRequest.getKey() + "", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this,"NULL", Toast.LENGTH_LONG).show();
		}

		AppConfigManager appConfigManager = AppConfigManager.getInstance(this);
		try {
			appConfigManager.getBackendReportRepository(this).addExposee(exposeeRequest, new ExposeeAuthMethodJson("777"),
					new ResponseCallback<Void>() {
						@Override
						public void onSuccess(Void response) {
							Toast.makeText(MainActivity.this,"Reported Infected Successfully", Toast.LENGTH_LONG).show();
							try{
								Thread thread = new Thread(new Runnable() {

									@Override
									public void run() {
											try {
												SyncWorker.doSync( MainActivity.this );
												Log.d( "KEY_D", "------- Synced successfully! ----------" );
												Log.d( "KEY_D", "Infection Status: " + DP3T.getStatus( MainActivity.this ).getInfectionStatus() );
												Log.d( "KEY_D", "Number of contacts: " + DP3T.getStatus( MainActivity.this ).getNumberOfContacts() );

												Log.d( "KEY_D", "EXPOSURE DAYS: " + DP3T.getStatus( MainActivity.this ).getNumberOfContacts() );
												for (ExposureDay exD : DP3T.getStatus( MainActivity.this ).getExposureDays()) {
													Log.d( "KEY_D", "Exposed Date: " + exD.getExposedDate() );
													Log.d( "KEY_D", "Report Date: " + exD.getReportDate() );
													Log.d( "KEY_D", "ID: " + exD.getId() );
												}

												Log.d( "KEY_D", "Last sync date: " + new Date( DP3T.getStatus( MainActivity.this ).getLastSyncDate() ) );
												Log.d( "KEY_D", "Errors: " + DP3T.getStatus( MainActivity.this ).getErrors() );
											}catch (SignatureException e){
												Log.d("KEY_D", e + "" + 1);
											} catch (Exception e) {
												Log.d("KEY_D", e.getMessage() + "" + 2);
												Log.d("KEY_D", e.toString() + "" + 2);

											}
									}
								});

								thread.start();
							}catch(Exception ex){
								Toast.makeText(MainActivity.this,"Exception thrown", Toast.LENGTH_LONG).show();

								Log.d("KEY_D", ex.toString() + "");
							}
						}
						@Override
						public void onError(Throwable throwable) {
							Toast.makeText(MainActivity.this,"Reported Infected Failed", Toast.LENGTH_LONG).show();
							Log.d("KEY_D",   throwable.getMessage() + throwable.toString());
						}
					});
		} catch (IllegalStateException e) {
			Toast.makeText(MainActivity.this,"Illegal State Exception", Toast.LENGTH_LONG).show();
			Log.d("KEY_D", e.getMessage());
		}
	}
	*/

	@Override
	public void onResume() {
		super.onResume();

		checkIntentForActions();

		if (!consumedExposedIntent) {
			boolean isHotlineCallPending = secureStorage.isHotlineCallPending();
			if (isHotlineCallPending) {
				gotoReportsFragment();
			}
		}
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(STATE_CONSUMED_EXPOSED_INTENT, consumedExposedIntent);
	}

	private void checkIntentForActions() {
		Intent intent = getIntent();
		String intentAction = intent.getAction();
		boolean launchedFromHistory = (intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0;
		if (ACTION_STOP_TRACING.equals(intentAction) && !launchedFromHistory) {
			tracingViewModel.setTracingEnabled(false);
			intent.setAction(null);
			setIntent(intent);
		}
		else if (ACTION_GOTO_REPORTS.equals(intentAction) && !launchedFromHistory && !consumedExposedIntent) {
			consumedExposedIntent = true;
			gotoReportsFragment();
			intent.setAction(null);
			setIntent(intent);
		}
	}

	private void showHomeFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.main_fragment_container, HomeFragment.newInstance())
				.commit();
	}

	private void gotoReportsFragment() {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.main_fragment_container, ReportsFragment.newInstance())
				.addToBackStack(ReportsFragment.class.getCanonicalName())
				.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_ONBOARDING) {
			if (resultCode == RESULT_OK) {
				secureStorage.setOnboardingCompleted(true);
				showHomeFragment();
			} else {
				finish();
			}
		}
	}

}
