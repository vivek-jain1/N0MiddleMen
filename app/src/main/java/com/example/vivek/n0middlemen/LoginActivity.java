package com.example.vivek.n0middlemen;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity {


    private DatabaseReference dbUsers;
    private long timeCountInMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private LinearLayout l1,l2;

    private EditText etm,etc;
    private TextInputLayout t1,t2;

    private TextView time,welcome1,welcome2;

    private FirebaseAuth mAuth;
    // [END declare_auth]
    private CountDownTimer countDownTimer;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ImageButton ib1;
    private Button login,verify;
    private String phoneNumber;
    private TextView resend;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ib1 = (ImageButton) findViewById(R.id.ib1);
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        dbUsers = FirebaseDatabase.getInstance().getReference("Users");

        etm = (EditText) findViewById(R.id.editTextMobile);
        etc = (EditText) findViewById(R.id.editTextCode);

        login = (Button) findViewById(R.id.buttonNext);

        resend = (TextView) findViewById(R.id.resend);

        t1 =(TextInputLayout) findViewById(R.id.input_layout_mobile);
        t2 = (TextInputLayout) findViewById(R.id.input_layout_code);

        verify = (Button) findViewById(R.id.buttonVerify);
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);

        welcome1 = (TextView) findViewById(R.id.t1);
        welcome2 = (TextView) findViewById(R.id.t2);

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(phoneNumber,mResendToken);
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.

                signInWithPhoneAuthCredential(credential);
                Toast.makeText(LoginActivity.this,"Verification complete",LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(LoginActivity.this,"Invalid Request",LENGTH_SHORT).show();
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                Toast.makeText(LoginActivity.this,"Code Sent",LENGTH_SHORT).show();
                mVerificationId = verificationId;
                mResendToken = token;

                t2.setVisibility(View.VISIBLE);
                t1.setVisibility(GONE);
                verify.setVisibility(View.VISIBLE);
                login.setVisibility(GONE);
                welcome1.setVisibility(GONE);
                welcome2.setVisibility(View.VISIBLE);
                progressBarCircle.setVisibility(View.VISIBLE);
                resend.setVisibility(GONE);
                setProgressBarValues();
                startCountDownTimer();
                // ...
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        phoneNumber = etm.getText().toString();
                        if(!TextUtils.isEmpty(phoneNumber)){
                            if(dataSnapshot.child(phoneNumber).exists() == true) {
                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        phoneNumber,        // Phone number to verify
                                        60,                 // Timeout duration
                                        TimeUnit.SECONDS,   // Unit of timeout
                                        LoginActivity.this,               // Activity (for callback binding)
                                        mCallbacks);
                                mVerificationInProgress = true;
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"User doesn't exist.",LENGTH_SHORT).show();
                            }// OnVerificationStateChangedCallbacks
                        }else {
                            Toast.makeText(LoginActivity.this,"Incomplete Information", LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = etc.getText().toString();
                if(!TextUtils.isEmpty(code)){
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }else{
                    Toast.makeText(LoginActivity.this,"Incomplete Information",LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this,"Successful.Logging in...",LENGTH_SHORT).show();
                            mVerificationInProgress = false;
                            FirebaseUser user = task.getResult().getUser();
                            Intent intent =  new Intent(LoginActivity.this,Home.class);
                            intent.putExtra("Mob",phoneNumber);
                            finish();
                            startActivity(intent);



                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                // [END_EXCLUDE]Toast.makeText(LoginActivity.this,"Invalid Request",LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this,"Invalid Code.Check Again",LENGTH_SHORT).show();


                            }
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                // call to initialize the progress bar values
                setProgressBarValues();
                progressBarCircle.setVisibility(GONE);
                resend.setVisibility(View.VISIBLE);
                // hiding the reset icon
                // changing stop icon to start icon
                // making edit text editable
                // changing the timer status to stopped
            }

        }.start();
        countDownTimer.start();
    }

    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }
}
