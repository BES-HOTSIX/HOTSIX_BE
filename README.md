<div align="center">

## [HOTSHARE 바로가기](https://www.hotshare.me)

<br>
<br>
<br>
<br>

<img src="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/07cbc2a5-b351-4104-b639-fbc556c6b315" width="30%">


![HOTSHARE_IMAGE2](https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/23e6638c-f27e-4793-92d1-5a4b6b79897d)

<br>

![HOTSHARE_NEED](https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/aed81452-2ca4-4258-bf72-88650c969bdd)

<br>

<table>
<tr >
<td align="center">
메인 페이지
</td>
</tr>
<tr>
<td align="center">
      <img src='https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/2f8ef39b-d7b3-400a-81fc-a8fe8682b03d'>
    </td>
</tr>
</table>

|                                                        숙소 등록                                                        |                                                         숙소 예약                                                         |
| :---------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------: |
| <img src='https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/7571cfcc-d7d2-4279-8495-673caf4836b1'> | <img src='https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/4dfc2a2d-a128-4794-b833-42db81cd7b92'> |
|                                                     <b>숙소 결제</b>                                                      |                                                <b>리뷰 남기기</b>                                                |
| <img src='https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/10df2906-9dde-4ef1-b8cc-20d8d6df0517'> | <img src='https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/b2d28a96-45b6-4923-aab6-b33dbf93973d'> |
|                                                     <b>로그인, 회원가입</b>                                                      |                                                <b>마이페이지</b>                                                |
| <img src='https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/e83f7dc3-fb10-42a9-815b-ccea533df783'> | <img src='https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/7d2d8389-26e3-4c0e-911a-54c00e3f4aea'> |





<br/>



<br>

</div>

## 기술 스택

### 프론트엔드

![HOTSHARE_FRONTEND_STACK](https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/97e5323c-2a55-40b2-93cc-7f619fe5314b)

### 백엔드

![HOTSHARE_BACKEND_STACK](https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/81c19be7-6946-45a6-a055-5ace77c4fd7a)

### 인프라

![HOTSHARE_INFRA](https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/55205781-503d-4ed8-8d61-d5d4b720e227)

## 서비스 요청 흐름도

![HOTSHARE_CLIENT](https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/0f19c6e9-277a-45d7-b222-ab46548aa111)

## CI/CD

![HOTSHARE_CI_CD](https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/41add0f2-7967-4409-b3bc-d2c6bb4c6f7c)

## 모니터링 구조도

![HOTSHARE_MONITOR](https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/251a0f83-9c01-4466-b867-82c7ccf593b4)


<br/>

## 🤔️ 기술적 의사 결정
<details>
<summary>우리는 이렇게 생각하고 결정했습니다 !</summary>

| 요구 사항| 선택지                                                      |핵심 기술을 선택한 이유 및 근거|
|:---|:---------------------------------------------------------|:---|
| 배포 환경 모니터링(로그 추적, 서버 상태 확인) | - Loki, Prometheus, Grafana <br> - ELK Stack(Elasticsearch, Logstash, kibana) | - ElasticSearch는 모든 로그 데이터를 인덱싱하여 저장한다. 그렇기 때문에 많은 컴퓨터 자원은 필요로 하게 된다. 반면 Loki는 로그의 메타데이터(로그 레벨, 어플리케이션 이름, 호스트)에 대해서만 인덱싱을 하기 때문에 저장 공간을 절약할 수 있고, 관리를 단순하게 해준다. <br>  -  Prometheus는 시계열 데이터에 최적화 되어있어 시간의 흐름에 따른 서버 상태를 관찰, 분석, 예측하는데 용이하다. PromQL이를 쿼리 언어를 사용하는데 복잡한 시계열 분석을 쉽게 할 수 있게 해주고 시스템 성능 분석이나 트러블 슈팅에 유리하다. ELK Stack은 로그 데이터의 수집, 검색, 시각화에 특화되어있다. 그렇기 때문에 서버의 상태 분석을 위해서는 추가적인 툴을 사용하여야 하는 불편함이 있다. <br> - Grafana는 Loki와 Prometheus를 시각화 시켜주고 에러 로그 발생 시 슬랙으로 자동 알림이 가는 등의 기능이 있어 서버에 문제가 생겼을 경우 신속하고 유연하게 대처가 가능하다.|
| 숙소 등록과 예약 단계에서 입력된 정보의 상태를 유지하여 데이터의 재사용성 보장 | - Recoil<br>- Redux<br>- Context API<br>- Zustand     | - Recoil은 React의 훅 기반 설계와 잘 통합되어 있다. 또 상태 관련 로직을 재사용할 수 있는 방식으로 구성할 수 있어서 코드의 모듈성과 재사용성을 높여준다. 컴포넌트가 필요로 하는 최소한의 상태 변경만을 감지하여 불필요한 렌더링을 줄이므로 성능 유지에 도움이 된다. <br> - Redux는 상태를 관리하는 강력한 도구이지만, 설정이 복잡하고 보일러플레이트 코드가 많다. 비동기 로직 처리를 위한 추가 미들웨어 사용이 필요하다. <br> - Context API는 React에 내장되어 있어 추가 라이브러리 없이 사용할 수 있지만, 복잡한 상태 관리 로직이나 비동기 작업을 처리하기에 한계가 있다.<br> - Zustand 설정이 쉬운 라이브러리로 간단한 상태 관리에 적합하다. 훅 기반의 인터페이스를 제공한다는 점에서 Recoil과 비슷하지만, Recoil이 React와의 통합과 상태 간 의존성 관리에서 더 강력한 기능을 제공한다. <br> |
| oauth2 로그인, 결제 기능 구현을 위한 외부 api http 요청 | - WebClient <br> - RestTemplate   | - RestTemplate 는 동기 처리만 가능하지만 WebClient 는 동기 처리와 비동기 처리 둘 다 가능하여 보다 유연한 설계가 가능하다 <br> - RestTemplate이 deprecated 될 예정이라는 얘기가 있다 <br> - 현재 수행하고 있는 프로젝트에선 문제가 없지만 향후 이용자가 많은 서버를 처리해야 할 경우 WebClient 의 비동기 방식이 필요해질 수 있다 |
| API 문서 자동화를 위한 라이브러리 |  - Swagger | - 개발자가 개발한 REST API 서비스를 편리하게 문서화 해주고 편리하게 API를 호출하고 테스트 할 수 있다 <br> - 의존성 추가와 코드 몇줄만으로 API 문서 자동화를 할 수 있고, 커스텀이 가능하다 |
| 요구사항5 |  선택지                            | 근거 |
| 요구사항6 |  선택지                            | 근거 |

</details>

<br/>

## 🛠️ 트러블슈팅

<details>

<summary>FRONT-END</summary>

### AccesToken 만료 시 생기는 문제

|진행 순서| 내용|
|:---|:---|
|😱 문제|AccessToken 만료가 되면 새로운 AccessToken을 발급받아서 재요청을 보내는 것까지는 정상적으로 작동하는 것을 확인하였다. 근데 저희가 등록, 수정, 삭제 등과 같은 인증이 필요한 요청의 경우 한번 AccessToken 만료로 인해 실패를 한번 하고 새로운 AccessToken을 발급받아서 재요청을 보내기 때문에 처음 실패한 요청으로 인해 Tanstack Query의 useMutation안에 있는 onError에 있는 로직을 수행하게 된다. 그 과정에서 백엔드 서버에 정상적으로 요청이 됐음에도 불구하고 요청이 실패했다는 토스트 메세지와 관련 로직들이 수행되는 문제가 생김. 현재 AccessToken의 만료 기간은 30분으로 설정해놓았기 때문에 30분마다 이런 문제가 발생을 하게 됨.|
|🤔 원인|AccessToken 만료 기한이 30분으로 설정해놓았고 본래의 요청이 AccessToken 만료로 인해 한번 실패한 후 AccessToken을 재발급 받는 요청을 보내고 재요청을 하기 때문.  |
|😭 시도|Axios 인터셉터를 사용했기 때문에 백엔드 서버에 요청을 보내거나 응답을 받을 때 인터셉터에서 처리하는 부분이 많았다. 인터셉터에 대한 이해가 부족한 것 같아 일단 Axios 인터셉터에 대해 공부하였다. 그리고 서버에서 전역적으로 처리하는 예외에 대해서도 공부하니 어떻게 문제를 해결해야 할 지 감이 왔다.   |
|😄 해결|useMutation의 onError 함수를 자세히 살펴보면 우선 const { statusCode, code } = err ?? {} 이렇게 해서 axios interceptor에서 던진 error에서  statusCode와 code를 가져온다. (?? {} 를 포함시킨 이유는 err가 거의 대부분의 경우 null이나 undefined일리는 없지만 가끔 그런 경우가 있다고 한다. 그래서 ?? {} 를 붙여주었다.) 그럼 statusCode에는 400 BadRequest와 같은 http 상태코드가 들어가고 code에는 Spring 에서 예외 처리를 해준 code가 들어간다. if문을 살펴보면 http 상태코드가 400이고 code가 Spring에서 정의해놓았던 accessToken이 만료되었을때의 code와 일치하면 실행되도록 되어있다. 이 경우에는 실패 로직이 아닌 성공 로직을 수행할 수 있도록 바꿔서 문제를 해결했다. 이 외의 경우에는 error가 발생한 것이므로 else문을 써서 실패 로직을 수행한다. |


### 문제2

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

### 문제3

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

### 문제3

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

### 문제4

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |


### 문제5

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

### 문제6

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

</details>

<details>

<summary>BACK-END</summary>

### 문제1

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |


### 문제2

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

### 문제3

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

### 문제3

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

### 문제4

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |


### 문제5

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

### 문제6

|진행 순서| 내용|
|:---|:---|
| 😱 문제|
|🤔 원인|
|😭 시도|  |
|😄 해결| |

</details>

<br/>





## 멤버
|<img src="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/c601a2f4-db66-40d3-8d7d-079ee1f9bee1" width="100" height="100">|<img src ="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/ea331acd-c248-45a3-844d-289e2ce3b0d3" width="100" height="100">|<img src ="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/21b246bc-6147-4eb8-bd9a-e3843608fb00" width="100" height="100">|<img src="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/fce2229f-8811-4bfa-80a2-a6f3423f2739" width="100" height="100">|<img src ="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/b0e7f298-8889-4287-b009-268546f0f24d" width="100" height="100">|<img src ="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/41fd57fc-abc5-4fdc-ba3b-b0606269da15" width="100" height="100">
|:-:|:-:|:-:|:-:|:-:|:-:|
|[김겸호](https://github.com/js030)|[금시연](https://github.com/jkeum-dev)|[김경환](https://github.com/hagd0520)|[정주영](https://github.com/git990412)|[이유현](https://github.com/leeyuhyun0104)|[배현준](https://github.com/bhj2bb)|

<br>
<br>




