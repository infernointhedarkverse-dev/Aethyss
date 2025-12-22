from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI(
    title="Aethyss Backend",
    description="Cloud backend for Aethyss Android app",
    version="1.0.0",
)

# ---------- MODELS ----------

class ChatRequest(BaseModel):
    message: str

class ChatResponse(BaseModel):
    reply: str

# ---------- ROUTES ----------

@app.get("/health")
async def health():
    return {
        "status": "ok",
        "backend": "online",
    }

@app.post("/chat", response_model=ChatResponse)
async def chat(req: ChatRequest):
    # TEMP reply (LLM comes later)
    return ChatResponse(
        reply=f"You said: {req.message}"
    )
