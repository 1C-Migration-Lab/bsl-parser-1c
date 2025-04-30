package com.example.bslservice.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.stereotype.Service;

import com.github._1c_syntax.bsl.parser.BSLLexer;
import com.github._1c_syntax.bsl.parser.BSLParser;

@Service
public class BSLParserService {

    public Map<String, Object> parseCode(String code) {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            BSLLexer lexer = new BSLLexer(CharStreams.fromString(code));
            BSLParser parser = new BSLParser(new CommonTokenStream(lexer));
            BSLParser.FileContext fileContext = parser.file();

            if (parser.getNumberOfSyntaxErrors() > 0) {
                result.put("error", "BSL Syntax Error: Ошибка синтаксического анализа");
                return result;
            }

            Map<String, Object> ast = toAstJson(fileContext);
            ast.put("astVersion", "1.0");
            result = ast;
        } catch (RuntimeException e) {
            result.put("error", "Internal server error: " + e.getMessage());
        }
        return result;
    }

    private Map<String, Object> toAstJson(ParseTree node) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("type", node.getClass().getSimpleName());
        json.put("text", node.getText());
        List<Map<String, Object>> children = new ArrayList<>();
        int n = node.getChildCount();
        for (int i = 0; i < n; i++) {
            ParseTree child = node.getChild(i);
            children.add(toAstJson(child));
        }
        if (!children.isEmpty()) {
            json.put("children", children);
        }
        return json;
    }
}
