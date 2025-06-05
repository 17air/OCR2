# OCR2

This repository contains an example of using a TensorFlow Lite (TFLite) model.

## Usage
1. Copy your `.tflite` model file to the `models` directory with the name `model.tflite`.
2. Install the required dependencies:
   ```bash
   pip install tensorflow
   ```
3. Run the inference script:
   ```bash
   python run_inference.py
   ```

The script will load `models/model.tflite`, run a dummy inference with zeros, and print the output.
