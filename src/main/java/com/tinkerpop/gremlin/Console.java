package com.tinkerpop.gremlin;

import com.tinkerpop.gremlin.statements.EvaluationException;
import com.tinkerpop.gremlin.statements.SyntaxException;
import jline.ConsoleReader;
import jline.History;
import org.apache.commons.jxpath.JXPathException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @version 0.1
 */
public class Console {

    private static final String INDENT = "         ";
    private static final int TAB_LENGTH = 2;
    private static final String PRINT_RETURN = "==>";
    private static final String MAP_EQUALS = "=";
    private static final String PROMPT = "gremlin> ";
    private static final String QUIT = "quit";
    private static final String SINGLE_SPACE = " ";
    private static final String EMPTY_STRING = "";
    private static final String SYNTAX_ERROR = "Syntax error: ";
    private static final String EVALUATION_ERROR = "Evaluation error: ";
    //private static final String GREMLIN_VERSION = "0.2";


    public static void main(final String[] args) throws IOException {

        PrintStream output = System.out;

        ConsoleReader reader = new ConsoleReader();
        reader.setBellEnabled(false);
        reader.setUseHistory(true);

        try {
            History history = new History();
            history.setHistoryFile(new File(".gremlin_history"));
            reader.setHistory(history);
        } catch (IOException e) {
        }

        GremlinEvaluator gremlinEvaluator = new GremlinEvaluator();

        String line = "";
        output.println();
        output.println("         \\,,,/");
        output.println("         (o o)");
        output.println("-----oOOo-(_)-oOOo-----");
        //output.println("     Gremlin v" + GREMLIN_VERSION);

        while (line != null) {
            if (gremlinEvaluator.inStatement())
                line = reader.readLine(INDENT + generateIndentation(gremlinEvaluator.getDepth() * TAB_LENGTH));
            else {
                line = reader.readLine(PROMPT);
                if (null == line || line.equalsIgnoreCase(QUIT)) {
                    if (null == line) {
                        System.out.println();
                    }
                    break;
                }
            }
            if (line.length() > 0) {
                try {
                    List results = gremlinEvaluator.evaluate(line);
                    if (null != results) {
                        if (results.size() > 0) {
                            if (results.size() == 1 && results.get(0) instanceof Map) {
                                Map map = (Map) results.get(0);
                                for (Object key : map.keySet()) {
                                    output.println(PRINT_RETURN + key + MAP_EQUALS + map.get(key));
                                }
                            } else {
                                for (Object o : results) {
                                    output.println(PRINT_RETURN + o);
                                }
                            }
                        }
                    }
                } catch (JXPathException e) {
                    output.println(e.getMessage());
                } catch (SyntaxException e) {
                    output.println(SYNTAX_ERROR + e.getMessage());
                } catch (EvaluationException e) {
                    output.println(EVALUATION_ERROR + e.getMessage());
                } catch (Exception e) {
                    output.println(e.getMessage());
                }
            }
        }
    }

    private static String generateIndentation(final int spaces) {
        String spaceString = EMPTY_STRING;
        for (int i = 0; i < spaces; i++) {
            spaceString = spaceString + SINGLE_SPACE;
        }
        return spaceString;
    }
}
