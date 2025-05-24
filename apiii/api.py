from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session, sessionmaker
from pydantic import BaseModel, ValidationError
from typing import List
from database import *
import logging

# Настройка логирования
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Создание таблиц в базе данных
Base.metadata.create_all(bind=engine)

app = FastAPI()
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Dependency
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

class UserLogin(BaseModel):
    login: str
    password: str

class UserRegister(BaseModel):
    name: str
    surname: str
    height: float
    weight: float
    gender: bool
    birthday: str
    password: str
    login: str
    lifestyleID: int
    allergies: List[int] = []
    preferences: List[int] = []

@app.post("/login")
def login(user_login: UserLogin, db: Session = Depends(get_db)):
    user = db.query(User).filter(User.login == user_login.login).first()
    if not user or user.password != user_login.password:
        raise HTTPException(status_code=400, detail="Invalid login or password")
    return {"message": "Login successful", "user_id": user.id}

@app.post("/register")
def register(user_register: UserRegister, db: Session = Depends(get_db)):
    try:
        # Проверка, существует ли уже пользователь с таким логином
        existing_user = db.query(User).filter(User.login == user_register.login).first()
        if existing_user:
            raise HTTPException(status_code=400, detail="Login already exists")

        # Создание нового пользователя
        new_user = User(
            name=user_register.name,
            surname=user_register.surname,
            height=user_register.height,
            weight=user_register.weight,
            gender=user_register.gender,
            birthday=user_register.birthday,
            password=user_register.password,
            login=user_register.login,
            lifestyleID=user_register.lifestyleID
        )

        # Добавление нового пользователя в базу данных
        db.add(new_user)
        db.commit()
        db.refresh(new_user)

        # Добавление аллергий и предпочтений
        for allergy_id in user_register.allergies:
            db.add(UserAllergy(user_id=new_user.id, allergy_id=allergy_id))
        for preference_id in user_register.preferences:
            db.add(UserPreference(user_id=new_user.id, preference_id=preference_id))

        db.commit()

        return {"message": "Registration successful", "user_id": new_user.id}
    except ValidationError as e:
        logger.error(f"Validation error: {e.json()}")
        raise HTTPException(status_code=422, detail=e.json())
    except Exception as e:
        logger.error(f"Registration failed: {e}")
        raise HTTPException(status_code=500, detail="Registration failed")
