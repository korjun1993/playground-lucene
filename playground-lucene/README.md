## 루씬 기본 용어

---

### Field

- 문서를 구성하는 속성
- Field의 값은 Analyzer를 통해 분석되어 하나 이상의 Term으로 구성
- 예시
  - {"필드명": "title", "값": "Apache Lucene 소개"}

### Term
- Analyzer를 통해 분석된 결과
- 역색인표를 구성
- 예시
  - {"필드명": "title", "값": "Apache"}
  - {"필드명": "title", "값": "Lucene"}
  - {"필드명": "title", "값": "소개}]

### Term vector
- 특정 Field 내에서 각 Term의 빈도수 및 위치 정보를 담고 있음
