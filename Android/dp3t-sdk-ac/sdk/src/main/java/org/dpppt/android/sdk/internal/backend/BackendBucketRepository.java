/*
 * Copyright (c) 2020 Ubique Innovation AG <https://www.ubique.ch>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * SPDX-License-Identifier: MPL-2.0
 */
package org.dpppt.android.sdk.internal.backend;

import android.content.Context;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;

import org.dpppt.android.sdk.backend.SignatureException;
import org.dpppt.android.sdk.backend.SignatureVerificationInterceptor;
import org.dpppt.android.sdk.internal.backend.proto.Exposed;

import java.io.IOException;
import java.security.PublicKey;

import androidx.annotation.NonNull;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.protobuf.ProtoConverterFactory;

public class BackendBucketRepository implements Repository {

	public static long BATCH_LENGTH = 2 * 60 * 60 * 1000L;

	private BucketService bucketService;

	public BackendBucketRepository(@NonNull Context context, @NonNull String bucketBaseUrl, @NonNull PublicKey publicKey) {
		Retrofit bucketRetrofit = new Retrofit.Builder()
				.baseUrl(bucketBaseUrl)
				.client(getClientBuilder(context)
						.addInterceptor(new TimingVerificationInterceptor())
						.addInterceptor(new SignatureVerificationInterceptor(publicKey))
						.build())
				.addConverterFactory(ProtoConverterFactory.create())
				.build();

		bucketService = bucketRetrofit.create(BucketService.class);
	}

	public Exposed.ProtoExposedList getExposees(long batchReleaseTime) throws IOException, StatusCodeException, ServerTimeOffsetException, SignatureException {
		Response<Exposed.ProtoExposedList> response;
		try {
			Log.d("SKList", "TRY ");

			response = bucketService.getExposees(batchReleaseTime).execute();
		} catch (RuntimeException re) {
			Log.d("SKList", "Exception  " + re.getMessage());

			if (re.getCause() instanceof InvalidProtocolBufferException) {
				// unwrap protobuf exception
				throw (InvalidProtocolBufferException) re.getCause();
			} else {
				throw re;
			}
		}
		if (response.isSuccessful() && response.body() != null) {
			Log.d("SKList", "SUCCS ");

			return response.body();
		} else {
			Log.d("SKList", "FAIL " + response.code() +  "\n\n\n" + response.raw());

			throw new StatusCodeException(response.raw());
		}
	}

}
