import os
from dotenv import load_dotenv
from pinecone import Pinecone, ServerlessSpec
from langchain_openai import ChatOpenAI, OpenAIEmbeddings
from langchain_pinecone import PineconeVectorStore
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser
from langchain_core.runnables import RunnablePassthrough

# 🔧 Cargar variables de entorno
load_dotenv()

# 🧠 Inicializar Pinecone SDK v4
pc = Pinecone(api_key=os.getenv("PINECONE_API_KEY"))
index_name = "rag-demo-index"

# Crear el índice si no existe
if index_name not in [idx["name"] for idx in pc.list_indexes().get("indexes", [])]:
    print("🆕 Creando índice en Pinecone...")
    pc.create_index(
        name=index_name,
        dimension=1536,  # tamaño del embedding de text-embedding-3-small
        metric="cosine",
        spec=ServerlessSpec(cloud="aws", region="us-east-1")
    )

print("✅ Índice conectado:", index_name)

# ⚙️ Embeddings
embeddings = OpenAIEmbeddings(model="text-embedding-3-small")

# 🗂️ VectorStore correcto (v4 compatible)
vectorstore = PineconeVectorStore.from_existing_index(
    index_name=index_name,
    embedding=embeddings
)

retriever = vectorstore.as_retriever(search_kwargs={"k": 3})

# 💬 Modelo
llm = ChatOpenAI(model="gpt-4o-mini", temperature=0.3)

# 🧩 Prompt base
prompt = ChatPromptTemplate.from_template("""
Responde a la siguiente pregunta usando solo el contexto proporcionado.
Sé claro, breve y preciso.

Contexto:
{context}

Pregunta:
{question}
""")

# 🪄 Construcción del pipeline (sin chains)
rag_chain = (
    {"context": retriever, "question": RunnablePassthrough()}
    | prompt
    | llm
    | StrOutputParser()
)

print("🚀 Sistema RAG inicializado correctamente.\n")

# 🧠 Interfaz de prueba
while True:
    query = input("💬 Pregunta (o 'exit'): ")
    if query.lower() in ["exit", "salir"]:
        break
    try:
        answer = rag_chain.invoke(query)
        print(f"\n🤖 {answer}\n")
    except Exception as e:
        print(f"❌ Error: {e}\n")
