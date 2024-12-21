import os
from flask import Flask, request, jsonify, session
from flask_cors import CORS, cross_origin
import pymysql
import bcrypt
import logging
import jwt
import datetime
import mysql.connector
import base64


app = Flask(__name__)
CORS(app)  # 전체 애플리케이션에 CORS 적용
app.secret_key = os.environ.get('SECRET_KEY', 'your_default_secret_key')



# MySQL 연결
try:
    db = pymysql.connect(
        host="10.200.71.64",
        user="mobile",
        password="0000",
        database="with_animals"
    )
except Exception as e:
    logging.error(f"Database connection error: {e}")

    
def ensure_mysql_connection():
    if not db.open:
        db.ping(reconnect=True)

# 로깅 설정
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s",
    handlers=[
        logging.StreamHandler(),
        logging.FileHandler("server.log")
    ]
)

@app.before_request
def log_request_data_and_info():
    try:
        ensure_mysql_connection()
        logging.info(f"Request: {request.method} {request.url}")
        if request.method not in ['GET', 'OPTIONS'] and request.json:
            logging.info(f"Request JSON: {repr(request.json)[:500]}")
        auth_header = request.headers.get('Authorization', '')
        if auth_header:
            logging.info(f"Authorization Header: {auth_header}")
        else:
            logging.warning("Authorization 헤더가 누락되었습니다.")
    except Exception as e:
        logging.error(f"Error in log_request_data_and_info: {e}")

@app.after_request
def log_response_data(response):
    try:
        logging.info(f"Response: {response.status_code}")
        if response.is_json:
            logging.info(f"Response JSON: {repr(response.get_json())[:500]}")
    except Exception as e:
        logging.error(f"Response logging error: {e}")
    return response


def token_required(f):
    def decorator(*args, **kwargs):
        token = request.headers.get('Authorization')
        if not token:
            return jsonify({"error": "Token is missing!"}), 403
        try:
            data = jwt.decode(token.split()[1], app.secret_key, algorithms=["HS256"])
            current_user = data["user_id"]
        except jwt.ExpiredSignatureError:
            return jsonify({"error": "Token has expired!"}), 403
        except jwt.InvalidTokenError:
            return jsonify({"error": "Invalid token!"}), 403
        return f(current_user, *args, **kwargs)
    return decorator




@app.route('/join', methods=['POST'])
def signup():
    try:
        data = request.json or {}
        user_id = data.get("user_id")
        user_name = data.get("user_name")
        email = data.get("email")
        password = data.get("password")
        phone = data.get("phone")

        if not user_id or not user_name or not email or not password or not phone:
            return jsonify({"error": "All fields are required"}), 400

        hashed_password = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())

        cursor = db.cursor()
        query = """
        INSERT INTO users (user_id, user_name, email, password, phone)
        VALUES (%s, %s, %s, %s, %s)
        """
        cursor.execute(query, (user_id, user_name, email, hashed_password.decode('utf-8'), phone))
        db.commit()
        return jsonify({"message": "User registered successfully"}), 201
    except Exception as e:
        logging.error(f"Error in /join: {e}")
        return jsonify({"error": str(e)}), 500

@app.route('/login', methods=['POST'])
def login():
    try:
        data = request.json
        user_id = data.get('user_id')
        password = data.get('password')

        if not user_id or not password:
            return jsonify({"error": "Both user_id and password are required"}), 400

        cursor = db.cursor()
        query = "SELECT user_id, user_name, email, password, phone FROM users WHERE user_id = %s"
        cursor.execute(query, (user_id,))
        result = cursor.fetchone()

        if result:
            stored_password = result[3]
            if bcrypt.checkpw(password.encode('utf-8'), stored_password.encode('utf-8')):
                # JWT 토큰 생성
                token_payload = {
                    "user_id": result[0],
                    "exp": datetime.datetime.utcnow() + datetime.timedelta(hours=1)  # 1시간 유효
                }
                token = jwt.encode(token_payload, app.secret_key, algorithm="HS256")
                session['user_id'] = result[0]

                return jsonify({
                    "message": "Login successful",
                    "token": token,  # 토큰 직접 포함
                    "user_data": {
                        "user_id": result[0],
                        "user_name": result[1],
                        "email": result[2],
                        "phone": result[4]
                    }
                }), 200
            else:
                return jsonify({"error": "Invalid password"}), 401
        else:
            return jsonify({"error": "User not found"}), 404

    except Exception as e:
        logging.error(f"Error in /login: {e}")
        return jsonify({"error": str(e)}), 500



@app.route('/pets', methods=['POST'])
def get_pet():
    data = request.json
    pet_name = data.get('pet_name')
    species = data.get('species')
    age = data.get('age')
    gender = data.get('gender')
    photo_url = data.get('photo_url')
    user_id = data.get('user_id')  

    cursor = db.cursor()
    try:
        # user_id 존재 여부 확인
        cursor.execute("SELECT 1 FROM users WHERE user_id = %s", (user_id,))
        if cursor.fetchone() is None:
            return jsonify({"error": "User ID not found"}), 404

        # pet 데이터 삽입
        query = """
        INSERT INTO pet (user_id, pet_name, species, age, gender, photo_url)
        SELECT user_id, %s, %s, %s, %s, %s
        FROM users
        WHERE user_id = %s
        """
        cursor.execute(query, (pet_name, species, age, gender, photo_url, user_id))
        db.commit()
        return jsonify({"message": "Pet registered successfully"}), 201
    except Exception as e:
        db.rollback()
        return jsonify({"error": str(e)}), 500


@app.route('/pets', methods=['GET'])
def get_pets():
    user_id = request.args.get('user_id')  # URL의 쿼리 매개변수에서 user_id 가져오기
    logging.info(f"GET /pets user_id: {user_id}")
    
    if not user_id:
        return jsonify({"error": "User ID is required"}), 400

    try:
        cursor = db.cursor()
        query = """
        SELECT pet_id, pet_name, species, age, gender, photo_url
        FROM pet
        WHERE user_id = %s
        ORDER BY pet_id ASC
        """
        cursor.execute(query, (user_id,))
        pets = cursor.fetchall()

        result = [
            {
                "pet_id": pet[0],
                "pet_name": pet[1],
                "species": pet[2],
                "age": pet[3],
                "gender": pet[4],
                "photo_url": pet[5]
            }
            for pet in pets
        ]

        return jsonify(result), 200
    except Exception as e:
        logging.error(f"Error in /pets: {e}")
        return jsonify({"error": "Failed to fetch pet data"}), 500
    finally:
        cursor.close()

# 사진 업로드 및 DB 저장
@app.route('/upload/photo', methods=['POST'])
def upload_photo():
    # 파일이 없으면 에러 반환
    if 'photo' not in request.files:
        return jsonify({"error": "No photo uploaded"}), 400

    photo = request.files['photo']
    description = request.form.get('description', 'No description')
    user_id = request.form.get('user_id')

    if not user_id or not description:
        return jsonify({"error": "User ID and description are required"}), 400

    cursor = db.cursor()
    try:
        # BLOB 저장을 위해 파일 읽기
        photo_data = photo.read()
        encoded_photo = base64.b64encode(photo_data).decode('utf-8')
        
        query = """
        INSERT INTO lost_pets (name, species, lost_date, lost_location, description, photo_blob, user_id)
        VALUES (%s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(query, ('이름', '종', '2024-12-21', '장소', description, encoded_photo, user_id))
        db.commit()
        return jsonify({"message": "Photo uploaded successfully"}), 201

    except Exception as e:
        db.rollback()
        return jsonify({"error": str(e)}), 500


# BLOB으로 저장된 이미지 조회
@app.route('/lost_pets/<int:lost_pet_id>', methods=['GET'])
def get_lost_pet(lost_pet_id):
    try:
        cursor = db.cursor(dictionary=True)
        query = "SELECT * FROM lost_pets WHERE lost_pet_id = %s"
        cursor.execute(query, (lost_pet_id,))
        pet = cursor.fetchone()

        if not pet:
            return jsonify({"error": "Lost pet not found"}), 404

        # BLOB 데이터 Base64로 디코딩 후 반환
        if pet['photo_blob']:
            pet['photo_blob'] = base64.b64decode(pet['photo_blob']).decode('utf-8')

        return jsonify(pet), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500



@app.route('/lost_pets/<int:lost_pet_id>', methods=['PUT'])
def update_lost_pet(lost_pet_id):
    try:
        data = request.json
        name = data.get('name')
        species = data.get('species')
        gender = data.get('gender')
        description = data.get('description')
        lost_date = data.get('lost_date')
        lost_location = data.get('lost_location')
        photo_url = data.get('photo_url')

        cursor = db.cursor()
        query = """
        UPDATE lost_pets
        SET name = %s, species = %s, gender = %s, description = %s, 
            lost_date = %s, lost_location = %s, photo_url = %s
        WHERE lost_pet_id = %s
        """
        cursor.execute(query, (name, species, gender, description, lost_date, lost_location, photo_url, lost_pet_id))
        db.commit()
        
        if cursor.rowcount == 0:
            return jsonify({"error": "Lost pet not found"}), 404
        return jsonify({"message": "Lost pet updated successfully"}), 200
    except Exception as e:
        logging.error(f"Error in PUT /lost_pets/{lost_pet_id}: {e}")
        return jsonify({"error": str(e)}), 500


@app.route('/lost_pets/<int:lost_pet_id>', methods=['DELETE'])
def delete_lost_pet(lost_pet_id):
    try:
        cursor = db.cursor()
        query = "DELETE FROM lost_pets WHERE lost_pet_id = %s"
        cursor.execute(query, (lost_pet_id,))
        db.commit()

        if cursor.rowcount == 0:
            return jsonify({"error": "Lost pet not found"}), 404
        return jsonify({"message": "Lost pet deleted successfully"}), 200
    except Exception as e:
        logging.error(f"Error in DELETE /lost_pets/{lost_pet_id}: {e}")
        return jsonify({"error": str(e)}), 500



@app.route('/board/posts', methods=['POST'])
@cross_origin()
@token_required
def create_post():
    try:
        auth_header = request.headers.get('Authorization')
        if not auth_header or not auth_header.startswith("Bearer "):
            return jsonify({"error": "Authorization header missing or invalid"}), 401

        token = auth_header.split(" ")[1]
        try:
            payload = jwt.decode(token, app.secret_key, algorithms=["HS256"])
            user_id = payload.get("user_id")
        except jwt.ExpiredSignatureError:
            return jsonify({"error": "Token has expired"}), 401
        except jwt.InvalidTokenError:
            return jsonify({"error": "Invalid token"}), 401

        data = request.json
        title = data.get('title')
        content = data.get('content')
        photo_url = data.get('photo_url', None)

        if not all([title, content]):
            return jsonify({"error": "Title and content are required"}), 400

        cursor = db.cursor()
        query = """
        INSERT INTO board_posts (user_id, title, content, photo_url)
        VALUES (%s, %s, %s, %s)
        """
        cursor.execute(query, (user_id, title, content, photo_url))
        db.commit()

        return jsonify({"message": "Post created successfully"}), 201
    except Exception as e:
        db.rollback()
        logging.error(f"Error in /board/posts: {e}")
        return jsonify({"error": str(e)}), 500





@app.route('/board/posts', methods=['GET'])
def get_posts():
    cursor = db.cursor(pymysql.cursors.DictCursor)
    query = "SELECT post_id, title, content, created_at FROM board_posts ORDER BY created_at DESC"
    cursor.execute(query)
    posts = cursor.fetchall()
    return jsonify(posts), 200

@app.route('/board/posts/<int:post_id>', methods=['GET'])
def get_post_detail(post_id):
    cursor = db.cursor(pymysql.cursors.DictCursor)
    query = "SELECT * FROM board_posts WHERE post_id = %s"
    cursor.execute(query, (post_id,))
    post = cursor.fetchone()
    if not post:
        return jsonify({"error": "Post not found"}), 404
    return jsonify(post), 200

@app.route('/board/comments', methods=['POST'])

def add_comment(user_id):
    data = request.json
    post_id = data.get('post_id')
    content = data.get('content')

    if not all([post_id, content]):
        return jsonify({"error": "Post ID and content are required"}), 400

    cursor = db.cursor()
    query = "INSERT INTO board_comments (post_id, user_id, content) VALUES (%s, %s, %s)"
    cursor.execute(query, (post_id, user_id, content))
    db.commit()
    return jsonify({"message": "Comment added successfully"}), 201

@app.route('/board/comments', methods=['GET'])
def get_comments():
    post_id = request.args.get('post_id')
    if not post_id:
        return jsonify({"error": "post_id is required"}), 400

    cursor = db.cursor(pymysql.cursors.DictCursor)
    query = "SELECT * FROM board_comments WHERE post_id = %s ORDER BY created_at ASC"
    cursor.execute(query, (post_id,))
    comments = cursor.fetchall()
    return jsonify(comments), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
