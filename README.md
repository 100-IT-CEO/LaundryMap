![스크린샷 2022-07-10 오후 7 53 29](https://user-images.githubusercontent.com/68109961/179046769-eee1da94-b55b-4e15-a9cb-46c0338f1766.png)

# Intro,  어플리케이션 소개 
안녕하세요 IT_CEO조 숙명여자대학교 최가희 카이스트 구민재 입니다.

저희는 어떤 안드로이드 앱을 만들어볼까 고민하다가 , 아직 세상에 없는 앱을 고민해보게 됐습니다. 
저희의 목표는 이 시스템을 개발하여 대기업에게 시스템을 파는게 목표입니다. 

혹시 코인세탁소 이용 해 보셨나요? 기숙사에 있는 세탁기나, 밖에서 코인세탁기를 이용해보신 분들은 공감하실겁니다.
대부분의 코인세탁소의 단점은 현재 코인세탁소에 세탁기가 이용중인지 , 대기중인지 알지못한다는 점이었습니다.
저희 조는 그 부분을 캐치하여 , 우리나라의 코인세탁소의 세탁기나 건조기 상태를 큐알코드로 체크할 수 있는 시스템을 탑재한 안드로이드 앱을 만들었습니다. 

저희 앱은 세탁가맹점 확장성도 굉장히 용이합니다. 저희 시스템을 원하는 세탁소에 가서, 각각의 기기에 id를 부여한 후 카카오결제를 포함한 qr코드를 붙이면 바로 시스템 적용이 가능합니다. 
큐알코드를 통해, 각 세탁기의 id를 부여하고, 부여한 id를 통해 세탁기의 상태를 파악할 수 있습니다.

## Topic
> 몰입캠프 2주차 공통과제 : 서버, DB를 이용한 앱 제작

## Concept
- 세상에 없는 앱을 고민해보았습니다.
- 현실에서 적용 가능한, 기업에게 팔 수 있는 시스템을 만드는 것을 목표로 했습니다.


 ![Video Label](http://img.youtube.com/vi/z7II2r4DytY/0.jpg)

## Splash 화면
- 간단한 애니메이션을 넣어 로딩 화면을 구현했습니다.

## 로그인 화면
- 카카오 로그인 SDK를 사용해서 회원가입 및 로그인 기능을 만들었습니다.
- 로그인 버튼을 눌렀을 때, 카카오톡이 설치되어 있는 지 확인해서 설치된 경우 카카오톡으로 로그인, 아닌 경우 카카오 계정으로 로그인하게 됩니다.

## Map
- Naver Map API를 사용했습니다.
- 전국의 코인 세탁소 300여 곳을 표시하기 위해 Naver Map API의 Marker을 커스터마이징해서 사용했습니다.
- 세탁소 마커를 클릭하면 화면에 표시되는 다이얼로그를 통해 세탁소의 이름, 사진, 전화번호, 이용 가능 현황을 조회할 수 있습니다.
- 전화번호를 클릭하면 다이얼로 넘어가 전화를 쉽게 걸 수 있도록 했습니다.
- 예약하기를 눌러 예약 페이지로, 이용하기를 눌러 QR 인식 화면으로 넘어갈 수 있습니다.

## 예약하기
- 해당하는 세탁소의 예약 가능한 세탁기, 건조기를 보여줍니다.
- 예약하고 싶은 기기를 누르면 10분간 예약이 진행됩니다.
- 예약할 수 없는 기기는 선택할 수 없는 상태가 됩니다.

## 사이드 페이지
- 카카오톡 프로필, 예약상태, 이용상태 등을 확인할 수 있습니다.
- 카카오톡 닉네임 옆 화살표를 누르면 마이페이지로 들어갈 수 있습니다.
- 예약 기록을 누르면 현재 진행중인 예약의 남은 시간을 볼 수 있고, 예약 취소도 가능합니다.
- 이용 기록을 누르면 현재 이용중인 기기가 있을 경우 사용 시작 시간과 작동 시간을 조회 가능합니다.

## 마이 페이지
- 간단한 프로필사진과 닉네임, 가입일을 나타내었습니다.
- 이용기록을 눌러서 현재까지 총 이용 횟수와 어떤 종류의 기기를 몇 번 이용했는지, 또한 각각 이용했던 장소와 시간, 기기 종류 등을 조회할 수 있습니다.
- 가장 아래에는 로그아웃 버튼을 통해 카카오 계정 로그아웃을 진행할 수 있습니다.

# 팀원
[최가희](https://github.com/GaHee99)
[구민재](https://github.com/9mande)
