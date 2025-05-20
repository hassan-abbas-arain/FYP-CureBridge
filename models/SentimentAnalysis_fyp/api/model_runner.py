import torch
from transformers import XLNetTokenizer, XLNetForSequenceClassification

# Load model + tokenizer
model_path = "./curebridge_xlnet_model"
tokenizer = XLNetTokenizer.from_pretrained(model_path)
model = XLNetForSequenceClassification.from_pretrained(model_path)

device = torch.device("cpu")
model.to(device)
model.eval()

label_mapping = {
    0: "Racial Discrimination",
    1: "Gender-Based Harassment",
    2: "Respectful",
    3: "Religious Discrimination"
}

def predict(text):
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding=True, max_length=512)
    inputs = {k: v.to(device) for k, v in inputs.items()}
    with torch.no_grad():
        outputs = model(**inputs)
        logits = outputs.logits
        pred_idx = torch.argmax(logits, axis=-1).item()
    return label_mapping[pred_idx]
