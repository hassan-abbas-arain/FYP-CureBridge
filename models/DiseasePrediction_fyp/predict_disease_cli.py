import joblib
import pandas as pd
import warnings
from collections import Counter
from itertools import combinations
import operator
import requests
from bs4 import BeautifulSoup
from nltk.corpus import wordnet, stopwords
from nltk.stem import WordNetLemmatizer
from nltk.tokenize import RegexpTokenizer
import nltk

nltk.download('all')
warnings.simplefilter("ignore")

# Synonym expansion
def synonyms(term):
    synonyms = []
    try:
        response = requests.get(f'https://www.thesaurus.com/browse/{term}')
        soup = BeautifulSoup(response.content, "html.parser")
        container = soup.find('section', {'class': 'MainContentContainer'})
        if container:
            row = container.find('div', {'class': 'css-191l5o0-ClassicContentCard'})
            if row:
                for x in row.find_all('li'):
                    synonyms.append(x.get_text())
    except:
        pass

    for syn in wordnet.synsets(term):
        synonyms += syn.lemma_names()

    return set(synonyms)

# Preprocessing setup
stop_words = stopwords.words('english')
lemmatizer = WordNetLemmatizer()
splitter = RegexpTokenizer(r'\w+')

# Load dataset
df_norm = pd.read_csv("./Dataset/disease_symptom_matrix.csv")
X = df_norm.iloc[:, 1:]
Y = df_norm.iloc[:, 0]
dataset_symptoms = list(X.columns)

# User input
user_input = input("Please enter symptoms separated by commas:\n")
user_symptoms_raw = user_input.lower().split(',')

# Clean user input
processed_user_symptoms = []
for sym in user_symptoms_raw:
    sym = sym.strip().replace('-', ' ').replace("'", '')
    tokens = splitter.tokenize(sym)
    cleaned = ' '.join([lemmatizer.lemmatize(word) for word in tokens])
    processed_user_symptoms.append(cleaned)

# Expand query via synonyms
expanded_symptoms = []
for symptom in processed_user_symptoms:
    words = symptom.split()
    expansion = set()
    for r in range(1, len(words) + 1):
        for subset in combinations(words, r):
            term = ' '.join(subset)
            expansion.update(synonyms(term))
    expansion.add(symptom)
    expanded_symptoms.append(' '.join(expansion).replace('_', ' '))

print("\nAfter synonym expansion:")
print(expanded_symptoms)

# Match user symptoms with dataset symptoms
matched_symptoms = set()
for data_sym in dataset_symptoms:
    data_tokens = data_sym.split()
    for user_sym in expanded_symptoms:
        user_tokens = user_sym.split()
        match_count = sum(1 for token in data_tokens if token in user_tokens)
        if match_count / len(data_tokens) > 0.5:
            matched_symptoms.add(data_sym)

matched_symptoms = list(matched_symptoms)
print("\nTop matching symptoms:")
for i, sym in enumerate(matched_symptoms):
    print(f"{i}: {sym}")

# Ask user to confirm relevant symptoms 
selected_indices = input("\nSelect relevant symptoms by index (space-separated):\n").split()
final_symptoms = [matched_symptoms[int(idx)] for idx in selected_indices]

# Get diseases that have these symptoms
disease_set = set()
for sym in final_symptoms:
    disease_set.update(df_norm[df_norm[sym] == 1]['label_dis'])

# Suggest co-occurring symptoms 
counter = []
for dis in disease_set:
    row = df_norm[df_norm['label_dis'] == dis].iloc[0, 1:].values.tolist()
    for i, val in enumerate(row):
        if val == 1 and dataset_symptoms[i] not in final_symptoms:
            counter.append(dataset_symptoms[i])

symptom_counts = Counter(counter)
common_suggestions = sorted(symptom_counts.items(), key=operator.itemgetter(1), reverse=True)

# Ask for optional co-occurring symptoms
buffered = []
for i, (sym, _) in enumerate(common_suggestions, 1):
    buffered.append(sym)
    if i % 5 == 0 or i == len(common_suggestions):
        print("\n💡 Do you have any of these symptoms?")
        for j, s in enumerate(buffered):
            print(f"{j}: {s}")
        user_more = input("Enter indices (space-separated), 'no' to stop, '-1' to skip:\n").lower().split()
        if user_more[0] == 'no':
            break
        elif user_more[0] == '-1':
            buffered = []
            continue
        for idx in user_more:
            final_symptoms.append(buffered[int(idx)])
        buffered = []

# Final one-hot encoding of selected symptoms
print("\n Final Symptoms used for prediction:")
sample_x = [0] * len(dataset_symptoms)
for sym in final_symptoms:
    print(f" {sym}")
    if sym in dataset_symptoms:
        sample_x[dataset_symptoms.index(sym)] = 1

# Load trained model & predict disease
model = joblib.load("./Models/stacking_model_logistic_regression.joblib")
prediction = model.predict([sample_x])
print(f"\n Predicted Disease: {prediction[0]}")