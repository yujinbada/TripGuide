# ![image](https://user-images.githubusercontent.com/106158445/204071401-74ab78e2-71f6-4674-b436-042846edbea3.png)
> TripGuide는 여행의 출발부터 다시 집에 돌아오는 것까지 계획을 세워주는 여행 계획 쉽고 빠르게 만들 수 있는 어플리케이션 입니다

# 1. 제작 기간 & 참여인원
  * 2022년 3월 ~ 2022년 11월
  * 팀장: 배준형 (전체 기획, 안드로이드)
  * 팀원: 두유진 (안드로이드, Firebase)

# 2. 사용 기술
<div align=center><h1>📚 STACKS</h1></div>

<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"> 
  <br>
  
  <img src="https://img.shields.io/badge/firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=white">
  <br>
  
  <img src="https://img.shields.io/badge/android studio-3DDC84?style=for-the-badge&logo=android studio&logoColor=white">
  <br>
  
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <br>
</div>

# 3. 프로젝트 배경
> 이 프로젝트는 학과의 졸업과제로 시작하였습니다. 주제를 정할때 저희는 우리가 살면서 필요하다고 생각했던 것을 만들고자 했습니다. 
> 저희는 여행을 갈때 효율적인 동선을 짜기 위해 많은 시간을 쓰곤 했고 그 시간을 줄여주는 어플리케이션을 만들면
> 어떨까 생각을 했습니다. 그래서 졸업과제로 여행 계획을 쉽고 빠르게 만들 수 있는 어플리케이션을 만들었습니다.

# 4. 프로젝트 구현 계획
|순서|구현 내용|설명|구현|
|:---:|:---:|:---:|:---:|
|1|Google Login|Firebase를 이용한 로그인 기능 구현|O|
|2|여행지 추천|정보가 없는 사용자를 위한 여행지 추천|O|
|3|다른 사람의 여행 일정 공유|사용자들 간의 일정 공유|X|
|4|여행 일정 만들기|여행 일정 만들기 구현|O|
|5|사용자 정보 입력|여행 일정 계산을 위한 정보 입력|O|
|6|장소 입력|여행지에서 방문할 장소 입력|O|
|7|여행 일정 완성|최적화된 여행 일정 완성|O|
|8|만들어진 여행 일정 수정|여행 일정 추가 및 삭제, 이동하기|X|
|9|만들어진 여행 일정 공유|카카오톡을 통한 여행 일종 공유|X|

# 5. Overview
![image](https://user-images.githubusercontent.com/106158445/204072551-afd998fa-9a06-431d-bfef-cf5fae1a63b7.png)
  >Firebase를 통한 구글 로그인 후 추천 여행지, 추천 축제, 다른 사람의 여행 일정 확인
  >'나의 여행 일정 만들기' 버튼을 통한 나의 일정 만들기 시작
  
 ![image](https://user-images.githubusercontent.com/106158445/204072634-bfa2ab5c-a802-4253-95dc-449625b3eccf.png)
  >사용자의 기본 정보 입력
  >(기본 정보: 출발지, 도착지, 여행 기간, 여행 인원, 이동 수단)
  
  ![image](https://user-images.githubusercontent.com/106158445/204072868-3e728f76-5d76-4319-be60-04c839ba9c94.png)
  >여행지 입력은 KakaoApi의 로컬 api 에서 address 검색 기능을 Retrofit2로 구현
  >여행 출발지 입력은 KakaoApi의 로컬 api 에서 keyword 검색 기능을 Retrofit2로 구현
  
  ![image](https://user-images.githubusercontent.com/106158445/204072718-0ba2c9b8-09c0-4f40-9b59-da291ae6edf1.png)
  >이동 수단을 '비행기 / 기차'로 선택했다면 추가적인 정보 입력
  >출발, 도착 역 / 공항은 이전의 여행 출발지 입력을 구현한 Fragmentdialog 재사용
  >(추가 정보: 출발, 도착 역 / 공항 and 출발, 도착 시간)
  
  ![image](https://user-images.githubusercontent.com/106158445/204073043-92d40441-0ef0-47a1-8bf7-6fb6c213c9ab.png)
  >방문 장소 Type을 관광지, 식당 / 카페, 숙소 3가지로 분류하고 사용자가 가고자 하는 장소 추가
  >장소 추가는 한국관광공사의 TourApi에 있는 국문관광장소 키워드 검색 서비스를 이용해 구현
  
  ![image](https://user-images.githubusercontent.com/106158445/204073153-8a451988-e0c0-4ae2-9ecd-f81811c52648.png)
  >사용자가 추가한 장소 외에 다른 장소도 추천, 장소 Item 클릭시 사진, 상세정보 및 KakaoMap 위치 확인
  
  ![image](https://user-images.githubusercontent.com/106158445/204073236-b27b71ad-d759-4815-b66b-1e60ebb7db8e.png)
  >사용자의 정보를 바탕으로 한 여행 일정 완성
  
  ![image](https://user-images.githubusercontent.com/106158445/204073353-ba416ebb-5bd7-4fb6-be98-01092ffb61db.png)
  >여행 일정의 

  
 
