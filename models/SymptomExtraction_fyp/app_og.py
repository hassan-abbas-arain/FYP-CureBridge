from model import apply

def main():
    print("Symptom Detection")
    while True:
        input_text = input("\nEnter medical text (or type 'exit' to quit): ").strip()

        if input_text.lower() == 'exit':
            print("Exiting the program. Goodbye!")
            break

        # Applying symptom detection
        entities = apply(input_text)
        
        print("\nExtracted Symptoms:")
        for entity in entities:
            print(f"- {entity}")

if __name__ == "__main__":
    main()