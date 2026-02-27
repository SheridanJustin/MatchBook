from fastapi import FastAPI
from pydantic import BaseModel
import requests
import json

app = FastAPI()

class BookRequest(BaseModel):
    prompt: str


def generate_books_from_ollama(prompt: str):
    url = "http://localhost:11434/api/generate"

    payload = {
        "model": "qwen2.5:3b-instruct",
        "prompt": f"""
You are a book recommendation engine.
The user wrote: "{prompt}"

Recommend EXACTLY 5 REAL, VERIFIED books in the following JSON structure:

{{
  "books": [
    {{
      "title": "string",
      "author": "string",
      "reason": "string"
    }}
  ]
}}

Rules:
- All books MUST exist in real life.
- All authors MUST be real.
- DO NOT include any thinking process.
- ONLY output valid JSON.
""",
        "format": "json",
        "stream": False
    }

    # --- Call Ollama ---
    response = requests.post(url, json=payload)
    result = response.json()

    print("\n=== RAW OLLAMA OUTPUT ===")
    print(result)
    print("=== RAW TEXT ===")
    print(result.get("response", ""))

    return result


@app.post("/recommend")
async def recommend_books(request: BookRequest):
    try:
        result = generate_books_from_ollama(request.prompt)

        # Parse JSON from the model's response
        parsed = json.loads(result["response"])

        return parsed

    except Exception as e:
        return {"error": str(e)}
