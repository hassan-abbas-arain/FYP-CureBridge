import warnings
import joblib
import pandas as pd
import numpy as np
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split, cross_val_score
import os
os.makedirs("Models", exist_ok=True)

warnings.simplefilter("ignore")

# Load dataset
df_comb = pd.read_csv("./Dataset/disease_symptom_combinations.csv")

# Split features and label
X = df_comb.iloc[:, 1:]
Y = df_comb.iloc[:, 0]

# Train-test split
x_train, x_test, y_train, y_test = train_test_split(X, Y, test_size=0.10, random_state=42)

# Random Forest
rf_model_path = "./Models/RF_model.joblib"
if os.path.exists(rf_model_path):
    print("\nLoading pre-trained Random Forest model...")
    rf = joblib.load(rf_model_path)
else:
    print("\nTraining Random Forest model...")
    rf = RandomForestClassifier(n_estimators=100, criterion='entropy', random_state=42)
    rf.fit(x_train, y_train)
    joblib.dump(rf, rf_model_path)
    print(f"Model saved as {rf_model_path}")

rf_pred = rf.predict(x_test)
acc_rf = accuracy_score(y_test, rf_pred)
cv_rf = cross_val_score(rf, X, Y, cv=5).mean()
print(f"Random Forest - Test Accuracy: {acc_rf:.4f}, Cross-Validation: {cv_rf * 100:.2f}%")

# SVM 
svm_model_path = "./Models/SVM_model.joblib"
if os.path.exists(svm_model_path):
    print("\nLoading pre-trained SVM model...")
    svm = joblib.load(svm_model_path)
else:
    print("\nTraining SVM model...")
    svm = SVC(probability=True, random_state=42)
    svm.fit(x_train, y_train)
    joblib.dump(svm, svm_model_path)
    print(f"Model saved as {svm_model_path}")

svm_pred = svm.predict(x_test)
acc_svm = accuracy_score(y_test, svm_pred)
cv_svm = cross_val_score(svm, X, Y, cv=5).mean()
print(f"SVM - Test Accuracy: {acc_svm:.4f}, Cross-Validation: {cv_svm * 100:.2f}%")

# Decision Tree 
dt_model_path = "./Models/DT_model.joblib"
if os.path.exists(dt_model_path):
    print("\nLoading pre-trained Decision Tree model...")
    dt = joblib.load(dt_model_path)
else:
    print("\nTraining Decision Tree model...")
    dt = DecisionTreeClassifier(random_state=42)
    dt.fit(x_train, y_train)
    joblib.dump(dt, dt_model_path)
    print(f"Model saved as {dt_model_path}")

dt_pred = dt.predict(x_test)
acc_dt = accuracy_score(y_test, dt_pred)
cv_dt = cross_val_score(dt, X, Y, cv=5).mean()
print(f"Decision Tree - Test Accuracy: {acc_dt:.4f}, Cross-Validation: {cv_dt * 100:.2f}%")