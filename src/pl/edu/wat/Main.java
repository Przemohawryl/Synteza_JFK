package pl.edu.wat;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import javax.tools.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        final String fileName = "src\\ExampleClass.java";
        final String alteredFileName = "src\\ExampleClassAltered.java";
        CompilationUnit cu;
        try(FileInputStream in = new FileInputStream(fileName)){
            cu = JavaParser.parse(in);
        }

        cu.getNodesByType(MethodDeclaration.class)
			.forEach(Main::setMethodDescription);

        cu.getClassByName("ExampleClass").get().setName("ExampleClassAltered");

        try(FileWriter output = new FileWriter(new File(alteredFileName), false)) {
            output.write(cu.toString());
        }

        File[] files = {new File(alteredFileName)};
        String[] options = { "-d", "out//production//Synthesis" };

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits =
                fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
            compiler.getTask(
                null,
                fileManager,
                diagnostics,
                Arrays.asList(options),
                null,
                compilationUnits).call();

            diagnostics.getDiagnostics().forEach(d -> System.out.println(d.getMessage(null)));
        }
    }

    private static void setMethodDescription(MethodDeclaration method) {
        method.setBlockComment("Returned type of method: " + method.getType() + getParameters(method));
    }

    private static String getParameters(MethodDeclaration method) {
        String temp = "\n\t";
        int i = 0;
        if (method.getParameters() == null) return "";
        else temp += "Parameter" + (method.getParameters().size() > 1 ? "s of method: " : " of method: ");
        for(Parameter parameter : method.getParameters()) {
            i++;
            temp += String.format("{%d}(Name = " + parameter.getName() + ", Type = " + parameter.getType() + ")" + (i == method.getParameters().size() ? ". " : "; "), i - 1);
        }
        return temp;
    }
}
