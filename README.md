# To Do
### Elasticsearch 관련
- [x] ES Java High Level Client 연동 (elasticsearch 버전에 맞는 라이브러리 의존성 추가)
- [x] ES document, Java Object 매핑 (spring starter의 jackson 라이브러리 object mapper 사용)
- [x] 기본적인 조회 API 테스트 
- [ ] Java에서 ES로 document 추가 테스트
- [ ] Scroll API 테스트 (페이징 -> 추후 배치 작업과 연결)
- [ ] ES document projection 테스트 (필요한 필드만 가져오기)
- [ ] ES 집계함수 테스트
- [ ] ES Full text Search 테스트 (match query, bool query 조합 테스트, 복잡한 조건의 쿼리 조합)
- [ ] GeoPoint 연산 테스트  
- [ ] Array형태 받아오는 방법 JSON 파싱 방식 고려
- [ ] 모든 개발에 앞서서 (ES 연동 포함) 먼저 수행할 비즈니스 로직(쿼리)부터 작성
- [ ] 중첩타입 필드 프로젝션 하는 테스트 해보기

### Spring Batch 관련
- [ ] 배치 작업 정의
- [ ] 배치 스케줄링 처리 (스프링의 quartz? 제일 후순위)
  
### 추천 알고리즘 관련
- [ ] 추천 알고리즘 재고 (리더님 피드백 반영, 일차적으로 위의 문제 먼저 해결)
- [ ] 협업 필터링 다시 자료 조사

### 조사
- 쿼리 결과 정렬 기준 (기본 ES의 유사도 score, 1차 정렬 2차 정렬 조건 여러 개 줄 수 있는지?)
  - 가능
  - 정렬기준은 대신 query dsl sort 필드 위에서부터 아래로 차례대로 적용
  - bool query 에 filter 조건 찾아보기
    - filter 조건을 주면 score대로 정렬되지 않는다고함
    - 그럼 먼저 filter로 거른 후, 그 결과내에서 재검색 가능한지?
  - bool query내부 filter말고 그냥 filter 쿼리도 있는데 찾아보기
  - 집계쿼리로 필터조건을 검색할 수 있는 것 같다
    - 집계쿼리로 먼저 특정 기준 알아낸 후, 그 기준 이상 값 가지는 필드만 재검색
- _source 필드 프로젝션 (ES는 필터링이라는 용어로 사용)
  - query dsl _source 필드에 배열 형식으로 입력해주면 해당 필드들만 프로젝션 된다.
- 자바 ES 클라이언트 빈 등록
  - 운영 ES, 추천 ES 두가지 모두 연동해야하므로 빈등록 2개 해야함
  - 동일타입으로 2개이상 빈 등록? Qualifier 사용 고려
  - 꼭 두개다 빈으로 등록해야하나? 운영 ES같은 경우 배치 시작과 끝에 연결해주고 끊어주면 되지 않나?
- UI 관련
  - 사용자 키워드 검색 하게 해줄 것인지?
  - 검색하는 사용자 위치 고려할 것인지?
    - 현재위치 고려한다면, http로 위치정보 어떻게 받을 것인지?
- 배치처리
  - 배치처리(작업)의 정의?
- 형태소 분석기
  - ES 루씬에서 공식으로 제공되는 Nori 분석기 사용
    - nori_tokenizer : 토크나이저
    - nori_part_of_speech : 토큰 필터
    - nori_readingform: 토큰 필터
  - 제목에는 tokenizer와 tokenfilter만 적용
  - desc에는 html tag 제거해주는 character filter 적용
  - decoumpound_mode 옵션 (복합명사 처리 방식)
    - mixed : 원본데이터랑 분리된 복합명사 모두 term으로 저장
  - stoptags에 제거할거 생각해보기
    - 기본적으로 대명사같은 것은 없애도 될 듯