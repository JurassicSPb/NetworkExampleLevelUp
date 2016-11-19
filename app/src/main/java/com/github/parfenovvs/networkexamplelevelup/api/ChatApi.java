package com.github.parfenovvs.networkexamplelevelup.api;


import android.os.Handler;
import android.os.Looper;

import com.github.parfenovvs.networkexamplelevelup.Func;
import com.github.parfenovvs.networkexamplelevelup.entities.AuthPayload;
import com.github.parfenovvs.networkexamplelevelup.entities.AuthResponse;
import com.github.parfenovvs.networkexamplelevelup.entities.RequestContainer;
import com.github.parfenovvs.networkexamplelevelup.entities.ResponseContainer;
import com.github.parfenovvs.networkexamplelevelup.entities.User;
import com.github.parfenovvs.networkexamplelevelup.utils.HashUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatApi {
	private static final String BASE_URL = "http://91.122.56.48:8084/levelupchat";
	private static final ChatApi instance = new ChatApi();
	private OkHttpClient client;
	private Gson gson;
	private Handler uiHandler;

	private ChatApi() {
		client = new OkHttpClient();
		gson = new Gson();
		uiHandler = new Handler(Looper.getMainLooper());
	}

	public static ChatApi getInstance() {
		return instance;
	}

	private Request prepareRequest(RequestContainer requestContainer, String urlSuffix) {
		RequestBody body = RequestBody
				.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(requestContainer));
		Request request = new Request.Builder()
				.url(BASE_URL + urlSuffix)
				.post(body)
				.build();
		return request;
	}

	public void register(String name, String pwd, Func<AuthResponse> onSuccess,
	                     Func<Throwable> onError) {
		doRegOrAuth("/register", name, pwd, onSuccess, onError);
	}

	public void auth(String name, String pwd, Func<AuthResponse> onSuccess,
	                 Func<Throwable> onError) {
		doRegOrAuth("/auth", name, pwd, onSuccess, onError);
	}

	private void doRegOrAuth(final String urlSuffix, final String name, final String pwd, final Func<AuthResponse> onSuccess,
	                         final Func<Throwable> onError) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				AuthPayload payload = new AuthPayload(name, HashUtil.hash(pwd));
				RequestContainer<AuthPayload> requestContainer = new RequestContainer<>(payload);

				Request request = prepareRequest(requestContainer, urlSuffix);

				try {
					final Response response = client.newCall(request).execute();
					if (response.code() == 200) {
						String json = response.body().string();
						final ResponseContainer<AuthResponse> responseContainer =
								gson.fromJson(json, new TypeToken<ResponseContainer<AuthResponse>>() {
								}.getType());
						uiHandler.post(new Runnable() {
							@Override
							public void run() {
								onSuccess.onResult(responseContainer.getPayload());
							}
						});
					} else {
						final String body = response.body().string();
						uiHandler.post(new Runnable() {
							@Override
							public void run() {
								onError.onResult(new IOException("Bad response code: " + response.code() +
										" reason: " + body));
							}
						});
					}
				} catch (final IOException e) {
					uiHandler.post(new Runnable() {
						@Override
						public void run() {
							onError.onResult(e);
						}
					});
				}
			}
		}).start();
	}

	public void getUsers(final Func<List<User>> onSuccess, final Func<Throwable> onError) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RequestContainer requestContainer = new RequestContainer();

				Request request = prepareRequest(requestContainer, "/users/getAll");

				try {
					final Response response = client.newCall(request).execute();
					if (response.code() == 200) {
						String json = response.body().string();
						final ResponseContainer<List<User>> responseContainer =
								gson.fromJson(json, new TypeToken<ResponseContainer<ArrayList<User>>>() {
								}.getType());
						uiHandler.post(new Runnable() {
							@Override
							public void run() {
								onSuccess.onResult(responseContainer.getPayload());
							}
						});
					} else {
						final String body = response.body().string();
						uiHandler.post(new Runnable() {
							@Override
							public void run() {
								onError.onResult(new IOException("Bad response code: " + response.code() +
										" reason: " + body));
							}
						});
					}
				} catch (final IOException e) {
					uiHandler.post(new Runnable() {
						@Override
						public void run() {
							onError.onResult(e);
						}
					});
				}
			}
		}).start();
	}
}
