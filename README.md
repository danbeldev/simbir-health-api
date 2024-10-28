# Основное задание:
1. Account URL: http://localhost:8081/swagger-ui/index.html
2. Hospital URL: http://localhost:8082/swagger-ui/index.html
3. Timetable URL: http://localhost:8083/swagger-ui/index.html
4. Document URL: http://localhost:8084/swagger-ui/index.html

# Дополнительное задание:
1. ElasticSearch URL: http://localhost:9200  
   Энпойнт поиска: http://localhost:8084/api/History/search?query={query}
2. Kibana URL: http://localhost:5601

# Дополнительная информация

DELETE /api/Accounts/{id}  
Если удаляемый аккаунт является доктором, тогда также удаляется его расписание

DELETE /api/Hospitals/{id}  
Также удаляется расписание

Из-за этого нарушается “Диаграмма зависимостей микросервисов друг от друга” из задания. Account зависит от Timetable, Hospital зависит от Timetable. Возможно, это было лишним, но кажется более логичным
