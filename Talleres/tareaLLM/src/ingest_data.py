import os
from dotenv import load_dotenv
from pinecone import Pinecone, ServerlessSpec
from langchain_pinecone import PineconeVectorStore
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_huggingface import HuggingFaceEmbeddings

# Cargar variables
load_dotenv()

# Inicializar Pinecone
pc = Pinecone(api_key=os.getenv("PINECONE_API_KEY"))

# Usar un NUEVO nombre de índice para embeddings locales
index_name = "rag-demo-local-index"  # <- NUEVO NOMBRE
dimension = 384  # Dimensión de all-MiniLM-L6-v2

print("✅ Pinecone inicializado")

# Verificar si el índice existe, si no CREARLO
if index_name not in pc.list_indexes().names():
    print(f"📦 Creando nuevo índice '{index_name}' con dimensión {dimension}...")
    pc.create_index(
        name=index_name,
        dimension=dimension,
        metric="cosine",
        spec=ServerlessSpec(
            cloud="aws",
            region="us-east-1"
        )
    )
    print(f"✅ Índice {index_name} creado")
else:
    print(f"✅ Índice {index_name} encontrado")

# VERIFICAR SI YA EXISTEN DATOS EN EL NUEVO ÍNDICE
try:
    index = pc.Index(index_name)
    stats = index.describe_index_stats()
    
    if stats.total_vector_count > 0:
        print("🚫 YA hay datos en Pinecone. No necesitas re-ingestar.")
        print(f"📊 Vectores existentes: {stats.total_vector_count}")
        print("💡 Si quieres re-ingestar, borra el índice primero")
        exit()
    else:
        print("📤 No hay datos existentes, procediendo con ingestión...")
except Exception as e:
    print(f"⚠️ Error verificando datos existentes: {e}")
    print("📤 Continuando con ingestión...")

# Usar embeddings LOCALES y GRATUITOS
print("🔄 Cargando embeddings locales (gratuitos)...")
embeddings = HuggingFaceEmbeddings(
    model_name="sentence-transformers/all-MiniLM-L6-v2",
    model_kwargs={'device': 'cpu'},
    encode_kwargs={'normalize_embeddings': True}
)
print("✅ Embeddings locales inicializados")

# Cargar y procesar documentos
texts = []
data_dir = "data"
for file in os.listdir(data_dir):
    file_path = os.path.join(data_dir, file)
    if os.path.isfile(file_path) and file.endswith('.txt'):
        with open(file_path, "r", encoding="utf-8") as f:
            texts.append(f.read())
            print(f"📖 {file}")

print(f"📄 {len(texts)} documentos cargados")

# Dividir textos
splitter = RecursiveCharacterTextSplitter(chunk_size=800, chunk_overlap=100)
docs = splitter.create_documents(texts)
print(f"🔪 {len(docs)} fragmentos creados")

# Indexar usando langchain-pinecone
print("📤 Indexando...")
vectorstore = PineconeVectorStore.from_documents(
    documents=docs,
    embedding=embeddings,
    index_name=index_name
)
print("✅ Documentos indexados")

# Verificar
index = pc.Index(index_name)
stats = index.describe_index_stats()
print(f"📊 Vectores en índice: {stats.total_vector_count}")