# Taller 4 – Modularización con virtualización e Introducción a Docker

Este proyecto implementa un **servidor web minimalista en Java puro**, capaz de atender solicitudes HTTP, servir archivos estáticos y manejar rutas dinámicas con handlers definidos por el usuario.  
El servidor fue extendido para soportar **concurrencia** y un **apagado elegante**, y se desplegó en AWS utilizando **Docker y EC2**.

---

## ✨ Funcionalidades principales
- Servir páginas HTML, CSS, JS e imágenes desde una carpeta estática (`page/`).
- Definir endpoints dinámicos con `HttpServer.get(path, handler)`.
- Manejo de solicitudes concurrentes mediante `ExecutorService`.
- Apagado controlado del servidor.
- Empaquetado y despliegue en **Docker**.
- Publicación de la imagen en **DockerHub**.
- Despliegue en **AWS EC2** con acceso público.

---

## 🚀 Getting Started

### 📋 Prerrequisitos
- Java JDK 17+
- Maven
- Docker
- Cuenta en DockerHub
- Instancia AWS EC2 con Docker instalado

---

### 🔧 Instalación y ejecución local

1. Clona el repositorio:
   ```bash
   git clone https://github.com/TUUSUARIO/AREP-taller3
   cd AREP-taller3
   
2. Compila el proyecto:

   ```bash
   mvn clean package -DskipTests
   ```

3. Ejecuta el servidor localmente:

   ```bash
   java -cp "target/classes:target/dependency/*" edu.eci.arep.taller.App
   ```

4. Abre tu navegador en:

   ```
   http://localhost:5000
   http://localhost:5000/hello?name=Geronimo
   http://localhost:5000/pi
   ```

---

## 📂 Uso del Framework

### Archivos estáticos

Configura la carpeta de archivos estáticos en tu `main`:

```java
HttpServer.staticfiles("page");
```

Accede a ellos desde el navegador:

```
http://localhost:5000/index.html
http://localhost:5000/styles.css
http://localhost:5000/image.png
```

### Endpoints dinámicos

Ejemplo de definición de endpoints en `App.java`:

```java
package edu.eci.arep.taller;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        HttpServer.staticfiles("page");

        HttpServer.get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        HttpServer.get("/pi", (req, res) -> String.valueOf(Math.PI));

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "5000"));
        HttpServer.start(port);
    }
}
```

Acceso:

```
http://localhost:5000/hello?name=Geronimo
http://localhost:5000/pi
```

---

## 🏗️ Arquitectura

El proyecto sigue una arquitectura simple inspirada en MVC:

* **HttpServer** → Recibe solicitudes, maneja concurrencia, enruta a controladores o archivos estáticos.
* **Request** → Parseo de la solicitud HTTP y parámetros.
* **Response** → Construcción y envío de la respuesta.
* **RouteHandler** → Interfaz funcional para definir endpoints dinámicos.

---

## ⚡ Docker

### Construir la imagen

```bash
docker build -t tallerarep .
```

### Correr un contenedor

```bash
docker run -d -p 34000:6000 --name Apptallerarep tallerarep
```

### Subir a DockerHub

```bash
docker tag tallerarep TUUSUARIO/NOMBREREPO:latest
docker push TUUSUARIO/NOMBREREPO:latest
```

---

## ☁️ Despliegue en AWS EC2

1. Conectar a la instancia:

   ```bash
   ssh -i "tu-llave.pem" ec2-user@<PUBLIC_DNS_EC2>
   ```

2. Instalar Docker (Amazon Linux):

   ```bash
   sudo yum update -y
   sudo yum install docker -y
   sudo service docker start
   sudo usermod -a -G docker ec2-user
   exit
   ```

3. Descargar y correr la imagen:

   ```bash
   docker pull TUUSUARIO/NOMBREREPO:latest
   docker run -d -p 42000:6000 --name tallerarepaws TUUSUARIO/NOMBREREPO:latest
   ```

4. Abrir el puerto `42000` en el **Security Group** de AWS.

5. Probar en el navegador:

   ```
   http://<PUBLIC_DNS_EC2>:42000/hello?name=Geronimo
   http://<PUBLIC_DNS_EC2>:42000/index.html
   ```

---

## 🧪 Pruebas rápidas

Con `curl`:

```bash
curl "http://localhost:5000/hello?name=Geronimo"
curl "http://localhost:5000/pi"
```

Archivos estáticos:

```bash
curl "http://localhost:5000/index.html"
```

---

## 🎥 Video de prueba

👉 [Ver demostración en YouTube](https://youtu.be/DDsuTQQrjZM)

---

## 📦 Entregables

1. **Código del proyecto** en un repositorio de GitHub.
2. **README** con:

    * Resumen del proyecto.
    * Arquitectura y diseño de clases.
    * Pasos para generar las imágenes Docker y desplegarlas.
    * Capturas/ejemplos del despliegue.
3. **Video** con los despliegues funcionando ([enlace aquí](https://youtu.be/DDsuTQQrjZM)).

---

## 🛠️ Built With

* **Java** – Lenguaje principal.
* **Maven** – Gestión de dependencias y ciclo de vida.
* **Docker** – Contenedores.
* **AWS EC2** – Despliegue en la nube.

---

## ✍️ Author

**Geronimo Martinez** – GitHub: [MimiRandomS](https://github.com/MimiRandomS)
