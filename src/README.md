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

### Spring Batch 관련
- [ ] 배치 작업 정의
- [ ] 배치 스케줄링 처리 (스프링의 quartz? 제일 후순위)
  
### 추천 알고리즘 관련
- [ ] 추천 알고리즘 재고 (리더님 피드백 반영, 일차적으로 위의 문제 먼저 해결)
- [ ] 협업 필터링 다시 자료 조사
