class male_Person():
    def __init__(self, classes):
        print("I'm Person")
        self.gender = 'male'
        print(classes)

class Student(male_Person):
    def __init__(self, classes):
        super().__init__(classes)
        self.school = 'samsung'

sihun = Student('math')
print(sihun.gender)
print(sihun.school)