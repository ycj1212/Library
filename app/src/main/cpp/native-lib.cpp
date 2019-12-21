#include <jni.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <time.h>
#include <ctime>
#include <iomanip>

using namespace std;

class UserDB {
private:
   string name;
   string userid;
   string pw;
public:
   UserDB(string name);
   void setUserid(string userid);
   void setPw(string pw);
   void setName(string name);
   string getUserid();
   string getPw();
   string getName();
   void print();
};

class BookDB {
private:
   string bookName;
   string author;
   string publisher;
   string year;
   string loaner;
   string loanDate;
   string returnDate;
public:
   BookDB(string Bookname);
   void setBookInfo();
   void setAuthor(string author);
   void setBookName(string bookName);
   void setPublisher(string publisher);
   void setYear(string year);
   void setLoaner(string loaner);
   void setLoanDate(string loanDate);
   void setReturnDate(string returnDate);
   string getAuthor();
   string getBookName();
   string getPublisher();
   string getYear();
   string getLoaner();
   string getLoanDate();
   string getReturnDate();
   void print();
   void printLoanState();
};

class Library {
private:
   static vector<UserDB> userDb;
   static vector<BookDB> bookDb;
public:
   static void userRegist();
   static void userRegist(string name, string id, string pw);
   static int userLogin(string userid, string pw);
   static int verify_id(string id);
   static int verify_pw(string pw, int i);
   static void readDB();
   static void readBookDB();
   static void writeUserDB();
   static void writeBookDB();
   static int getUserdbSize() {
      return userDb.size();
   }
   static int getBookdbSize() {
      return bookDb.size();
   }

   static string getName(int i) { return userDb[i].getName(); }
   static string getId(int i) { return userDb[i].getUserid(); }
   static string getPw(int i) { return userDb[i].getPw(); }

   string getBookName(int i) { return bookDb[i].getBookName(); }
   string getAuthor(int i) { return bookDb[i].getAuthor(); }
   string getPublisher(int i) { return bookDb[i].getPublisher(); }
   string getYear(int i) { return bookDb[i].getYear(); }
   string getLoaner(int i) { return bookDb[i].getLoaner(); }
   string getLoanDate(int i) { return bookDb[i].getLoanDate(); }
   string getreturnDate(int i) { return bookDb[i].getReturnDate(); }

   void printUserList();
   void printBookList();
   void setUserName(int num);
   void setUserPw(int num);
   void setLoan(int num, string id);
   void delLoan(int num, string id);
   void loanState(string id);
   string getUserName(int num);
   ~Library();
};

class User : public Library {
private:
   string userId, userName;
   int userKey;
public:
   User();
   User(string userId, int num, string name);
   User(const User& other);
   void userMenu();
   void myLoanState();   // 나의 대출 현황
   void loanBook(int bookidx);   // 대출하기
   void returnBook(int bookidx);   // 반납하기
   void searchBook();   // 책 검색
   void setInfo();      // 나의 정보 수정
   void setUserId(string id);
   void setUserKey(int key);
   void setName(string name);
   string getUserId();
   ~User();
};

class Admin : public Library {
private:
    string adminId, adminName;
    int adminKey;
public:
    Admin(string id = "", string name = "", int key = 0);
    void adminMenu();
    void adjBook();
    void adjUser();
    void setAdminId(string id);
    void setAdminKey(int key);
    void setAdminName(string name);
    ~Admin();
};

// 현재시간을 string으로 반환하는 함수
string nowTime() {
   time_t now = time(0);
   struct tm now_tm;
   char buf[10];
   now_tm = *localtime(&now);
   strftime(buf, sizeof(buf), "%y%m%d", &now_tm);
   return buf;
}

// 현재시간에 +7일 해서 반환하는 함수
string returnTime() {
   time_t now = time(0);
   struct tm tstruct;
   char buf[10];
   tstruct = *localtime(&now);
   struct tm then_tm = tstruct;
   then_tm.tm_mday += 7;
   mktime(&then_tm); // +7일 했을 때 다음달로 넘어간다면 넘어간 달로 날짜를 계산
   strftime(buf, sizeof(buf), "%y%m%d", &then_tm);
   return buf;
}

// 정적 변수 초기화
vector<UserDB> Library::userDb;
vector<BookDB> Library::bookDb;

// 회원가입 함수 overloading(때려박기용)
void Library::userRegist(string name, string id, string pw) {
   userDb.push_back(name);
   userDb.back().setUserid(id);
   userDb.back().setPw(pw);
}

int Library::verify_id(string id) {
   int tmp = 0;
   for(auto& e : userDb) {
      if(e.getUserid() == id) {
         return tmp;
      }
      tmp++;
   }
   return -1;
}

int Library::verify_pw(string pw, int idx) {
   if(userDb[idx].getPw() == pw) {
      return idx;
   }
   return -1;
}

// DB를 읽어오는 함수
void Library::readDB() {
   int i = 0;
   // When program start
   string filePath = "data/data/com.example.library/user.txt";
   ifstream openFile(filePath.data(), ios::in);

   if (openFile.is_open()) {
      string line;
      while (getline(openFile, line, ',')) {
         if (openFile.eof())   break;
         if (i == 0) {
            userDb.push_back(line);
            i++;
         }
         else if (i == 2) {
            userDb.back().setUserid(line);
            i++;
         }
         else if (i == 4) {
            userDb.back().setPw(line);
            getline(openFile, line);
            i = -1;
         }
         i++;
      }
      openFile.close();
   }
   cout << " UserDB loaded." << endl;
}

void Library::readBookDB() {
   int i = 0;
   // When program start
   string filePath = "data/data/com.example.library/book.txt";
   ifstream openFile(filePath.data(), ios::in);

   if (openFile.is_open()) {
      string line;
      while (getline(openFile, line, ',')) {
         if (openFile.eof())   break;
         if (i == 0) {
            bookDb.push_back(line);
            i++;
         }
         else if (i == 2) {
            bookDb.back().setAuthor(line);
            i++;
         }
         else if (i == 4) {
            bookDb.back().setPublisher(line);
            i++;
         }
         else if (i == 6) {
            bookDb.back().setYear(line);
            i++;
         }
         else if (i == 8) {
            bookDb.back().setLoaner(line);
            i++;
         }
         else if (i == 10) {
            bookDb.back().setLoanDate(line);
            i++;
         }
         else if (i == 12) {
            bookDb.back().setReturnDate(line);
            getline(openFile, line, '\n');
            i = -1;
         }
         i++;
      }
      openFile.close();
   }
   cout << " BookDB loaded." << endl;
}

// DB를 저장하는 함수
void Library::writeUserDB() {
   string filePath = "data/data/com.example.library/user.txt";
   ofstream writeFile(filePath.data(), ios::trunc);

   if (writeFile.is_open()) {
      for (auto& e : userDb) {
         writeFile << e.getName() << "," << e.getUserid() << "," << e.getPw() << "," << endl;
      }
      writeFile.close();
   }

   cout << " BookDB saved." << endl;
}

void Library::writeBookDB() {
   string filePath = "data/data/com.example.library/book.txt";
   ofstream writeFile(filePath.data(), ios::trunc);

   if (writeFile.is_open()) {
      for (auto& e : bookDb) {
         writeFile << e.getBookName() << "," << e.getAuthor() << "," << e.getPublisher() << "," << e.getYear() << ","
            << e.getLoaner() << "," << e.getLoanDate() << "," << e.getReturnDate() << "," << endl;
      }
      writeFile.close();
   }
   cout << " UserDB saved." << endl;
}

// 유저리스트 출력 함수
void Library::printUserList() {
   int i = 1;
   for (auto& e : userDb) {
      cout << i << "번) ";
      e.print();
      i++;
   }
}

// 도서 리스트 출력 함수
void Library::printBookList() {
   int i = 1;
   for (auto& e : bookDb) {
      cout << i << "번) ";
      e.print();
      i++;
   }
}

// 대출 현황 리스트를 출력하는 함수
void Library::loanState(string id) {
   int i = 1;
   for (auto& e : bookDb) {
      if (e.getLoaner() == id) {
         cout << i << "번) ";
         e.printLoanState();
         i++;
      }
   }
}

// DB에서 유저의 이름값을 가져오는 함수
string Library::getUserName(int num) {
   return userDb[num - 1].getName();
}

// 유저 데이터를 수정하는 함수
void Library::setUserName(int num) { // 사용자의 key값을 받는다.
   string name;
   cout << "바꿀 이름 : ";
   getline(cin, name);
   userDb[num - 1].setName(name);
}

void Library::setUserPw(int num) { // 사용자의 key값을 받는다.
   string pw;
   cout << "바꿀 패스워드 : ";
   getline(cin, pw);
   userDb[num - 1].setPw(pw);
}
void Library::setLoan(int num, string id) {
   bookDb[num].setLoaner(id);
   bookDb[num].setLoanDate(nowTime());
   bookDb[num].setReturnDate(returnTime());
}
// 반납할 책의 대출 데이터들을 삭제하는 함수
void Library::delLoan(int num, string id) {
   bookDb[num].setLoaner("");
   bookDb[num].setLoanDate("");
   bookDb[num].setReturnDate("");
}

// 소멸자
Library::~Library() {
   cout << "Library객체 소멸" << endl;
}

UserDB::UserDB(string name) {
   this->name = name;
}

void UserDB::setUserid(string userid) {
   this->userid = userid;
}

void UserDB::setPw(string pw) {
   this->pw = pw;
}

void UserDB::setName(string name) {
   this->name = name;
}

string UserDB::getUserid() {
   return userid;
}

string UserDB::getPw() {
   return pw;
}

string UserDB::getName() {
   return name;
}

void UserDB::print() {
   cout << name << "  " << userid << "  " << pw << endl;
}

// BookDB 생성자
BookDB::BookDB(string bookName) {
   this->bookName = bookName;
   author = "";
   publisher = "";
   year = "";
   loaner = "";
   loanDate = "";;
   returnDate = "";
}
// 관리자가 책추가용 함수
void BookDB::setBookInfo() {
   cout << "저자 : ";
   getline(cin, author);
   cout << "출판사 : ";
   getline(cin, publisher);
   cout << "출판연도 : ";
   getline(cin, year);
}
// getter & setter
void BookDB::setAuthor(string author) {
   this->author = author;
}
void BookDB::setBookName(string bookName) {
   this->bookName = bookName;
}
void BookDB::setPublisher(string publisher) {
   this->publisher = publisher;
}
void BookDB::setYear(string year) {
   this->year = year;
}
void BookDB::setLoaner(string loaner) {
   this->loaner = loaner;
}
void BookDB::setLoanDate(string loanDate) {
   this->loanDate = loanDate;
}
void BookDB::setReturnDate(string returnDate) {
   this->returnDate = returnDate;
}
string BookDB::getAuthor() {
   return author;
}
string BookDB::getBookName() {
   return bookName;
}
string BookDB::getPublisher() {
   return publisher;
}
string BookDB::getYear() {
   return year;
}
string BookDB::getLoaner() {
   return loaner;
}
string BookDB::getLoanDate() {
   return loanDate;
}
string BookDB::getReturnDate() {
   return returnDate;
}

// 출력 함수
void BookDB::print() {
   cout << "(제목 : " << bookName << ") (작가 : " << author << ") (출판사 : " << publisher << ") (출판연도 : " << year
      << ")" << endl;
}

void BookDB::printLoanState() {
   cout << "(제목 : " << bookName << ") (작가 : " << author << ") (출판사 : " << publisher << ") (출판연도 : " << year
      << ") (대출날짜 : " << loanDate << ") (반납기한 : " << returnDate << ")" << endl;
}

// 기본 생성자
User::User() {
   userId = "";
   userName = "";
   userKey = 0;
}

// 매개변수를 받는 생성자
User::User(string userId, int num, string name) {
   this->userId = userId;
   this->userKey = num;
   this->userName = name;
}

// 복사 생성자
User::User(const User& other) {
   cout << "복사 생성자 실행" << endl;
   this->userKey = other.userKey;
   this->userId = other.userId;
   this->userName = other.userName;
}

/*
// 사용자 메뉴
void User::userMenu() {
   string sw;
   do {
      cout << "==============" << userName << "님 환영합니다!===============" << endl;
      cout << "1) 도서목록 \n2) 회원정보 수정 \n3) 나의 대출 현황 \n4) 로그아웃" << endl;
      getline(cin, sw);
      if (sw == "1") {
         printBookList();
         cout << "1) 책대여 2) 뒤로가기";
         getline(cin, sw);
         if (sw == "1") {
            loanBook();
            cin.ignore();
         }
      }
      else if (sw == "2") {
         setInfo(); // 회원정보 수정 함수
         printUserList(); // 리스트 출력(test용)

      }
      else if (sw == "3") {
         myLoanState(); // 빌린책들을 보여주는 함수
         cout << "1) 책반납 2) 뒤로가기";
         getline(cin, sw);
         if (sw == "1") {
            returnBook(); // 반납해주는 함수
            cin.ignore();
         }
      }
      else if (sw == "4") {
         userKey = 0;
      }
   } while (userKey != 0);
}
*/
// 사용자의 정보 수정 함수
void User::setInfo() {
   setUserName(userKey);
   setUserPw(userKey);
}

// 대출을 처리하는 함수
void User::loanBook(int bookidx) {
   setLoan(bookidx, userId);
}

// 반납을 처리하는 함수
void User::returnBook(int bookidx) {
   delLoan(bookidx, userId);
}

// setter
void User::setUserId(string id) {
   userId = id;
}

void User::setUserKey(int key) {
   userKey = key;
}

void User::setName(string name) {
   userName = name;
}

// 대출 현황을 출력해주는 함수
void User::myLoanState() {
   loanState(userId);
}

// getter
string User::getUserId() {
   return userId;
}

// 소멸자
User::~User() {
   cout << "User 객체 소멸" << endl;
}

// login을 담당하는 외부 함수
User login() {
   string id, pw;
   int loginUser;
   User user;
   do {
      cout << "==============로그인===============" << endl;
      cout << "아이디 : ";
      getline(cin, id);
      cout << "비밀번호 : ";
      getline(cin, pw);
      //loginUser = Library::userLogin(id, pw);
   } while (loginUser == 0);
   user.setUserId(id);
   user.setUserKey(loginUser);
   user.setName(user.getUserName(loginUser));
   return user; // User 객체를 반환
}

Admin::Admin(string id, string name, int key) {
   cout << "관리자 생성자 호출" << endl;
   adminId = id;
   adminName = name;
   adminKey = key;

}
Admin::~Admin() {
   cout << "관리자 객체 소멸" << endl;
}
void Admin::setAdminId(string id) {
   adminId = id;
}
void Admin::setAdminKey(int key) {
   adminKey = key;
}
void Admin::setAdminName(string name) {
   adminName = name;
}


extern "C" {
JNIEXPORT void JNICALL
Java_com_example_library_MainActivity_read_1file(JNIEnv *env, jobject) {
    Library::readDB();
    Library::readBookDB();
}

JNIEXPORT void JNICALL
Java_com_example_library_MainActivity_write_1file(JNIEnv *env, jobject) {
    Library::writeUserDB();
    Library::writeBookDB();
}

JNIEXPORT int JNICALL
Java_com_example_library_MainActivity_id_1verify(JNIEnv *env, jobject, jstring input_id) {
    string id = (*env).GetStringUTFChars(input_id, NULL);
    return Library::verify_id(id);
}

JNIEXPORT int JNICALL
Java_com_example_library_MainActivity_pw_1verify(JNIEnv *env, jobject, jstring input_pw,
                                                       jint i) {
    string pw = (*env).GetStringUTFChars(input_pw, NULL);
    return Library::verify_pw(pw, i);
}

JNIEXPORT int JNICALL
Java_com_example_library_SignupActivity_id_1verify(JNIEnv *env, jobject, jstring input_id) {
    string id = (*env).GetStringUTFChars(input_id, NULL);
    return Library::verify_id(id);
}

JNIEXPORT void JNICALL
Java_com_example_library_SignupActivity_join(JNIEnv *env, jobject, jstring join_name,
                                                   jstring join_id, jstring join_pw) {
    string name = (*env).GetStringUTFChars(join_name, NULL);
    string id = (*env).GetStringUTFChars(join_id, NULL);
    string pw = (*env).GetStringUTFChars(join_pw, NULL);
    Library::userRegist(name, id, pw);
}

User* puser;
JNIEXPORT void JNICALL
Java_com_example_library_UserActivity_login(JNIEnv *env, jobject, jstring id, jint idx) {
    const char* userId = env->GetStringUTFChars(id, 0);
    puser = new User();
    puser->setUserId(userId);
    puser->setUserKey(idx);
    puser->setUserName(idx);
}

JNIEXPORT void JNICALL
Java_com_example_library_UserActivity_logout(JNIEnv *env, jobject) {
   delete puser;
}

JNIEXPORT jobject JNICALL
Java_com_example_library_UserActivity_bookdb(JNIEnv *env, jobject) {
    jclass clsArr = env->FindClass("java/util/ArrayList");
    jmethodID constructor = env->GetMethodID(clsArr, "<init>", "()V");
    jobject arr = env->NewObject(clsArr, constructor);

    jclass clsUser = env->FindClass("com/example/library/Bookdb");

    User user;

    for (int i = 0; i < Library::getBookdbSize(); i++) {
        jobject objUser = env->NewObject(clsUser, (*env).GetMethodID(clsUser, "<init>", "()V"));
        jstring title = env->NewStringUTF(user.getBookName(i).c_str());
        jstring author = env->NewStringUTF(user.getAuthor(i).c_str());
        jstring publisher = env->NewStringUTF(user.getPublisher(i).c_str());
        jstring year = env->NewStringUTF(user.getYear(i).c_str());
        jstring loaner = env->NewStringUTF(user.getLoaner(i).c_str());
        jstring loandate = env->NewStringUTF(user.getLoanDate(i).c_str());
        jstring returndate = env->NewStringUTF(user.getreturnDate(i).c_str());

        env->CallVoidMethod(objUser,
                            (*env).GetMethodID(clsUser, "setBookName", "(Ljava/lang/String;)V"),
                            title);
        env->CallVoidMethod(objUser,
                            (*env).GetMethodID(clsUser, "setAuthor", "(Ljava/lang/String;)V"),
                            author);
        env->CallVoidMethod(objUser,
                            (*env).GetMethodID(clsUser, "setPublisher", "(Ljava/lang/String;)V"),
                            publisher);
        env->CallVoidMethod(objUser,
                            (*env).GetMethodID(clsUser, "setYear", "(Ljava/lang/String;)V"),
                            year);
        env->CallVoidMethod(objUser,
                            (*env).GetMethodID(clsUser, "setLoaner", "(Ljava/lang/String;)V"),
                            loaner);
        env->CallVoidMethod(objUser,
                            (*env).GetMethodID(clsUser, "setLoanDate", "(Ljava/lang/String;)V"),
                            loandate);
        env->CallVoidMethod(objUser,
                            (*env).GetMethodID(clsUser, "setReturnDate", "(Ljava/lang/String;)V"),
                            returndate);
        env->CallBooleanMethod(arr, (*env).GetMethodID(clsArr, "add", "(Ljava/lang/Object;)Z"),
                               objUser);
    }

    env->DeleteLocalRef(clsArr);
    env->DeleteLocalRef(clsUser);

    return arr;
}

JNIEXPORT jobject JNICALL
Java_com_example_library_UserActivity_userdb(JNIEnv *env, jobject) {
    jclass clsArr = env->FindClass("java/util/ArrayList");

    jmethodID constructor = env->GetMethodID(clsArr, "<init>", "()V");

    jobject arr = env->NewObject(clsArr, constructor);

    jclass clsUser = env->FindClass("com/example/library/Userdb");

    jmethodID setName = env->GetMethodID(clsUser, "setName", "(Ljava/lang/String;)V");
    jmethodID setId = env->GetMethodID(clsUser, "setId", "(Ljava/lang/String;)V");
    jmethodID setPw = env->GetMethodID(clsUser, "setPw", "(Ljava/lang/String;)V");

    for (int i = 0; i < Library::getUserdbSize(); i++) {
       jobject objUser = env->NewObject(clsUser, (*env).GetMethodID(clsUser, "<init>", "()V"));
        const char *cn = Library::getName(i).c_str();
        const char *ci = Library::getId(i).c_str();
        const char *cp = Library::getPw(i).c_str();
        jstring name = env->NewStringUTF(cn);
        jstring id = env->NewStringUTF(ci);
        jstring pw = env->NewStringUTF(cp);
        env->CallVoidMethod(objUser, setName, name);
        env->CallVoidMethod(objUser, setId, id);
        env->CallVoidMethod(objUser, setPw, pw);
        env->CallBooleanMethod(arr, (*env).GetMethodID(clsArr, "add", "(Ljava/lang/Object;)Z"),
                               objUser);
    }

    env->DeleteLocalRef(clsArr);
    env->DeleteLocalRef(clsUser);

    return arr;
}

Admin * admin;
JNIEXPORT void JNICALL
Java_com_example_library_AdministratorActivity_login_1admin(JNIEnv *env, jobject, jstring id, jint idx, jstring name) {
   const char* adminId = env->GetStringUTFChars(id, 0);
   const char* adminName = env->GetStringUTFChars(name, 0);
   admin = new Admin();
   admin->setAdminId(adminId);
   admin->setAdminKey(idx);
   admin->setAdminName(adminName);
}

JNIEXPORT void JNICALL
Java_com_example_library_AdministratorActivity_logout_1admin(JNIEnv *env, jobject) {
   delete admin;
}

JNIEXPORT jobject JNICALL
Java_com_example_library_AdministratorActivity_bookdb(JNIEnv *env, jobject) {
   jclass clsArr = env->FindClass("java/util/ArrayList");
   jmethodID constructor = env->GetMethodID(clsArr, "<init>", "()V");
   jobject arr = env->NewObject(clsArr, constructor);

   jclass clsUser = env->FindClass("com/example/library/Bookdb");

   User user;

   for (int i = 0; i < Library::getBookdbSize(); i++) {
      jobject objUser = env->NewObject(clsUser, (*env).GetMethodID(clsUser, "<init>", "()V"));
      jstring title = env->NewStringUTF(user.getBookName(i).c_str());
      jstring author = env->NewStringUTF(user.getAuthor(i).c_str());
      jstring publisher = env->NewStringUTF(user.getPublisher(i).c_str());
      jstring year = env->NewStringUTF(user.getYear(i).c_str());
      jstring loaner = env->NewStringUTF(user.getLoaner(i).c_str());
      jstring loandate = env->NewStringUTF(user.getLoanDate(i).c_str());
      jstring returndate = env->NewStringUTF(user.getreturnDate(i).c_str());

      env->CallVoidMethod(objUser,
                          (*env).GetMethodID(clsUser, "setBookName", "(Ljava/lang/String;)V"),
                          title);
      env->CallVoidMethod(objUser,
                          (*env).GetMethodID(clsUser, "setAuthor", "(Ljava/lang/String;)V"),
                          author);
      env->CallVoidMethod(objUser,
                          (*env).GetMethodID(clsUser, "setPublisher", "(Ljava/lang/String;)V"),
                          publisher);
      env->CallVoidMethod(objUser,
                          (*env).GetMethodID(clsUser, "setYear", "(Ljava/lang/String;)V"),
                          year);
      env->CallVoidMethod(objUser,
                          (*env).GetMethodID(clsUser, "setLoaner", "(Ljava/lang/String;)V"),
                          loaner);
      env->CallVoidMethod(objUser,
                          (*env).GetMethodID(clsUser, "setLoanDate", "(Ljava/lang/String;)V"),
                          loandate);
      env->CallVoidMethod(objUser,
                          (*env).GetMethodID(clsUser, "setReturnDate", "(Ljava/lang/String;)V"),
                          returndate);
      env->CallBooleanMethod(arr, (*env).GetMethodID(clsArr, "add", "(Ljava/lang/Object;)Z"),
                             objUser);
   }

   env->DeleteLocalRef(clsArr);
   env->DeleteLocalRef(clsUser);

   return arr;
}

JNIEXPORT jobject JNICALL
Java_com_example_library_AdministratorActivity_userdb(JNIEnv *env, jobject) {
   jclass clsArr = env->FindClass("java/util/ArrayList");

   jmethodID constructor = env->GetMethodID(clsArr, "<init>", "()V");

   jobject arr = env->NewObject(clsArr, constructor);

   jclass clsUser = env->FindClass("com/example/library/Userdb");

   jmethodID setName = env->GetMethodID(clsUser, "setName", "(Ljava/lang/String;)V");
   jmethodID setId = env->GetMethodID(clsUser, "setId", "(Ljava/lang/String;)V");
   jmethodID setPw = env->GetMethodID(clsUser, "setPw", "(Ljava/lang/String;)V");

   for (int i = 0; i < Library::getUserdbSize(); i++) {
      jobject objUser = env->NewObject(clsUser, (*env).GetMethodID(clsUser, "<init>", "()V"));
      const char *cn = Library::getName(i).c_str();
      const char *ci = Library::getId(i).c_str();
      const char *cp = Library::getPw(i).c_str();
      jstring name = env->NewStringUTF(cn);
      jstring id = env->NewStringUTF(ci);
      jstring pw = env->NewStringUTF(cp);
      env->CallVoidMethod(objUser, setName, name);
      env->CallVoidMethod(objUser, setId, id);
      env->CallVoidMethod(objUser, setPw, pw);
      env->CallBooleanMethod(arr, (*env).GetMethodID(clsArr, "add", "(Ljava/lang/Object;)Z"),
                             objUser);
   }

   env->DeleteLocalRef(clsArr);
   env->DeleteLocalRef(clsUser);

   return arr;
}

JNIEXPORT void JNICALL
Java_com_example_library_SearchAdapter_loan(JNIEnv *env, jobject, jint idx) {
   puser->loanBook(idx);
}

JNIEXPORT void JNICALL
Java_com_example_library_BookListAdapter_loan(JNIEnv *env, jobject, jint idx) {
    puser->loanBook(idx);
}

JNIEXPORT void JNICALL
Java_com_example_library_LoanAdapter_returnBook(JNIEnv *env, jobject, jint idx) {
    puser->returnBook(idx);
}
}

/*
int main() {
   bool gamesw = true;
   string sw;
   // DB 읽어오기
   Library::readDB();
   Library::readBookDB();
   //User::userRegist("송재헌", "apple", "12345");
   //User::userRegist("양철주", "banana", "54321");
   cout << endl;
   do {
      try {
         cout << "============도서관리프로그램=============" << endl;
         cout << "1) 회원가입 \n2) 로그인 \n3) 종료" << endl;
         getline(cin, sw);
         cout << sw << endl;
         if (sw == "1") {
            cout << "==============회원가입===============" << endl;
            Library::userRegist(); // static 멤버 함수를 사용하여 회원가입
         }
         else if (sw == "2") {
            User user = login();
            if (user.getUserId() == "admin") {
               // 아이디값이 관리자라면

            }
            else { // 일반 사용자의 아이디라면
               user.userMenu(); // 사용자 메뉴함수 호출
            }
         }
         else if (sw == "3") {
            cout << "프로그램을 종료합니다." << endl;
            // static 멤버함수를 사용하여 수정된 내용을 DB에 저장
            Library::writeUserDB();
            Library::writeBookDB();
            gamesw = false; // game switch를 false로 변경하여 loop를 종료
         }
         else { // 사용자가 1,2,3 외에 다른값을 입력하면
            throw "잘못된 값을 입력하셨습니다."; // 예외를 발생시킨다.
         }
      }
      catch (const char* e) { // 오류의 type이 문자열 이라면 예외를 처리
         cout << e << endl;
      }

   } while (gamesw);
}
*/