package education.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import education.domain.Institution;
import education.domain.Student;
import education.domain.Teacher;
import education.repository.IRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EduServer {
    // Теперь сервер знает и про студентов, и про учителей
    private IRepository<Student> studentRepo;
    private IRepository<Teacher> teacherRepo;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    private Institution university = new Institution(
            "Astana IT University",
            "Mangilik El Ave. 55/11, Block C1.0",
            "None"
    );

    // Конструктор теперь принимает ДВА репозитория
    public EduServer(IRepository<Student> studentRepo, IRepository<Teacher> teacherRepo) {
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);

        // 1. ПУТЬ ДЛЯ API (ДАННЫЕ)
        server.createContext("/api/data", exchange -> {
            try {
                List<Student> students = studentRepo.getAll();
                List<Teacher> teachers = teacherRepo.getAll();
                university.updateStatistics(students.size(), teachers.size());

                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("institution", university);
                responseMap.put("students", students);
                responseMap.put("teachers", teachers);

                String jsonResponse = gson.toJson(responseMap);
                sendResponse(exchange, 200, jsonResponse, "application/json");
            } catch (Exception e) {
                sendResponse(exchange, 500, "{\"error\": \"" + e.getMessage() + "\"}", "application/json");
            }
        });

        // 2. ПУТЬ ДЛЯ ФРОНТЕНДА (СТАТИКА)
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";

            java.io.File file = new java.io.File("resources" + path);

            if (file.exists()) {
                String contentType = "text/html";
                if (path.endsWith(".css")) contentType = "text/css";
                if (path.endsWith(".js")) contentType = "application/javascript";

                byte[] content = java.nio.file.Files.readAllBytes(file.toPath());
                exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
                exchange.sendResponseHeaders(200, content.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(content);
                }
            } else {
                String error = "404 Not Found";
                exchange.sendResponseHeaders(404, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        });

        // 3. НОВЫЙ ПУТЬ ДЛЯ УДАЛЕНИЯ (DELETE)
        server.createContext("/api/delete", exchange -> {
            if ("DELETE".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    // Разбираем запрос: /api/delete?type=student&id=5
                    String query = exchange.getRequestURI().getQuery();
                    Map<String, String> params = new HashMap<>();
                    for (String param : query.split("&")) {
                        String[] pair = param.split("=");
                        if (pair.length > 1) params.put(pair[0], pair[1]);
                    }

                    String type = params.get("type");
                    int id = Integer.parseInt(params.get("id"));

                    // Удаляем из базы
                    if ("student".equals(type)) {
                        studentRepo.delete(id);
                    } else if ("teacher".equals(type)) {
                        teacherRepo.delete(id);
                    }

                    // Отправляем ОК (200)
                    sendResponse(exchange, 200, "{\"status\":\"deleted\"}", "application/json");

                } catch (Exception e) {
                    sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}", "application/json");
                }
            } else {
                // Если пришел не DELETE запрос
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}", "application/json");
            }
        });

        // 4. ПУТЬ ДЛЯ СОЗДАНИЯ (CREATE)
        server.createContext("/api/create", exchange -> {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    // Читаем JSON, который прислал браузер
                    java.io.InputStream is = exchange.getRequestBody();
                    String body = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);

                    // Превращаем JSON в Map (универсальный объект)
                    Map<String, Object> data = gson.fromJson(body, Map.class);
                    String type = (String) data.get("type");
                    String name = (String) data.get("name");

                    if ("student".equals(type)) {
                        // Числа в JSON приходят как Double, приводим к int
                        int age = ((Double) data.get("age")).intValue();
                        // ID база даст сама (0)
                        studentRepo.add(new Student.Builder().setName(name).setAge(age).build());
                    }
                    else if ("teacher".equals(type)) {
                        int exp = ((Double) data.get("experience")).intValue();
                        String subject = (String) data.get("subject");
                        // Возраст для учителя пока дефолтный (или добавь поле в форму)
                        teacherRepo.add(new Teacher.Builder()
                                .setName(name).setAge(35).setSubject(subject).setExperience(exp).build());
                    }

                    sendResponse(exchange, 201, "{\"status\":\"created\"}", "application/json");

                } catch (Exception e) {
                    e.printStackTrace(); // Покажет ошибку в консоли
                    sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}", "application/json");
                }
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}", "application/json");
            }
        });

        // 5. ПУТЬ ДЛЯ ОБНОВЛЕНИЯ (UPDATE)
        server.createContext("/api/update", exchange -> {
            if ("PUT".equalsIgnoreCase(exchange.getRequestMethod())) {
                try {
                    // Читаем JSON
                    java.io.InputStream is = exchange.getRequestBody();
                    String body = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                    Map<String, Object> data = gson.fromJson(body, Map.class);

                    String type = (String) data.get("type");
                    int id = ((Double) data.get("id")).intValue(); // Gson парсит числа как Double

                    if ("student".equals(type)) {
                        int newAge = ((Double) data.get("age")).intValue();
                        studentRepo.update(id, newAge); // Обновляем возраст
                    } else if ("teacher".equals(type)) {
                        int newExp = ((Double) data.get("experience")).intValue();
                        teacherRepo.update(id, newExp); // Обновляем опыт
                    }

                    sendResponse(exchange, 200, "{\"status\":\"updated\"}", "application/json");

                } catch (Exception e) {
                    sendResponse(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}", "application/json");
                }
            } else {
                sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}", "application/json");
            }
        });

        server.start();
        System.out.println("[SERVER] Web Interface: http://localhost:8080/");
        System.out.println("[SERVER] API Endpoint: http://localhost:8080/api/data");
    }

    private void sendResponse(com.sun.net.httpserver.HttpExchange exchange, int code, String response, String contentType) throws IOException {
        byte[] bytes = response.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}