from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from model import apply

app = FastAPI()

class TextInput(BaseModel):
    text: str

@app.post("/predict")
def predict(input_data: TextInput):
    try:
        entities = apply(input_data.text)
        return {"extracted_symptoms": entities}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))