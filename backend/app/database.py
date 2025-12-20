from psycopg import connect
import os

DATABASE_URL = os.getenv("DATABASE_URL")

async def create_db_and_tables():
    # Placeholder – real DB setup happens in server/CI environment
    if not DATABASE_URL:
        print("DATABASE_URL not set, skipping DB init")
        return
    print("DB init placeholder")
