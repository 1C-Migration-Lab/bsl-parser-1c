import requests
import json

def test_bsl_parser():
    # URL вашего сервиса
    url = "http://localhost:8080/api/bsl/parse"
    
    # Тестовый код на 1С
    test_code = """
    Процедура ТестоваяПроцедура()
        А = 1;
        Б = 2;
        В = А + Б;
    КонецПроцедуры
    
    Функция ТестоваяФункция(Параметр1, Параметр2) Экспорт
        Возврат Параметр1 + Параметр2;
    КонецФункции
    """
    
    try:
        # Отправляем POST запрос
        response = requests.post(
            url=url,
            data=test_code,
            headers={'Content-Type': 'text/plain'}
        )
        
        # Проверяем статус ответа
        print(f"Status Code: {response.status_code}")
        
        # Выводим результат
        if response.status_code == 200:
            result = response.json()
            print("\nParser Response:")
            print(json.dumps(result, indent=2, ensure_ascii=False))
        else:
            print(f"Error: {response.text}")
            
    except requests.exceptions.ConnectionError:
        print("Error: Cannot connect to the server. Is it running?")
    except Exception as e:
        print(f"Error: {str(e)}")

if __name__ == "__main__":
    test_bsl_parser() 