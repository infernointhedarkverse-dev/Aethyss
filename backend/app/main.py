from fastapi import FastAPI
from dotenv import load_dotenv

from .routers.chat import chat_router
from .database import create_db_and_tables

load_dotenv()

app = FastAPI(
    title="Aethyss AI Chat Engine",
    description="Customizable AI RP chatbot backend",
    version="1.0.0",
)

@app.on_event("startup")
async def on_startup():
    await create_db_and_tables()

app.include_router(chat_router)

@app.get("/health")
async def health_check():
    return {"status": "ok"}
