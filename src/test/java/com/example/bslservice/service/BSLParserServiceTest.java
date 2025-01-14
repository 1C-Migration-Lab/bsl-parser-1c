package com.example.bslservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BSLParserServiceTest {

    @Autowired
    private BSLParserService parserService;

    @Test
    void testParseEmptyCode() {
        Map<String, Object> result = parserService.parseCode("");
        assertTrue((Boolean) result.get("success"));
        assertEquals(0, result.get("methodCount"));
    }

    @Test
    void testParseProcedureWithExport() {
        String code = """
            Процедура ТестоваяПроцедура() Экспорт
                // Тело процедуры
            КонецПроцедуры
            """;

        Map<String, Object> result = parserService.parseCode(code);

        assertTrue((Boolean) result.get("success"));
        assertEquals(1, result.get("methodCount"));

        List<Map<String, String>> methods = (List<Map<String, String>>) result.get("methods");
        assertEquals(1, methods.size());

        Map<String, String> method = methods.get(0);
        assertEquals("procedure", method.get("type"));
        assertEquals("ТестоваяПроцедура", method.get("name"));
        assertEquals("true", method.get("export"));
    }

    @Test
    void testParseFunctionWithExport() {
        String code = """
            Функция ТестоваяФункция() Экспорт
                Возврат Истина;
            КонецФункции
            """;

        Map<String, Object> result = parserService.parseCode(code);

        assertTrue((Boolean) result.get("success"));
        assertEquals(1, result.get("methodCount"));

        List<Map<String, String>> methods = (List<Map<String, String>>) result.get("methods");
        assertEquals(1, methods.size());

        Map<String, String> method = methods.get(0);
        assertEquals("function", method.get("type"));
        assertEquals("ТестоваяФункция", method.get("name"));
        assertEquals("true", method.get("export"));
    }

    @Test
    void testParseProcedureWithoutExport() {
        String code = """
            Процедура ВнутренняяПроцедура()
                // Тело процедуры
            КонецПроцедуры
            """;

        Map<String, Object> result = parserService.parseCode(code);

        assertTrue((Boolean) result.get("success"));
        assertEquals(1, result.get("methodCount"));

        List<Map<String, String>> methods = (List<Map<String, String>>) result.get("methods");
        assertEquals(1, methods.size());

        Map<String, String> method = methods.get(0);
        assertEquals("procedure", method.get("type"));
        assertEquals("ВнутренняяПроцедура", method.get("name"));
        assertNull(method.get("export"));
    }

    @Test
    void testParseMultipleMethods() {
        String code = """
            Процедура Процедура1() Экспорт
                // Тело процедуры 1
            КонецПроцедуры

            Функция Функция1() Экспорт
                Возврат Истина;
            КонецФункции

            Процедура Процедура2()
                // Тело процедуры 2
            КонецПроцедуры
            """;

        Map<String, Object> result = parserService.parseCode(code);

        assertTrue((Boolean) result.get("success"));
        assertEquals(3, result.get("methodCount"));

        List<Map<String, String>> methods = (List<Map<String, String>>) result.get("methods");
        assertEquals(3, methods.size());

        // Проверяем первую процедуру
        Map<String, String> method1 = methods.get(0);
        assertEquals("procedure", method1.get("type"));
        assertEquals("Процедура1", method1.get("name"));
        assertEquals("true", method1.get("export"));

        // Проверяем функцию
        Map<String, String> method2 = methods.get(1);
        assertEquals("function", method2.get("type"));
        assertEquals("Функция1", method2.get("name"));
        assertEquals("true", method2.get("export"));

        // Проверяем вторую процедуру
        Map<String, String> method3 = methods.get(2);
        assertEquals("procedure", method3.get("type"));
        assertEquals("Процедура2", method3.get("name"));
        assertNull(method3.get("export"));
    }
}
