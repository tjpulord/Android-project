package com.yesway.izhijia.view;

import com.yesway.izhijia.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnFocusChangeListener;

public class EditTextWithDelete extends EditText implements OnFocusChangeListener {
	private Drawable imgEnable;
	private Context context;

	public EditTextWithDelete(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public EditTextWithDelete(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public EditTextWithDelete(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		imgEnable = context.getResources().getDrawable(R.drawable.delete_text);
		addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();
			}

		});
		setDrawable();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (imgEnable != null && event.getAction() == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight()))
					&& (x < (getWidth() - getPaddingRight()));

			Rect rect = imgEnable.getBounds();
			int height = rect.height();
			int y = (int) event.getY();
			int distance = (getHeight() - height) / 2;
			boolean isInnerHeight = (y > distance) && (y < (distance + height));
			if (isInnerWidth && isInnerHeight) {
				setText("");
			}
		}
		return super.onTouchEvent(event);
	}

	private void setDrawable() {
		if (length() == 0) {
			setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		} else {
			setCompoundDrawablesWithIntrinsicBounds(null, null, imgEnable, null);
		}
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setDrawable();
		} else {
			setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}
	}

}