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
  
---
# 스프링 배치 개념 정리
출처 : https://jojoldu.tistory.com/324?category=902551

### 배치 애플리케이션이란?
> 배치는 **일괄처리** 라는 뜻을 갖고 있다.

- 하루에 한 번 수행되는 대용량 데이터 처리를 위해 API를 구성하는 것은 낭비!
- 데이터가 너무 많아서 처리중에 실패할 수도 있다.
  - 실패지점부터 다시 실행할 수 있다면 좋을 것
- 같은 데이터 처리 또 실행한다면 중복 데이터 뻥튀기 된다.
  - 같은 파라미터로 같은 함수를 실행할 경우, 이미 실행한 적이 있어 실패하는 기능 지원
- 바로 이런 단발성으로 대용량 데이터를 처리하는 애플리케이션을 **배치 애프리케이션**이라고 한다.
- 위의 고민들을 다시 생각해보면 배치 애플리케이션을 구성하기 위해서는 **비즈니스 로직 외에 부가적으로 신경써야할 부분들이 많다**는 것

### 배치 애플리케이션의 조건
- 대용량 데이터 : 대량의 데이터를 가져오거나, 전달하거나, 계산하는 등의 처리가 가능해야함
- 자동화 : 심각한 문제해결을 제외하고는 **사용자 개입 없이 실행**되어야함
- 견고성 : 배치 애플리케이션은 잘못된 데이터를 충돌/중단 없이 처리할 수 있어야함
- 신뢰성 : 배치 애플리케이션은 무엇이 잘못되었는지 추적할 수 있어야함 (로깅/ 알림)
- 성능 : 배치 애플리케이션은 **지정한 시간 안에 처리를 완료**하거나 동시에 실행되는 **다른 애플리케이션을 방해하지 않도록 수행**되어야함

### Spring Batch
Spring Batch 는 Spring의 특성을 그대로 가져왔음
**DI, AOP, 서비스 추상화** 등 Spring 프레임워크의 3대요소를 모두 사용 가능

### Batch vs Quartz
- Quartz : 스케줄러의 역할, Batch와 같이 대용량 데이터 배치처리에 대한 기능을 지원하지 않음
- Batch : 스케줄 기능을 지원하지 않음

결론 : Quartz + Batch 를 조합해서 사용

### Batch 사용 사례
#### 일매출 집계
많은 거래가 이루어지는 커머스 사이트의 경우 하루 거래건이 50~100만건까지 나옴
이와 같은 경우 관련 데이터 최소 100만에서 200만 row 이상
한달이면 5000만에서 1억까지 가능

이를 **실시간 집계 쿼리**로 해결하기에는 조회 시간이나 서버부하가 심각함

그래서 매일 새벽, 전날의 매출 집계 데이터를 만들어서 외부 요청이 올 경우 **미리 만들어준 집계 데이터를 바로 전달**하면 성능과 부하를 모두 잡을 수 있음

![image](https://user-images.githubusercontent.com/40594564/109596504-bd1c8200-7b59-11eb-85fc-159ec1749727.png)


### 마무리
> Spring Batch는 **정해진 시간마다 데이터 가공이 필요**한 경우 어디서든 사용될 수 있다.

---

# Spring Batch - JobParameter와 Scope

출처 : https://jojoldu.tistory.com/330?category=902551 

Spring Batch의 경우 외부 혹은 내부에서 파라미터를 받아 여러 Batch 컴포넌트에서 사용할 수 있게 지원
이 파라미터를 **Job Parameter**라고 한다.

Job Parameter를 사용하기 위해서는 항상 **Spring Batch 전용 Scope**를 선언해야한다!

- @StepScope
- @JobScope

크게 위와 같이 2가지가 존재, 아래와 같이 SpEL로 선언해서 사용하면 된다. (Value애노테이션은 스프링 프레임워크 애노테이션이다 lombok 아님!)

```@Value("#{jobParameters[파라미터명]}")```

- @JobScope : Step 선언문에서 사용가능
- @StepScope : Tasklet이나 ItemReader, ItemWriter, ItemProcessor에서 사용가능

###  @StepScope & @JobScope 소개
- Spring Batch는 @StepScope와 @JobScope라는 특별한 Bean Scope를 지원
- Spring Bean의 기본 Scope는 Singleton이다.
- 그러나 Spring Batch 컴포넌트 (Tasklet, ItemReader, ItemWriter, ItemProcessor 등)에 @StepScope를 사용하게 되면, Spring Batch가 Spring 컨테이너를 통해 지정된 **Step의 실행시점에 해당 컴포넌트를 Spring Bean으로 생성**
- 마찬가지로 @JobScope는 **Job 실행시점**에 Bean이 생성된다.
- 즉, **Bean의 생성 시점을 지정된 Scope가 실행되는 시점으로 지연**시킨다.

> 어떻게 보면 MVC의 request scope와 비슷할 수 있다.
> 
> request scope가 request가 왔을 때 생성되고, response를 반환하면 삭제되는 것처럼,
> 
> JobScope, StepScope 역시 Job이 실행되고 끝날때, Step이 실행되고 끝날 때 생성/삭제가 이루어진다고 보면 된다.

#### Bean의 생성시점을 애플리케이션 실행 시점이 아닌, Step혹은 Job의 실행시점으로 지연시키면 얻는 장점은?
- JobParameter의 Late Binding이 가능
  - 꼭 Application 실행시점이 아니더라도, Controller나 Service와 같은 **비즈니스 로직 처리단계에서 JobParameter를 할당** 가능
- 동일한 컴포넌트를 병렬 혹은 동시에 사용할 때 유용
  - Step안에 Tasklet이 있고, 이 Tasklet은 멤버변수와 멤버 변수를 변경하는 로직이 있다 가정
  - 이 경우 @StepScope없이 Step을 병렬로 수행시킨다면 서로다른 Step에서 하나의 Tasklet을 두고 마구잡이로 상태를 변경하려 할 것
  - 왜냐면 Tasklet이 기본 scope인 싱글톤으로 생성되기 때문
  - @StepScope가 있다면 가각의 Step에서 별도의 Tasklet을 생성하고 관리하기 때문에 서로의 상태를 침범할 일 없음
  