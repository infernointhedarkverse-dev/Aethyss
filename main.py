from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware
import os
import google.generativeai as genai

# =========================
# Gemini Configuration
# =========================
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")

if not GEMINI_API_KEY:
    raise RuntimeError("GEMINI_API_KEY not set in environment variables")

genai.configure(api_key=GEMINI_API_KEY)
model = genai.GenerativeModel("gemini-pro")

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
# Models
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
        # Generate AI response using Gemini
        response = model.generate_content(req.message)
        return {"reply": response.text}
    except Exception as e:
        # Return a safe error message instead of crashing
        return {"reply": f"AI error: {str(e)}"}
