package com.github.parfenovvs.networkexamplelevelup;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.parfenovvs.networkexamplelevelup.api.ChatApi;
import com.github.parfenovvs.networkexamplelevelup.entities.AuthResponse;
import com.github.parfenovvs.networkexamplelevelup.entities.User;
import com.github.parfenovvs.networkexamplelevelup.utils.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
	private EditText name;
	private EditText pwd;
	private EditText repeatPwd;
	private Button signUpBtn;
	private ImageView imageView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		name = (EditText) findViewById(R.id.username);
		pwd = (EditText) findViewById(R.id.pwd);
		repeatPwd = (EditText) findViewById(R.id.repeat);
		signUpBtn = (Button) findViewById(R.id.sign_up);
		imageView = (ImageView) findViewById(R.id.image);

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

		Picasso.with(this)
				.load("http://pr1.zoon.ru/wpyKAUV-t4JmSQKNrqrzOQ==/600x300,q85/4px-BW84_n1ZhjPGtDc10n2vcQtG-iF8JgmBL5TxEFM7SDM1DYiZAbpWZn936YbMKCu8MQqDs4o=")
//				.resize(600, 400)
//				.centerCrop()
				.transform(new CircleTransformation())
				.into(imageView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_example, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item1:
				Toast.makeText(this, "Item 1", Toast.LENGTH_SHORT).show();
				break;
			case R.id.item2:
				Toast.makeText(this, "Item 2", Toast.LENGTH_SHORT).show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getUsers() {
		ChatApi.getInstance().getUsers(new Func<List<User>>() {
			@Override
			public void onResult(List<User> result) {
				//UserDatabase
				//beginTransaction
				//add(result)
				//commit
				//closeDatabase
			}
		}, new Func<Throwable>() {
			@Override
			public void onResult(Throwable result) {

			}
		});
	}

	private void doSignUp() {
		Log.d(RegisterActivity.class.getSimpleName(), "Start sign up");
		ChatApi.getInstance().register(name.getText().toString(), pwd.getText().toString(),
				new Func<AuthResponse>() {
					@Override
					public void onResult(AuthResponse result) {
						Log.d(RegisterActivity.class.getSimpleName(), "Sign up success");
					}
				}, new Func<Throwable>() {
					@Override
					public void onResult(Throwable result) {
						Log.e(RegisterActivity.class.getSimpleName(), "Sign up error");
						Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
					}
				});
		Log.d(RegisterActivity.class.getSimpleName(), "Sign up method end");
//		AuthPayload payload = new AuthPayload(name.getText().toString(), HashUtil.hash(pwd.getText().toString()));
//		final RequestContainer<AuthPayload> requestContainer = new RequestContainer<>(payload);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				OkHttpClient client = new OkHttpClient();
//				RequestBody body = RequestBody
//						.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(requestContainer));
//				Request request = new Request.Builder()
//						.url("http://192.168.3.205:8080/levelupchat/register")
//						.post(body)
//						.build();
//				try {
//					Response response = client.newCall(request).execute();
//					Log.d(RegisterActivity.class.getSimpleName(), response.body().string());
//				} catch (IOException e) {
//					Log.e(RegisterActivity.class.getSimpleName(), Log.getStackTraceString(e));
//				}
////				try {
////					URL url = new URL("http://91.122.56.48:8080/levelupchat/register");
////					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////					connection.setRequestMethod("POST");
////					connection.setDoInput(true);
////					connection.setDoOutput(true);
////					connection.setRequestProperty("Content-Length", String.valueOf(hello.getBytes().length));
////
////					OutputStream os = connection.getOutputStream();
////					os.write(hello.getBytes("UTF-8"));
////					connection.connect();
////
////					int responseCode = connection.getResponseCode();
////
////					InputStream is = connection.getInputStream();
////					ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
////					byte[] buffer = new byte[4096];
////					int length;
////					while ((length = is.read(buffer)) != -1) {
////						byteArrayStream.write(buffer, 0, length);
////					}
////					String response = byteArrayStream.toString();
////					Log.d(RegisterActivity.class.getSimpleName(), "RESPONSE code: " + responseCode + " body: " + response);
////					is.close();
////				} catch (IOException e) {
////					Log.e(RegisterActivity.class.getSimpleName(), Log.getStackTraceString(e));
////				}
//			}
//		}).start();
	}
}
