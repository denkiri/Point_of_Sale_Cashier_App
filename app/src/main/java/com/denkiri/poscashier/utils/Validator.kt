package com.denkiri.poscashier.utils
import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern
class Validator {
    companion object {

        // Default validation messages
        private val PASSWORD_POLICY = """Password should be minimum 5 characters long""".trimMargin()
        private val NAME_VALIDATION_MSG = "Enter a valid name"
        private val EMAIL_VALIDATION_MSG = "Enter a valid email address"
        private val PHONE_VALIDATION_MSG = "Enter a valid phone number"
        private val AMOUNT_VALIDATION_MSG ="Enter a valid Amount"
        private val PRICE_VALIDATION_MSG ="Enter a valid Price"

        /**
         * Retrieve string data from the parameter.
         * @param data - can be EditText or String
         * @return - String extracted from EditText or data if its data type is Strin.
         */
        private fun getText(data: Any): String {
            var str = ""
            if (data is EditText) {
                str = data.text.toString()
            } else if (data is String) {
                str = data
            }
            return str
        }

        /**
         * Checks if the name is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the name is valid.
         */
        fun isValidName(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = str.trim().length > 2

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else NAME_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }


        fun isValidAmount(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = str.trim().isNotEmpty()

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else AMOUNT_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }
        fun isValidPrice(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = str.trim().isNotEmpty()

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else PRICE_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }

        /**
         * Checks if the email is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the email is valid.
         */
        fun isValidEmail(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            val valid = Patterns.EMAIL_ADDRESS.matcher(str).matches()

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else EMAIL_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }

        /**
         * Checks if the phone is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the phone is valid.
         */
        fun isValidPhone(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            var valid = Patterns.PHONE.matcher(str).matches()
            if (str.length < 12) {
                valid = false
            }
            if (str.length > 12) {
                valid = false
            }

            // Set error if required
            if (updateUI) {
                val error: String? = if (valid) null else PHONE_VALIDATION_MSG
                setError(data, error)
            }

            return valid
        }

        /**
         * Checks if the password is valid as per the following password policy.
         * Password should be minimum minimum 8 characters long.
         * Password should contain at least one number.
         * Password should contain at least one capital letter.
         * Password should contain at least one small letter.
         * Password should contain at least one special character.
         * Allowed special characters: "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
         *
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the password is valid as per the password policy.
         */
        fun isValidPassword(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            var valid = true

            // Password policy check
            // Password should be minimum minimum 8 characters long
            if (str.length < 5) {
                valid = false
            }
            if (updateUI) {
                val error: String? = if (valid) null else PASSWORD_POLICY
                setError(data, error)
            }

            return valid
        }

        /**
         * Sets error on EditText or TextInputLayout of the EditText.
         * @param data - Should be EditText
         * @param error - Message to be shown as error, can be null if no error is to be set
         */
        private fun setError(data: Any, error: String?) {
            if (data is EditText) {
                if (data.parent.parent is TextInputLayout) {
                    (data.parent.parent as TextInputLayout).setError(error)
                } else {
                    data.setError(error)
                }
            }
        }


    }

   }

