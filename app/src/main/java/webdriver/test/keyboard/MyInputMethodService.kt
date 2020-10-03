package webdriver.test.keyboard

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.text.TextUtils
import android.view.View


class MyInputMethodService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    var keyboard: KeyboardView? = null
    var pad: Keyboard? = null
    var myShift = false

    override fun onCreateInputView(): View {
        keyboard = layoutInflater.inflate(R.layout.keyboard, null) as KeyboardView
        pad = Keyboard(this, R.xml.lowercase_pad)
        keyboard!!.keyboard = pad
        keyboard!!.setOnKeyboardActionListener(this)
        return keyboard!!
    }

    override fun onPress(i: Int) {}
    override fun onRelease(i: Int) {}

    override fun onKey(primatyCode: Int, keyCodes: IntArray) {
        val inputConnection = currentInputConnection
        if (inputConnection != null) {
            when (primatyCode) {
                Keyboard.KEYCODE_DELETE -> {
                    val selectedText = inputConnection.getSelectedText(0)
                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0)
                    } else {
                        inputConnection.commitText("", 1)
                    }
                }
                Keyboard.KEYCODE_SHIFT -> {
                    myShift = !myShift
                    if (myShift) {
                        pad = Keyboard(this, R.xml.uppercase_pad)
                        keyboard!!.keyboard = pad
                        keyboard!!.isShifted = true
                        keyboard!!.invalidateAllKeys()
                    } else {
                        pad = Keyboard(this, R.xml.lowercase_pad)
                        keyboard!!.keyboard = pad
                        keyboard!!.isShifted = false
                        keyboard!!.invalidateAllKeys()
                    }
                }
                else -> {
                    val code = primatyCode.toChar()
                    inputConnection.commitText(code.toString(), 1)
                }
            }
        }
    }

    override fun onText(charSequence: CharSequence) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
}