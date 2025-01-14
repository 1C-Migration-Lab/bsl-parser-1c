package com.example.bslservice.service;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.stereotype.Service;
import com.github._1c_syntax.bsl.parser.BSLLexer;
import com.github._1c_syntax.bsl.parser.BSLParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BSLParserService {

    public Map<String, Object> parseCode(String code) {
        Map<String, Object> result = new HashMap<>();
        try {
            BSLLexer lexer = new BSLLexer(CharStreams.fromString(code));
            BSLParser parser = new BSLParser(new CommonTokenStream(lexer));
            BSLParser.FileContext fileContext = parser.file();
            List<Map<String, String>> methods = new ArrayList<>();
            BSLParser.SubsContext subsContext = fileContext.subs();
            if (subsContext != null) {
                for (BSLParser.SubContext subContext : subsContext.sub()) {
                    Map<String, String> methodInfo = new HashMap<>();
                    if (subContext.procedure() != null) {
                        String name = subContext.procedure().procDeclaration().subName().getText();
                        methodInfo.put("type", "procedure");
                        methodInfo.put("name", name);
                        if (subContext.procedure().procDeclaration().EXPORT_KEYWORD() != null) {
                            methodInfo.put("export", "true");
                        }
                        methods.add(methodInfo);
                    } else if (subContext.function() != null) {
                        String name = subContext.function().funcDeclaration().subName().getText();
                        methodInfo.put("type", "function");
                        methodInfo.put("name", name);
                        if (subContext.function().funcDeclaration().EXPORT_KEYWORD() != null) {
                            methodInfo.put("export", "true");
                        }
                        methods.add(methodInfo);
                    }
                }
            }
            result.put("success", true);
            result.put("methodCount", methods.size());
            result.put("methods", methods);
            result.put("hasErrors", parser.getNumberOfSyntaxErrors() > 0);
            result.put("errorCount", parser.getNumberOfSyntaxErrors());
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }
}
