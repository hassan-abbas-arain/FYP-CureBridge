import pickle
import re
import pandas as pd
from nltk.corpus import stopwords, wordnet
from nltk.stem import WordNetLemmatizer
from nltk.tokenize import RegexpTokenizer
from itertools import combinations
from time import time
from collections import OrderedDict

# Load and initialize
stop_words = stopwords.words('english')
lemmatizer = WordNetLemmatizer()
splitter = RegexpTokenizer(r'\w+')

with open('Dataset/raw_disease_symptom_map.pickle', 'rb') as handle:
    dis_symp = pickle.load(handle)

diseases_symptoms_cleaned = OrderedDict()
total_symptoms = set()

# Clean and tokenize symptoms
for disease in sorted(dis_symp.keys()):
    raw = re.sub(r"\[\S+\]", "", dis_symp[disease]).lower().split(',')
    tokens = [s.strip() for s in raw if s.strip() and s.strip() != 'none']
    cleaned = []
    for sym in tokens:
        sym = sym.replace('-', ' ').replace("'", '').replace('(', '').replace(')', '')
        tokens_clean = [lemmatizer.lemmatize(w) for w in splitter.tokenize(sym) if w not in stop_words and not w[0].isdigit()]
        final_sym = ' '.join(tokens_clean)
        total_symptoms.add(final_sym)
        cleaned.append(final_sym)
    diseases_symptoms_cleaned[disease] = cleaned

total_symptoms = sorted(list(total_symptoms))
print(f"Unique symptoms (pre-similarity filtering): {len(total_symptoms)}")

# Using WordNet for synonyms for now
def synonyms(term):
    synonyms = []
    
    for syn in wordnet.synsets(term):
        synonyms += syn.lemma_names()
    return set(synonyms)

# Build synonym sets for each symptom 
sym_syn = dict()
for i, sym in enumerate(total_symptoms):
    if i % 20 == 0:
        print(f"Expanding synonyms for {i}/{len(total_symptoms)}: {sym}")
    words = sym.split()
    expanded = set()
    for r in range(1, len(words) + 1):
        for subset in combinations(words, r):
            expanded.update(synonyms(' '.join(subset)))
    expanded.add(sym)
    cleaned = ' '.join(expanded).replace('_', ' ').lower().split()
    sym_syn[sym] = sorted(set(cleaned))

print("Synonym expansion complete.")

# Jaccard-based merging
symptom_match = {}
total_symptoms = sorted(total_symptoms, key=len, reverse=True)

for i, symi in enumerate(total_symptoms):
    if i % 10 == 0:
        print(f" Comparing {i}/{len(total_symptoms)}: {symi}")
    for j in range(i + 1, len(total_symptoms)):
        symj = total_symptoms[j]
        set_i, set_j = set(sym_syn[symi]), set(sym_syn[symj])
        jaccard = len(set_i & set_j) / len(set_i | set_j)
        if jaccard > 0.75:
            print(f"Merged: {symj} → {symi}")
            symptom_match[symj] = symptom_match.get(symi, symi)

final_symptoms = sorted(set(total_symptoms) - set(symptom_match.keys()))
final_symptoms = ['label_dis'] + final_symptoms
print(f"Final unique symptoms (after merging): {len(final_symptoms) - 1}")

# Create dataset (normal + combination) 
df_norm_rows, df_comb_rows = [], []

for disease, symptoms in diseases_symptoms_cleaned.items():
    normalized = [symptom_match.get(sym, sym) for sym in symptoms]
    normalized = list(set(normalized))

    # Normal
    row = dict.fromkeys(final_symptoms, 0)
    row['label_dis'] = disease
    for sym in normalized:
        row[sym] = 1
    df_norm_rows.append(row)

    # Combos
    for r in range(1, len(normalized) + 1):
        for subset in combinations(normalized, r):
            row = dict.fromkeys(final_symptoms, 0)
            row['label_dis'] = disease
            for sym in subset:
                row[sym] = 1
            df_comb_rows.append(row)

# saving ti csv
df_norm = pd.DataFrame(df_norm_rows)
df_comb = pd.DataFrame(df_comb_rows)
df_norm.to_csv("Dataset/disease_symptom_matrix.csv", index=False)
df_comb.to_csv("Dataset/disease_symptom_combinations.csv", index=False)

with open('Dataset/cleaned_disease_symptom_list.txt', 'w', encoding='utf-8') as f:
    for dis, syms in diseases_symptoms_cleaned.items():
        print([dis] + syms, file=f)

print("Dataset saved successfully.")