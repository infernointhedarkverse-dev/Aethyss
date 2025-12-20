from fastapi import APIRouter

chat_router = APIRouter(prefix="/chat", tags=["chat"])

@chat_router.post("/")
async def chat_endpoint(message: str):
    return {"reply": f"You said: {message}"}
