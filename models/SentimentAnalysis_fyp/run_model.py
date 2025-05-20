import torch
import numpy as np
from transformers import XLNetForSequenceClassification, XLNetTokenizer

model_path = "./curebridge_xlnet_model"
tokenizer = XLNetTokenizer.from_pretrained(model_path)
model = XLNetForSequenceClassification.from_pretrained(model_path)

device = torch.device("cpu")
model.to(device)
model.eval()

example_text = ['''Doctor: Hello. How can I help you today?

Patient: I was hoping for someone more professional... you don’t look like a real doctor.

Doctor: I am a licensed professional here to assist you. Let's discuss your health.

Patient: I just don’t think people from your kind should be in this field.''']

# Tokenize and predict
encodings = tokenizer(example_text, truncation=True, padding=True, max_length=512, return_tensors="pt")
encodings = {key: value.to(device) for key, value in encodings.items()}

with torch.no_grad():
    outputs = model(**encodings)
    logits = outputs.logits
    predicted_label_idx = torch.argmax(logits, axis=-1).item()

label_mapping = {
    0: "Racial Discrimination",
    1: "Gender-Based Harassment",
    2: "Respectful",
    3: "Religious Discrimination"
}

predicted_label = label_mapping[predicted_label_idx]
print(f"Predicted label: {predicted_label}")