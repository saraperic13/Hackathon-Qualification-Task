package eu.execom.todolistgrouptwo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.model.User;
import eu.execom.todolistgrouptwo.model.dto.RegisterDTO;
import eu.execom.todolistgrouptwo.model.dto.TokenContainerDTO;
import eu.execom.todolistgrouptwo.util.NetworkingUtils;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @ViewById
    EditText confirmPassword;

    @ViewById
    EditText username;

    @ViewById
    EditText password;

    @RestService
    RestApi restApi;


    @EditorAction(R.id.password)
    @Click
    void register() {
        final String confirmPassword = this.confirmPassword.getText().toString();
        final String username = this.username.getText().toString();
        final String password = this.password.getText().toString();
        final User user = new User(username, password);

        if(confirmPassword.equals("") || username.equals("") || password.equals(""))
            Toast.makeText(this,"All fields are required!", Toast.LENGTH_LONG).show();

        else if(!isEmailValid(username)) Toast.makeText(this,"Email is invalid!", Toast.LENGTH_LONG).show();

        else if(!isPasswordValid(password)) Toast.makeText(this,"Password must contain at least one digit and be at least 6 characters long!", Toast.LENGTH_LONG).show();

        else if(!password.equals(confirmPassword)) Toast.makeText(this,"Passwords do not match!", Toast.LENGTH_LONG).show();

        else registerUser(user);
    }


    @Background
    void registerUser(User user) {
        String token;
        final RegisterDTO registerDTO = new RegisterDTO(user.getUsername(), user.getPassword(), user.getPassword());
        try{
            restApi.register(registerDTO);
        }
        catch (Exception e){
            showRegisterError();
            Log.e(TAG, e.getMessage(), e);
            return;
        }
        final TokenContainerDTO tokenContainerDTO = restApi.login(
                NetworkingUtils.packUserCredentials(user.getUsername(), user.getPassword()));

        token = tokenContainerDTO.getAccessToken();
        login(token);
    }


    @UiThread
    void login(String token) {
        final Intent intent = new Intent();
        intent.putExtra("token", token);

        setResult(RESULT_OK, intent);
        finish();
    }


    @UiThread
    void showRegisterError() {
        username.setError("Username already exists.");
        Toast.makeText(this, "Username already exists",Toast.LENGTH_LONG).show();
    }


    static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    static boolean isPasswordValid(String password){
        return !(password.length() < 6 || !password.matches(".*\\d.*"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
