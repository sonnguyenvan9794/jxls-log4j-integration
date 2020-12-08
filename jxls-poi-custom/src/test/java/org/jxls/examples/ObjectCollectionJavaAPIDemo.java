package org.jxls.examples;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.jxls.area.XlsArea;
import org.jxls.command.EachCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.entity.Employee;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Leonid Vysochyn
 *         Date: 11/4/13
 */
public class ObjectCollectionJavaAPIDemo {
    private static final Logger logger = LogManager.getLogger(ObjectCollectionJavaAPIDemo.class);

    @Test
    public void test() throws ParseException, IOException {
        logger.info("Running Object Collection JavaAPI demo");
        List<Employee> employees = Employee.generateSampleEmployeeData();
        try (InputStream is = ObjectCollectionDemo.class.getResourceAsStream("object_collection_javaapi_template.xls")) {
            try (OutputStream os = new FileOutputStream("target/object_collection_javaapi_output.xls")) {
                Transformer transformer = TransformerFactory.createTransformer(is, os);
                XlsArea xlsArea = new XlsArea("Template!A1:D4", transformer);
                XlsArea employeeArea = new XlsArea("Template!A4:D4", transformer);
                EachCommand employeeEachCommand = new EachCommand("employee", "employees", employeeArea);
                xlsArea.addCommand("A4:D4", employeeEachCommand);
                Context context = new Context();
                context.putVar("employees", employees);
                xlsArea.applyAt(new CellRef("Result!A1"), context);
                transformer.write();
            }
        }
    }
}
