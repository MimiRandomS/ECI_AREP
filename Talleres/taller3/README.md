# Taller 3 – MicroSpringBoot: Framework Web en Java

Este proyecto implementa un **servidor web básico en Java puro** (similar a Apache) con capacidades de **reflexión e inversión de control (IoC)** para construir aplicaciones web a partir de **POJOs anotados**.

El servidor es capaz de:

* Servir **páginas HTML e imágenes PNG** desde una carpeta estática.
* Explorar el classpath (`target/classes`) para detectar componentes anotados con `@RestController`.
* Registrar automáticamente servicios definidos con `@GetMapping`.
* Manejar parámetros en consultas (`@RequestParam`).
* Atender múltiples solicitudes secuenciales (no concurrentes).

---

## Getting Started

### Prerequisitos

* [Java JDK 17+](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
* [Maven](https://maven.apache.org/)

### Instalación

1. Clona el repositorio:

```bash
git clone https://github.com/MimiRandomS/AREP_taller3
cd AREP_taller3
```

2. Compila el proyecto:

```bash
mvn package
```

3. Ejecuta el servidor con `MicroSpringBoot`:

```bash
mvn exec:java -Dexec.mainClass="com.mimirandoms.arep1.microspringboot.MicroSpringBoot"
```

4. Abre tu navegador en `http://localhost:35000`.

---

## Uso del Framework

### Archivos estáticos

Define la carpeta de archivos estáticos en tu aplicación principal:

```java
HttpServer.staticfiles("page");
```

Cualquier archivo en esa carpeta podrá ser accedido por URL:

* `http://localhost:35000/index.html`
* `http://localhost:35000/styles.css`
* `http://localhost:35000/image.png`

---

### Definición de controladores REST

El framework detecta automáticamente clases anotadas con `@RestController` y métodos con `@GetMapping`.

Ejemplo:

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
```

Acceso:

```
http://localhost:35000/hello
```

---

### Soporte de parámetros con `@RequestParam`

```java
@RestController
public class GreetingController {
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
}
```

Acceso:

```
http://localhost:35000/greeting?name=Pedro
```

Respuesta:

```
Hola Pedro
```

Si el parámetro no se envía, se usa el valor por defecto:

```
http://localhost:35000/greeting
→ Hola World
```

---

### Ejemplo de `main`

El punto de entrada carga archivos estáticos y arranca el servidor:

```java
public class MicroSpringBoot {
    public static void main(String[] args) {
        HttpServer.staticfiles("page");
        HttpServer.start(args);
    }
}
```

---

## Arquitectura

El proyecto sigue una **arquitectura MVC simplificada**:

* **HttpServer** → Recibe las solicitudes, enruta a controladores o archivos estáticos.
* **Request** → Parseo de la solicitud HTTP, extracción de parámetros.
* **Response** → Construcción y envío de la respuesta HTTP.
* **Anotaciones** → `@RestController`, `@GetMapping`, `@RequestParam`.

---

## Pruebas rápidas

Con `curl` o navegador:

```bash
curl "http://localhost:35000/hello"
curl "http://localhost:35000/greeting?name=Geronimo"
```

Archivos estáticos:

```
http://localhost:35000/index.html
http://localhost:35000/image.png
```

---

## Deployment

1. Asegúrate de que el puerto 35000 esté disponible.
2. Ejecuta el JAR en un servidor con Java instalado:

```bash
java -cp target/classes com.mimirandoms.arep1.microspringboot.MicroSpringBoot
```

3. Coloca los archivos estáticos en la carpeta configurada (`page/`).

---

## Built With

* [Java](https://www.java.com/) – Lenguaje principal.
* [Maven](https://maven.apache.org/) – Gestión de dependencias y ciclo de vida.

---

## Authors

* **Geronimo Martinez** – *Alias MimiRandomS* – [GitHub](https://github.com/MimiRandomS)
