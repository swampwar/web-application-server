# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
* 서버가 클라이언트에게 응답을 한번에 주는것이 아니라 첫번째 요청에 대한 응답으로는 HTML만을 보낸다.
* 이후 브라우저는 HTML 내용을 분석하여 이미지,CSS,자바스크립트 등의 자원이 포함되어 있으면 서버에게 해당자원을 재요청 하게된다.
* 추후 성능이슈를 해결하기 위해서는 이와같은 동작방식을 이해해야 한다.

### 요구사항 2 - get 방식으로 회원가입
* 서버에서 받게되는 Request Header : GET \user\create?userId=&password=&name=&email= HTTP/1.1 
* GET : HTTP-메소드
* /user/create?... : URI, 클라이언트의 요청자원 경로, URL과 거의 같은 의미로 혼용하여 사용한다. 
* HTTP/1.1 : HTTP 버젼

### 요구사항 3 - post 방식으로 회원가입
* 서버에서 받게되는 Request Header : POST \user\create HTTP/1.1 
* 

### 요구사항 4 - redirect 방식으로 이동
* 

### 요구사항 5 - cookie
* 

### 요구사항 6 - stylesheet 적용
* 

### heroku 서버에 배포 후
* 