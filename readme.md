
# ECI_AREP - Arq. de Soluciones Empresariales e IA

Repositorio monorepo que contiene talleres, parciales y proyectos desarrollados para la asignatura **Arquitectura de Soluciones Empresariales e Inteligencia Artificial (AREP)**.

Este repositorio agrupa múltiples servicios backend, microservicios, aplicaciones web y ejercicios prácticos enfocados en arquitectura empresarial, servicios distribuidos e integración con IA.

---

## 📁 Estructura del Proyecto

```
ECI_AREP/
│
├── Parciales/
│   ├── Primer Parcial/
│   │   ├── assets/
│   │   ├── backend/
│   │   └── facade/
│   │
│   ├── Segundo Parcial/
│   │   ├── math-service/
│   │   ├── proxy-service/
│   │   └── other/
│   │
│   └── Tercer Parcial/
│
├── Talleres/
│   ├── enunciados/
│   ├── IA/
│   ├── taller1/
│   ├── taller2/
│   ├── taller3/
│   ├── taller4/
│   ├── taller5/
│   ├── taller6/
│   ├── tallerLLM/
│   └── tareaLLM/
│
├── Quices/
│
└── Presentaciones/

```

---

## 🛠 Tecnologías Utilizadas

### Backend

* Java 17+
* Spring Boot
* Maven
* JUnit
* Arquitectura REST
* Microservicios

### IA

* Integración con LLMs
* Servicios de IA básicos (hello_ai, tareas LLM)

### Frontend / Web

* HTML / CSS
* JavaScript
* Recursos estáticos (static)

### Herramientas

* Git
* Maven
* Node.js (según taller)
* IntelliJ / VSCode

---

## ⚙️ Cómo ejecutar los proyectos

### 🔹 Backend (Spring Boot / Servicios)

Entrar a la carpeta del servicio correspondiente:

```bash
cd Parciales/Segundo Parcial/math-service
```

Compilar y ejecutar:

```bash
mvn clean install
mvn spring-boot:run
```

O ejecutar el `.jar` generado en `target/`.

---

### 🔹 Talleres Java simples

```bash
cd Talleres/taller1
mvn clean install
mvn test
```

---

## 🧪 Pruebas

Para ejecutar tests en cualquier módulo backend:

```bash
mvn test
```

Las pruebas unitarias se encuentran en:

```
src/test/java/
```

---

## 🏗 Arquitectura General

Los proyectos backend siguen una arquitectura en capas:

* `controller` → Exposición de endpoints REST
* `service` → Lógica de negocio
* `repository` → Acceso a datos
* `model` → Entidades
* `config` → Configuraciones
* `security` → Configuración de seguridad (según taller)
* `resources/static` → Archivos web estáticos

En los parciales avanzados se implementan:

* Arquitectura basada en microservicios
* Proxy service
* Separación de responsabilidades (Facade pattern)
* Integración con servicios externos
* Componentes de IA y LLM

