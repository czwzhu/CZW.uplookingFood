package app.uplookingfood.app.uplookingfood.tools;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
public class EditTextClearUtil implements OnClickListener, TextWatcher {
    private List<EditText> mEditTexts = new ArrayList<EditText>();
    private List<View> mClearBtns = new ArrayList<View>();
    public void addEditText(EditText editText) {
        mEditTexts.add(editText);
        editText.addTextChangedListener(this);
    }
    public void addClearBtn(View clearView) {
        mClearBtns.add(clearView);
        clearView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        for (int i = 0; i < mClearBtns.size(); i++) {
            if (v == mClearBtns.get(i)) {
                mEditTexts.get(i).getEditableText().clear();
                mEditTexts.get(i).requestFocus();
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        for (int i = 0; i < mEditTexts.size(); i++) {
            if (s == mEditTexts.get(i).getEditableText()) {
                if (TextUtils.isEmpty(s.toString())) {
                    mClearBtns.get(i).setVisibility(View.GONE);
                } else {
                    mClearBtns.get(i).setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }
}
