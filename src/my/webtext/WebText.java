package my.webtext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WebText extends Activity {
	
	MessageSender sender = null;
		
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final EditText textMessage = (EditText) findViewById(R.id.txt_msg);
		final EditText textPhoneNo = (EditText) findViewById(R.id.txt_phn_no);
		Button btnSendMessage = (Button) findViewById(R.id.btn_send_msg);

		sender = new MessageSender();
		Thread loginThread = new Thread(
				new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							sender.login();
						} catch(Throwable th) {
							
						}
					}
				}
		);
		loginThread.start();

		btnSendMessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String txtmsg = textMessage.getText().toString();
				String[] phoneNos = {textPhoneNo.getText().toString()};
				txtmsg = "test";
				phoneNos[0] = "0871709194";
				sender.sendMessage();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		sender.logout();
	}
}