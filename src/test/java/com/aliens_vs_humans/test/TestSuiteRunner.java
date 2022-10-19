package com.aliens_vs_humans.test;

import com.aliens_vs_humans.test.BeingTestSuite;

import org.junit.runners.Suite;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.platform.commons.annotation.*;

//@Suite
//@SelectPackages("com.aliens_vs_humans.test")
public class TestSuiteRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(BeingTestSuite.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }

}
