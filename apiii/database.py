from sqlalchemy import create_engine, Column, Integer, String, Date, Time, Float, ForeignKey, Boolean, Text
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship

Base = declarative_base()

class Activity(Base):
    __tablename__ = 'activity'

    id = Column(Integer, primary_key=True, nullable=False, autoincrement=True)
    lifestyleName = Column(String(25), nullable=False)
    nameActivity = Column(String(50), nullable=False)
    duration = Column(Time, nullable=False)
    callories = Column(Float, nullable=True)

    users = relationship("User", back_populates="activity")

class Eating(Base):
    __tablename__ = 'eating'

    id = Column(Integer, primary_key=True, nullable=False, autoincrement=True)
    userID = Column(Integer, ForeignKey('users.id'), nullable=False)
    water = Column(Boolean, nullable=True)
    callories = Column(Float, nullable=True)
    squirrels = Column(Float, nullable=True)
    fats = Column(Float, nullable=True)
    carbohydrates = Column(Float, nullable=True)
    date = Column(Date, nullable=False)
    foodId = Column(Integer, ForeignKey('food.id'), nullable=False)

    user = relationship("User", back_populates="eatings")
    food = relationship("Food", back_populates="eatings")

class Food(Base):
    __tablename__ = 'food'

    id = Column(Integer, primary_key=True, nullable=False, autoincrement=True)
    photo = Column(Text, nullable=True)
    nameFood = Column(String(50), nullable=False)
    callories = Column(Float, nullable=True)
    squirrels = Column(Float, nullable=True)
    fats = Column(Float, nullable=True)
    carbohydrates = Column(Float, nullable=True)
    reciepID = Column(Integer, ForeignKey('reciep.id'), nullable=True)

    eatings = relationship("Eating", back_populates="food")
    reciep = relationship("Reciep", back_populates="foods")

class Reciep(Base):
    __tablename__ = 'reciep'

    id = Column(Integer, primary_key=True, nullable=False, autoincrement=True)
    name = Column(String(50), nullable=False)
    callories = Column(Float, nullable=True)
    photo = Column(Text, nullable=False)
    userID = Column(Integer, ForeignKey('users.id'), nullable=False)
    dateCreate = Column(Date, nullable=False)
    components = Column(Text, nullable=False)
    steps = Column(Text, nullable=False)
    squirrels = Column(Float, nullable=True)
    fats = Column(Float, nullable=True)
    carbohydrates = Column(Float, nullable=True)

    user = relationship("User", back_populates="recieps")
    foods = relationship("Food", back_populates="reciep")


class Allergy(Base):
    __tablename__ = 'allergy'

    id = Column(Integer, primary_key=True, nullable=False, autoincrement=True)
    name = Column(String(50), nullable=False, unique=True)

class Preference(Base):
    __tablename__ = 'preference'

    id = Column(Integer, primary_key=True, nullable=False, autoincrement=True)
    name = Column(String(50), nullable=False, unique=True)


class UserAllergy(Base):
    __tablename__ = 'user_allergy'

    user_id = Column(Integer, ForeignKey('users.id'), primary_key=True)
    allergy_id = Column(Integer, ForeignKey('allergy.id'), primary_key=True)

class UserPreference(Base):
    __tablename__ = 'user_preference'

    user_id = Column(Integer, ForeignKey('users.id'), primary_key=True)
    preference_id = Column(Integer, ForeignKey('preference.id'), primary_key=True)


class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True, nullable=False, autoincrement=True)
    name = Column(String(50), nullable=False)
    surname = Column(String(50), nullable=False)
    height = Column(Float, nullable=False)
    weight = Column(Float, nullable=False)
    gender = Column(Boolean, nullable=False)
    birthday = Column(Date, nullable=False)
    password = Column(String(50), nullable=False)
    login = Column(String(50), nullable=False)
    lifestyleID = Column(Integer, ForeignKey('activity.id'), nullable=False)

    activity = relationship("Activity", back_populates="users")
    eatings = relationship("Eating", back_populates="user")
    recieps = relationship("Reciep", back_populates="user")
    allergies = relationship("Allergy", secondary="user_allergy", back_populates="users")
    preferences = relationship("Preference", secondary="user_preference", back_populates="users")

Allergy.users = relationship("User", secondary="user_allergy", back_populates="allergies")
Preference.users = relationship("User", secondary="user_preference", back_populates="preferences")


# Создание подключения к базе данных
engine = create_engine("postgresql://postgres:1153@localhost:5432/test1", echo=True)

# Создание таблиц
Base.metadata.create_all(engine)
