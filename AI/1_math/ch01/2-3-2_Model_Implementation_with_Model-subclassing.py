from tensorflow.keras.layers import Dense

from tensorflow.keras.models import Model

class TestModel(Model):
    def __init__(self):
        super(TestModel, self).__init__()
    
        self.dense1 = Dense(units=10, activation='sigmoid')
        self.dense2 = Dense(units=20, activation='sigmoid')
    

model = TestModel()
# print(model.dense1)
# print(model.dense2)