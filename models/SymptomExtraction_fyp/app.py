from fastapi import FastAPI
from pydantic import BaseModel
from model import apply  

app = FastAPI()

class TextInput(BaseModel):
    text: str

@app.post("/extract")
async def extract_symptoms(input_data: TextInput):
    input_text = input_data.text.strip()
    entities = apply(input_text)
    return {"extracted_symptoms": entities}
