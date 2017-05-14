package br.usp.ime.dcc.seminariosdcc;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import br.usp.ime.dcc.seminariosdcc.shared.UserStore;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class UserStoreTest {

    private UserStore userStore;

    @Before
    public void instantiateUserStore() {
        userStore = new UserStore(InstrumentationRegistry.getTargetContext());
        InstrumentationRegistry.getTargetContext().deleteFile("user.nusp");
        InstrumentationRegistry.getTargetContext().deleteFile("user.pass");
    }

    @Test
    public void userIsNotLoggedIn() throws IOException {
        assertFalse(userStore.isLoggedIn());
    }

    @Test
    public void storeLoginCredentials() throws IOException {
        userStore.storeLoginCredentials("nusp", "pass");
        assertEquals("nusp", userStore.getNusp());
        assertEquals("pass", userStore.getPass());
        assertTrue(userStore.isLoggedIn());

        userStore.removeLoginCredentials();
        assertFalse(userStore.isLoggedIn());
    }
}
