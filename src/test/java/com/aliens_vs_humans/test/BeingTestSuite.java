package com.aliens_vs_humans.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        //List of test classes to bundle together
        TestBeing.class,
        TestHuman.class,
        TestAlien.class,
        TestArena.class
})

public class BeingTestSuite {
}
