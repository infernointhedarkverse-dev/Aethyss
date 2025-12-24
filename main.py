import os
import google.generativeai as genai
from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware

# =========================
# Gemini API Configuration
# =========================
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")
if not GEMINI_API_KEY:
    raise RuntimeError("GEMINI_API_KEY not set")

genai.configure(api_key=GEMINI_API_KEY)

# v1beta-safe model (this one WORKS)
model = genai.GenerativeModel("models/gemini-1.0-pro")

# =========================
# FastAPI App
# =========================
app = FastAPI(
    title="Aethyss Cloud Backend",
    version="2.0.0",
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
        response = model.generate_content(req.message)
        return {"reply": response.text}
    except Exception as e:
        return {"reply": f"AI error: {str(e)}"}
