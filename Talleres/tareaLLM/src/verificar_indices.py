# verificar_indices.py
from pinecone import Pinecone
import os
from dotenv import load_dotenv

load_dotenv()
pc = Pinecone(api_key=os.getenv("PINECONE_API_KEY"))

print("📊 Índices disponibles:")
for index_name in pc.list_indexes().names():
    index = pc.Index(index_name)
    stats = index.describe_index_stats()
    print(f"  - {index_name}: {stats.total_vector_count} vectores, dim: {stats.dimension}")