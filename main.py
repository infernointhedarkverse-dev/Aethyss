import os
from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware
from google import genai

# =========================
# Gemini Configuration
# =========================
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")
if not GEMINI_API_KEY:
    raise RuntimeError("GEMINI_API_KEY not set")

client = genai.Client(api_key=GEMINI_API_KEY)
MODEL_NAME = "models/gemini-1.5-flash"

# =========================
# FastAPI App
# =========================
app = FastAPI(
    title="Aethyss Cloud Backend",
    version="3.0.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# =========================
# Data Models
# =========================
class ChatRequest(BaseModel):
    message: str

class ChatResponse(BaseModel):
    reply: str

# =========================
# Routes
# =========================
@app.get("/")
def root():
    return {"status": "Aethyss backend online"}

@app.get("/health")
def health():
    return {"ok": True}

@app.post("/chat", response_model=ChatResponse)
def chat(req: ChatRequest):
    try:
        response = client.models.generate_content(
            model=MODEL_NAME,
            contents=req.message
        )
        return {"reply": response.text}
    except Exception as e:
        return {"reply": f"AI error: {str(e)}"}
