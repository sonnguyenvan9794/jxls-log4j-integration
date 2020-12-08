package org.jxls.templatebasedtests.multisheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.jxls.JxlsTester;
import org.jxls.area.XlsArea;
import org.jxls.common.Context;
import org.jxls.transform.SafeSheetNameBuilder;
import org.jxls.unittests.PoiSafeSheetNameBuilderUnitTest;

//import uk.org.lidalia.slf4jtest.TestLogger;
//import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * This is a multi sheet test.
 * 
 * @see PoiSafeSheetNameBuilderUnitTest
 */
public class PoiSafeSheetNameBuilderTest extends AbstractMultiSheetTest {

    /**
     * Tests whether the SafeSheetNameBuilder is used correctly.
     */
    @Test
    public void testSafeSheetNameBuilder() {
        Context context = new Context();
        final List<String> safeNames = new ArrayList<>();
        context.putVar(SafeSheetNameBuilder.CONTEXT_VAR_NAME, new SafeSheetNameBuilder() {
            @Override
            public String createSafeSheetName(String givenSheetName, int index) {
                String ret = givenSheetName + " sheet";
                safeNames.add(ret);
                return ret;
            }
        });
        List<TestSheet> testSheets = getTestSheets();
        context.putVar("sheets", testSheets);
        context.putVar("sheetnames", getSheetnames(testSheets));
        
        JxlsTester tester = JxlsTester.xlsx(getClass());
        tester.processTemplate(context);
        
        assertEquals("Number of safeNames is wrong", 2, safeNames.size());
        assertEquals("Name of 1st sheet is wrong", "data sheet", safeNames.get(0));
        assertEquals("Name of 2nd sheet is wrong", "parameters sheet", safeNames.get(1));
    }
    
    /**
     * Tests whether it still works without a SafeSheetNameBuilder.
     */
    @Test
    public void testNoSafeSheetNameBuilder() throws IOException {
        Context context = new Context();
        List<TestSheet> testSheets = getTestSheets();
        context.putVar("sheets", testSheets);
        context.putVar("sheetnames", getSheetnames(testSheets));
        
        JxlsTester tester = JxlsTester.xlsx(getClass());
        tester.processTemplate(context);
    }

    /**
     * Tests what happens if there's no SafeSheetNameBuilder and an invalid sheet name.
     */
//    @Test
//    public void testNoSafeSheetNameBuilder_invalidName() throws IOException {
//        TestLoggerFactory.clearAll(); // must be before setEnabledLevels()
//        TestLogger testLogger = TestLoggerFactory.getTestLogger(XlsArea.class);
//        testLogger.setEnabledLevels(uk.org.lidalia.slf4jext.Level.ERROR);
//
//        Context context = new Context();
//        List<TestSheet> testSheets = getTestSheets();
//        testSheets.get(0).setName("data["); // make name invalid
//        context.putVar("sheets", testSheets);
//        context.putVar("sheetnames", getSheetnames(testSheets));
//
//        JxlsTester tester = JxlsTester.xlsx(getClass());
//        tester.processTemplate(context);
//
//        // Sheet "data[" will not be created. There are just ERROR messages in the log. I use the slf4j-test library to check that.
//        assertTrue("There must be an ERROR message in the log regarding the invalid sheet name 'data['", testLogger.getAllLoggingEvents().get(0).getMessage().contains("data["));
//        assertEquals(IllegalArgumentException.class, testLogger.getAllLoggingEvents().get(0).getThrowable().get().getClass());
//    }
}
