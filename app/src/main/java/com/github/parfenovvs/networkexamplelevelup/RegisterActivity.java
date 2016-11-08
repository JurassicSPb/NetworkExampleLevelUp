package com.github.parfenovvs.networkexamplelevelup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.parfenovvs.networkexamplelevelup.entities.AuthPayload;
import com.github.parfenovvs.networkexamplelevelup.entities.RequestContainer;
import com.github.parfenovvs.networkexamplelevelup.utils.HashUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
	private EditText name;
	private EditText pwd;
	private EditText repeatPwd;
	private Button signUpBtn;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		name = (EditText) findViewById(R.id.username);
		pwd = (EditText) findViewById(R.id.pwd);
		repeatPwd = (EditText) findViewById(R.id.repeat);
		signUpBtn = (Button) findViewById(R.id.sign_up);

		signUpBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (name.getText().length() > 0 && pwd.getText().length() > 0) {
					if (pwd.getText().toString().equals(repeatPwd.getText().toString())) {
						doSignUp();
					}
				}
			}
		});
	}

	private void doSignUp() {
		AuthPayload payload = new AuthPayload(name.getText().toString(), HashUtil.hash(pwd.getText().toString()));
		final RequestContainer<AuthPayload> requestContainer = new RequestContainer<>(payload);
		new Thread(new Runnable() {
			@Override
			public void run() {
				OkHttpClient client = new OkHttpClient();
				RequestBody body = RequestBody
						.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestContainer));
				Request request = new Request.Builder()
						.url("http://192.168.3.205:8080/levelupchat/register")
						.post(body)
						.build();
				try {
					Response response = client.newCall(request).execute();
					Log.d(RegisterActivity.class.getSimpleName(), response.body().string());
				} catch (IOException e) {
					Log.e(RegisterActivity.class.getSimpleName(), Log.getStackTraceString(e));
				}
//				try {
//					URL url = new URL("http://91.122.56.48:8080/levelupchat/register");
//					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//					connection.setRequestMethod("POST");
//					connection.setDoInput(true);
//					connection.setDoOutput(true);
//					connection.setRequestProperty("Content-Length", String.valueOf(hello.getBytes().length));
//
//					OutputStream os = connection.getOutputStream();
//					os.write(hello.getBytes("UTF-8"));
//					connection.connect();
//
//					int responseCode = connection.getResponseCode();
//
//					InputStream is = connection.getInputStream();
//					ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
//					byte[] buffer = new byte[4096];
//					int length;
//					while ((length = is.read(buffer)) != -1) {
//						byteArrayStream.write(buffer, 0, length);
//					}
//					String response = byteArrayStream.toString();
//					Log.d(RegisterActivity.class.getSimpleName(), "RESPONSE code: " + responseCode + " body: " + response);
//					is.close();
//				} catch (IOException e) {
//					Log.e(RegisterActivity.class.getSimpleName(), Log.getStackTraceString(e));
//				}
			}
		}).start();
	}
}
