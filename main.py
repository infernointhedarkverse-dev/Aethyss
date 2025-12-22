from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(
    title="Aethyss Cloud Backend",
    version="1.0.0",
)

# Allow Android app to connect
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class ChatRequest(BaseModel):
    message: str

class ChatResponse(BaseModel):
    reply: str

@app.get("/")
def root():
    return {"status": "Aethyss backend online"}

@app.get("/health")
def health():
    return {"ok": True}

@app.post("/chat", response_model=ChatResponse)
def chat(req: ChatRequest):
    # TEMP AI logic (safe, stable)
    user_msg = req.message.lower()

    if "hello" in user_msg:
        reply = "Hello. Aethyss is online and listening."
    elif "who are you" in user_msg:
        reply = "I am Aethyss, your AI companion."
    else:
        reply = f"You said: {req.message}"

    return {"reply": reply}
