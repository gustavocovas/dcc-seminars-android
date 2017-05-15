package br.usp.ime.dcc.seminariosdcc;


import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.usp.ime.dcc.seminariosdcc.student.StudentSignInActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class StudentSignInActivityTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            StudentSignInActivity.class);

    @Test
    public void signInButtonStatus() {
        onView(withId(R.id.button_student_sign_in)).check(matches(not(isEnabled())));

        onView(withId(R.id.text_input_nusp_student_sign_in)).perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.text_input_password_student_sign_in)).perform(typeText("pass"), closeSoftKeyboard());

        onView(withId(R.id.button_student_sign_in)).check(matches(isEnabled()));
    }
}
