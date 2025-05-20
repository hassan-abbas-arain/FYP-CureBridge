from fastapi import FastAPI
from pydantic import BaseModel
from api.model_runner import predict

app = FastAPI()

class TextInput(BaseModel):
    text: str

@app.post("/predict")
def predict_route(data: TextInput):
    result = predict(data.text)
    return {"prediction": result}
