package com.kasteca;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {
    private MainActivity mn;

    @Before
    public void setUp() throws Exception {
        mn = new MainActivity();
    }

    @Test
    public void controlloCampiTrue() {
        assertTrue("I campi o sono vuoti o mail non contiene la @", mn.ControlloCampi("p.carl@gmail.com", "prova") );

    }

    @Test
    public void controlloCampiFalse() {
        assertFalse("I campi o sono vuoti o mail non contiene la @", mn.ControlloCampi("p.carlgmail.com", "prova") );
    }


    @Test
    public void login( ){

    }
}