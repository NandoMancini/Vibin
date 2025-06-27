from flask import Flask, request, jsonify
from transformers import T5Tokenizer, T5ForConditionalGeneration

app = Flask(__name__)

model = T5ForConditionalGeneration.from_pretrained("mrm8488/t5-base-finetuned-emotion")
tokenizer = T5Tokenizer.from_pretrained("mrm8488/t5-base-finetuned-emotion")

@app.route("/analyze", methods=["POST"])
def analyze():
    data = request.get_json()
    text = data.get("text", "")
    
    input_text = f"emotion: {text}"
    input_ids = tokenizer.encode(input_text, return_tensors="pt")

    output = model.generate(input_ids)
    emotion = tokenizer.decode(output[0], skip_special_tokens=True)
    
    return jsonify({"emotion": emotion})

if __name__ == "__main__":
    app.run(port=5005)