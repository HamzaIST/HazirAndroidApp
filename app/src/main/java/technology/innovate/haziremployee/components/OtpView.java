package technology.innovate.haziremployee.components;

import static technology.innovate.haziremployee.config.ApplicationConstants.PIN_TYPE_CREATE;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import technology.innovate.haziremployee.R;
import technology.innovate.haziremployee.callbacks.OtpVerifyCallBack;

public class OtpView extends LinearLayout implements View.OnFocusChangeListener, TextWatcher {
    private Context context;
    EditText pinOneEditText, pinTwoEditText, pinThreeEditText, pinFourEditText, pinFiveEditText, pinSixEditText, pinHiddenEditText;
    private OtpVerifyCallBack otpVerifyCallBack;
    private int pinType;

    public OtpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews(context);
    }

    public void setContext(OtpVerifyCallBack otpVerifyCallBack, int pinType) {
        this.otpVerifyCallBack = otpVerifyCallBack;
        this.pinType = pinType;
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_otp_view, this);

        pinOneEditText = findViewById(R.id.pinOneEditText);
        pinTwoEditText = findViewById(R.id.pinTwoEditText);
        pinThreeEditText = findViewById(R.id.pinThreeEditText);
        pinFourEditText = findViewById(R.id.pinFourEditText);
        pinFiveEditText = findViewById(R.id.pinFiveEditText);
        pinSixEditText = findViewById(R.id.pinSixEditText);
        pinHiddenEditText = findViewById(R.id.pinHiddenEditText);

        setPinListeners();

    }

    public void resetFields() {
        pinOneEditText.setText("");
        pinTwoEditText.setText("");
        pinThreeEditText.setText("");
        pinFourEditText.setText("");
        pinFiveEditText.setText("");
        pinSixEditText.setText("");
        pinHiddenEditText.setText("");
    }

    public void setOtpText(String otpText) {
        pinOneEditText.setText(String.valueOf(otpText.charAt(0)));
        pinTwoEditText.setText(String.valueOf(otpText.charAt(1)));
        pinThreeEditText.setText(String.valueOf(otpText.charAt(2)));
        pinFourEditText.setText(String.valueOf(otpText.charAt(3)));
        pinFiveEditText.setText(String.valueOf(otpText.charAt(4)));
        pinSixEditText.setText(String.valueOf(otpText.charAt(5)));

        otpVerifyCallBack.confirmPinCallback(otpText);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pinOneEditText:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    setDefaultPinBackground(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;

            case R.id.pinTwoEditText:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;

            case R.id.pinThreeEditText:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;

            case R.id.pinFourEditText:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;

            case R.id.pinFiveEditText:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;

            case R.id.pinSixEditText:
                if (hasFocus) {
                    setFocus(pinHiddenEditText);
                    showSoftKeyboard(pinHiddenEditText);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }


    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setFocusedPinBackground(pinOneEditText);
        setFocusedPinBackground(pinTwoEditText);
        setFocusedPinBackground(pinThreeEditText);
        setFocusedPinBackground(pinFourEditText);
        setFocusedPinBackground(pinFiveEditText);
        setFocusedPinBackground(pinSixEditText);

        if (s.length() == 6) {
            pinSixEditText.setText(s.charAt(5) + "");
            hideSoftKeyboard(pinSixEditText);
            if (pinType == PIN_TYPE_CREATE) {
                otpVerifyCallBack.createPinCallback(pinOneEditText.getText().toString() + pinTwoEditText.getText().toString() +
                        pinThreeEditText.getText().toString() + pinFourEditText.getText().toString() + pinFiveEditText.getText().toString() + pinSixEditText.getText().toString());
            } else {
                otpVerifyCallBack.confirmPinCallback(pinOneEditText.getText().toString() + pinTwoEditText.getText().toString() +
                        pinThreeEditText.getText().toString() + pinFourEditText.getText().toString() + pinFiveEditText.getText().toString() + pinSixEditText.getText().toString());
            }
        } else {
            otpVerifyCallBack.onAllDigitNotCompleted();
            if (s.length() == 0) {
                setDefaultPinBackground(pinOneEditText);
                pinOneEditText.setText("");
            } else if (s.length() == 1) {
                setDefaultPinBackground(pinTwoEditText);
                pinOneEditText.setText(s.charAt(0) + "");
                pinTwoEditText.setText("");
                pinThreeEditText.setText("");
                pinFourEditText.setText("");
                pinFiveEditText.setText("");
                pinSixEditText.setText("");
            } else if (s.length() == 2) {
                setDefaultPinBackground(pinThreeEditText);
                pinTwoEditText.setText(s.charAt(1) + "");
                pinThreeEditText.setText("");
                pinFourEditText.setText("");
                pinFiveEditText.setText("");
                pinSixEditText.setText("");
            } else if (s.length() == 3) {
                setDefaultPinBackground(pinFourEditText);
                pinThreeEditText.setText(s.charAt(2) + "");
                pinFourEditText.setText("");
                pinFiveEditText.setText("");
                pinSixEditText.setText("");
            } else if (s.length() == 4) {
                setDefaultPinBackground(pinFiveEditText);
                pinFourEditText.setText(s.charAt(3) + "");
                pinFiveEditText.setText("");
                pinSixEditText.setText("");
            } else if (s.length() == 5) {
                setDefaultPinBackground(pinSixEditText);
                pinFiveEditText.setText(s.charAt(4) + "");
                pinSixEditText.setText("");
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void setDefaultPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.pin_background));
    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private void setFocusedPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.pin_focused));
    }

    private void setPinListeners() {
        pinHiddenEditText.addTextChangedListener(this);
        pinOneEditText.setOnFocusChangeListener(this);
        pinTwoEditText.setOnFocusChangeListener(this);
        pinThreeEditText.setOnFocusChangeListener(this);
        pinFourEditText.setOnFocusChangeListener(this);
        pinFiveEditText.setOnFocusChangeListener(this);
        pinSixEditText.setOnFocusChangeListener(this);
    }

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void setContext(Context context) {
        this.context = context;
    }
}