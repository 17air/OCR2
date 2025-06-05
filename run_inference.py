import numpy as np
import tensorflow as tf

MODEL_PATH = 'models/model.tflite'

def load_model(path=MODEL_PATH):
    interpreter = tf.lite.Interpreter(model_path=path)
    interpreter.allocate_tensors()
    return interpreter

def run_dummy_inference(interpreter):
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()

    input_shape = input_details[0]['shape']
    dummy_input = np.zeros(input_shape, dtype=np.float32)
    interpreter.set_tensor(input_details[0]['index'], dummy_input)

    interpreter.invoke()
    output_data = interpreter.get_tensor(output_details[0]['index'])
    return output_data

if __name__ == '__main__':
    interp = load_model()
    result = run_dummy_inference(interp)
    print('Output:', result)
