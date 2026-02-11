# 📘 Retrieval-Augmented Generation (RAG) with OpenAI & Pinecone

**Autor:** Geronimo
**Repositorio:** [github.com/MimiRandomS](https://github.com/MimiRandomS)

---

## 🧠 Descripción del Proyecto

Este proyecto implementa un **sistema RAG (Retrieval-Augmented Generation)** utilizando **LangChain**, **OpenAI** y **Pinecone**.
El objetivo es crear una aplicación capaz de **responder preguntas con información contextual extraída de documentos locales**.
El sistema combina un **modelo generativo (LLM)** con un **motor de recuperación semántica (vector database)** para producir respuestas más precisas y fundamentadas.

---

## 🧩 Arquitectura General

```
📁 rag_openai_pinecone/
│
├── .env_example                  # Variables de entorno de ejemplo
├── requirements.txt              # Dependencias del proyecto
├── data/                         # Carpeta donde van los documentos (.txt)
│   └── Inteligencia_artificial.txt
└── src/
    ├── ingest_data.py            # Script que ingesta documentos en Pinecone
    ├── query_rag.py              # Script principal para consultar el RAG
    └── verificar_indices.py      # Script auxiliar para verificar índices
```

---

## ⚙️ Tecnologías Utilizadas

* **Python 3.11+**
* **LangChain** → Framework de orquestación para LLMs
* **OpenAI API** → Modelos GPT-4o-mini y text-embedding-3-small
* **Pinecone** → Base de datos vectorial (serverless)
* **dotenv** → Manejo de variables de entorno
* **tiktoken** → Tokenizador para embeddings
* **Sentence-Transformers / HuggingFace** → Alternativa para embeddings locales

---

## 🚀 Instalación y Configuración

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/MimiRandomS/rag_openai_pinecone.git
cd rag_openai_pinecone
```

### 2️⃣ Crear y activar entorno virtual

```bash
python -m venv .venv
.\.venv\Scripts\activate
```

### 3️⃣ Instalar dependencias

```bash
pip install -r requirements.txt
```

Contenido del `requirements.txt`:

```
langchain
langchain-community
openai
python-dotenv
tiktoken
langchain_openai
pinecone
langchain-text-splitters
langchain-huggingface
sentence-transformers
```

---

## 🔑 Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto (basado en `.env_example`):

```env
OPENAI_API_KEY=sk-...tu_clave_openai...
PINECONE_API_KEY=pcsk_...tu_clave_pinecone...
PINECONE_ENVIRONMENT=us-east-1
```
---

## 🧩 Paso a Paso: Ejecución

### 1️⃣ Ingestar los documentos

Guarda tus archivos `.txt` dentro de la carpeta `/data`. Agrega todos los documentos que requiera el RAG para dar respues entrenadas y acertadas.
Ejemplo:

```
data/
 └── Inteligencia_artificial.txt
```

Luego ejecuta:

```bash
python src/ingest_data.py
```

Esto:

* Divide los textos en fragmentos.
* Crea un índice en Pinecone (`rag-demo-local-index`).
* Inserta los embeddings generados.

✅ Verás mensajes como:

```
✅ Pinecone inicializado
📖 Inteligencia_artificial.txt
📄 1 documentos cargados
🔪 5 fragmentos creados
✅ Documentos indexados
📊 Vectores en índice: 5
```

---

### 2️⃣ Consultar el sistema RAG

Ejecuta:

```bash
python src/query_rag.py
```

Ejemplo de interacción:

```
✅ Índice conectado: rag-demo-index
🚀 Sistema RAG inicializado correctamente.

💬 Pregunta (o 'exit'): inteligencia artificial

🤖 La inteligencia artificial (IA) es un campo de la informática que busca crear sistemas capaces de realizar tareas que normalmente requieren inteligencia humana...
```

---

### 3️⃣ Verificar índices creados

```bash
python src/verificar_indices.py
```

Salida esperada:

```
📊 Índices disponibles:
  - rag-demo-index: 5 vectores, dim: 1536
  - rag-demo-local-index: 10 vectores, dim: 384
```

---

## 🧠 Concepto de RAG

**RAG (Retrieval-Augmented Generation)** combina dos componentes:

| Componente          | Función                                               | Tecnología            |
| ------------------- | ----------------------------------------------------- | --------------------- |
| **Retriever**       | Busca información relevante en una base vectorial     | Pinecone + Embeddings |
| **Generator (LLM)** | Genera una respuesta basada en el contexto recuperado | OpenAI GPT-4o-mini    |

📊 Flujo:

1. El usuario formula una pregunta.
2. Se buscan fragmentos similares (contexto) en Pinecone.
3. El contexto se envía al modelo de lenguaje.
4. El modelo responde con base en esa información.

---

## 📷 Ejemplo de Ejecución

```
💬 Pregunta (o 'exit'): ¿Qué es un inteligencia aritificial?
🤖 La inteligencia artificial es una rama de la ciencia de la computación dedicada a crear máquinas y sistemas que pueden realizar tareas que normalmente requieren de la inteligencia humana.
```

---

## 🧰 Estructura de Archivos Clave

| Archivo                | Descripción                                           |
| ---------------------- | ----------------------------------------------------- |
| `ingest_data.py`       | Carga documentos, los divide y los indexa en Pinecone |
| `query_rag.py`         | Crea el pipeline RAG (Retriever + Generator)          |
| `verificar_indices.py` | Lista índices existentes y sus estadísticas           |
| `.env_example`         | Ejemplo de configuración para API Keys                |
| `requirements.txt`     | Lista de dependencias del proyecto                    |

---

## 🧑‍💻 Autor

**Geronimo**
👤 GitHub: [@MimiRandomS](https://github.com/MimiRandomS)

---

## ⭐ Agradecimientos

* [LangChain Documentation](https://python.langchain.com/docs)
* [OpenAI API Docs](https://platform.openai.com/docs)
* [Pinecone Python SDK](https://github.com/pinecone-io/pinecone-python-client)
* Inspirado en el laboratorio:
  **"Introduction to Creating RAGs with OpenAI"**
