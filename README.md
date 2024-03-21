<div align="center">

## [HOTSHARE 바로가기](https://www.hotshare.me)
aa
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
| 데이터의 저장 및 관리 | MYSQL  |- 프로젝트가 대용량 및 분산 환경, 효율적인 분석 및 집계작업이 큰 비중을 차지하지 않아서 RDB, NoSQL, Graph Database, Columnar Database 등 다양한 데이터베이스 중 관계형DB를 선택했습니다. <br> - 관계형 데이터베이스에도 MySQL, PostgreSQL, MariaDB 등 다양한 종류가 있는데 이중 안정성과 신뢰성, 확장성이 높고 스프링부트와의 통합 원활한 MySQL을 선택했습니다. <br>  - PostgreSQL은 MySQL과 비교하여 고급 기능과 높은 데이터 무결성과 안정성을 제공하지만 설정이 복잡한 단점이 있습니다. <br> - 마리아디비는 MySQL의 포크로 시작되었지만, MySQL에 비해 커뮤니티 규모가 작다는 단점이 있습니다. |
| 서버 데이터와의 동기화 및 업데이트 |  Tanstack Query | - 데이터 fetching 요청이 있을 때, 이에 대한 loading, error, success 상태를 바로 얻을 수 있다. <br> - 동일한 네트워크 요청이 발생시, 저장된 값을 재사용하여 불필요한 네트워크 요청을 막을 수 있다.  |

</details>

<br/>

## 🛠️ 트러블슈팅

<details>

<summary>FRONT-END</summary>

### AccesToken 만료 시 생기는 문제

|진행 순서| 내용|
|:---|:---|
|😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|AccessToken 만료가 되면 새로운 AccessToken을 발급받아서 재요청을 보내는 것까지는 정상적으로 작동하는 것을 확인하였다. 근데 저희가 등록, 수정, 삭제 등과 같은 인증이 필요한 요청의 경우 한번 AccessToken 만료로 인해 실패를 한번 하고 새로운 AccessToken을 발급받아서 재요청을 보내기 때문에 처음 실패한 요청으로 인해 Tanstack Query의 useMutation안에 있는 onError에 있는 로직을 수행하게 된다. 그 과정에서 백엔드 서버에 정상적으로 요청이 됐음에도 불구하고 요청이 실패했다는 토스트 메세지와 관련 로직들이 수행되는 문제가 생김. 현재 AccessToken의 만료 기간은 30분으로 설정해놓았기 때문에 30분마다 이런 문제가 발생을 하게 됨.|
|🤔 원인|AccessToken 만료 기한이 30분으로 설정해놓았고 본래의 요청이 AccessToken 만료로 인해 한번 실패한 후 AccessToken을 재발급 받는 요청을 보내고 재요청을 하기 때문.  |
|😭 시도|Axios 인터셉터를 사용했기 때문에 백엔드 서버에 요청을 보내거나 응답을 받을 때 인터셉터에서 처리하는 부분이 많았다. 인터셉터에 대한 이해가 부족한 것 같아 일단 Axios 인터셉터에 대해 공부하였다. 그리고 서버에서 전역적으로 처리하는 예외에 대해서도 공부하니 어떻게 문제를 해결해야 할 지 감이 왔다.   |
|😄 해결|useMutation의 onError 함수를 자세히 살펴보면 우선 const { statusCode, code } = err ?? {} 이렇게 해서 axios interceptor에서 던진 error에서  statusCode와 code를 가져온다. (?? {} 를 포함시킨 이유는 err가 거의 대부분의 경우 null이나 undefined일리는 없지만 가끔 그런 경우가 있다고 한다. 그래서 ?? {} 를 붙여주었다.) 그럼 statusCode에는 400 BadRequest와 같은 http 상태코드가 들어가고 code에는 Spring 에서 예외 처리를 해준 code가 들어간다. if문을 살펴보면 http 상태코드가 400이고 code가 Spring에서 정의해놓았던 accessToken이 만료되었을때의 code와 일치하면 실행되도록 되어있다. 이 경우에는 실패 로직이 아닌 성공 로직을 수행할 수 있도록 바꿔서 문제를 해결했다. 이 외의 경우에는 error가 발생한 것이므로 else문을 써서 실패 로직을 수행한다. |


### 결제하기 전 예약 정보를 저장한 임시 예약 데이터의 ID를 전역 상태로 관리

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|예약 페이지에서 예약 버튼을 눌러 결제 페이지로 넘어갔을 때 임시 예약 데이터가 생성됨. 결제 페이지에서 뒤로가기 혹은 다른 페이지로 이동하고 다시 예약을 시도하면 또 새로운 임시 예약 데이터가 생성되어 불필요한 데이터가 쌓임|
|🤔 원인|결제 페이지에서 예약 데이터의 id가 필요해서 미리 예약 데이터를 생성하는데 결제까지 완료되지 않은 데이터에 대해 따로 처리하지 않음|
|😭 시도| Recoil을 이용해서 생성된 임시 예약 데이터의 상태 관리를 하고자 함 |
|😄 해결|Recoil을 사용하여 클라이언트 측에서 세션스토리지를 활용해 임시 예약 데이터의 ID를 전역 상태로 관리하게 구현했다. 숙소 예약 페이지에서 결제 페이지로 넘어갈 때, 임시로 생성된 예약 데이터의 ID가 reserveIdState에 저장된다. 저장된 ID를 이용하여 사용자가 결제를 완료하지 않고 다른 페이지로 이동했다가 다시 예약을 시도할 경우, 기존의 임시 예약 데이터를 재사용할 수 있다. Recoil의 atom을 사용하여 상태를 정의하고, useRecoilState 훅을 통해 해당 상태를 컴포넌트에서 읽고 쓸 수 있게 했으며, recoilPersist로 상태를 영속화하여 페이지를 새로고침해도 유지되도록 했다.|

### Input, Checkbox 연동

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|가격을 설정하는 input 을 설정할 때 체크할 경우 최대금액을 입력해주는 checkbox를 추가하려고 했다. 이때 체크할 경우 동적으로 지정된 최대 금액을 입력해주지만 체크를 해제를 할 경우엔 최대 금액으로 유지되었다.|
|🤔 원인|체크 했을 경우의 로직과 별개로 체크를 해제했을 경우의 로직이 따로 필요하다|
|😭 시도| useState 를 활용하여 체크할 경우 기존의 최대 금액 설정 동작은 그대로 유지하면서도 추가로 체크 이전의 값을 저장하는 동작을 추가 |
|😄 해결|체크를 해제할 경우엔 저장했던 값을 현재 금액으로 설정함으로써 해결하였다. 이로써 사용자는 체크할 경우 최대 금액을, 체크를 해제할 경우엔 체크하기 전의 금액을 설정할 수 있다. |

### 페이지네이션 컴포넌트 리렌더링 문제

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|페이지네이션을 구현할때 페이지를 이동할시 페이지네이션 컴포넌트도 리렌더링이 발생.|
|🤔 원인|페이지를 이동할때마다 response 값이 새로 받아와지면서 response 내부의 totalpage 값을 사용하는 페이지네이션 컴포넌트도 리렌더링 됨|
|😭 시도| usememo를 통해 totalpage값이 바뀌지 않는다면 리렌더링하지 않도록 변경, 하지만 usequery를 통해 데이터를 불러오는 과정에서 기존 데이터에 변경 사항이 발생하기때문에 해결되지 않음 |
|😄 해결| useMemo와 useQuery의 keepPreviousData 옵션을 동시에 활용하여 이전 데이터를 유지하도록 설정했습니다. 이렇게 하면 useQuery가 새로운 데이터를 불러오면서 이전 데이터를 유지할 수 있어, 페이지 이동 시에도 컴포넌트의 리렌더링을 최소화할 수 있었습니다. |

### 전체 리뷰 불러오기

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| 전체 리뷰 목록을 불러오는 도중에 "TypeError: allReviews.map is not a function" 에러가 발생|
|🤔 원인| 리뷰 목록이 로드되기 전에 컴포넌트에서 allReviews를 매핑하려고 시도하고 있어서 발생 |
|😭 시도| Array.isArray(recentReviews)로 배열인지 확인, allReviews가 로드된 후에 매핑하도록 하기 위해 useEffect를 사용하여 비동기 처리를 추가 |
|😄 해결| useEffect를 이용하여 데이터 로드가 완료된 후에 매핑하도록 수정 |


### 멀티파트 요청 관련

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| 이미지와 string 타입을 하나의 폼으로 묶어서 요청할 때 오류 발생.|
|🤔 원인| 이미지와 string 타입을 함께 백엔드에 요청을 보낼 때 multipart/form-data 형식을 사용하지 않았기 때문에 발생 |
|😭 시도| string 데이터만 보내고 이미지를 따로 보내는 방식 시도. |
|😄 해결|이미지를 외부 서비스를 이용하여 올리고 url을 받아서 string 타입으로 변경 후 요청하는 방식으로 해결. |


</details>

<details>

<summary>BACK-END</summary>

### 예약 페이지 날짜 선택 시 예약되어 있는 날짜는 선택 불가능

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| 예약 페이지에서 이미 예약되어 있는 날짜는 선택이 불가능해야 하고, 날짜 선택 시 체크인 날짜와 체크아웃 날짜는 동일하게 선택되면 안 된다. 그런데 비활성화 된 날짜 사이에 하루만 예약되어 있지 않은 날짜가 있는 경우, 그 날짜가 체크인 날짜로 선택 가능해지면서 체크아웃 날짜가 예약이 불가능한 그 다음날로 선택되었다.|
|🤔 원인| 초기의 체크아웃 날짜를 무조건 체크인 날짜의 다음 날로 지정했다. 사용자가 날짜를 선택하지 않으면 비활성화 시키는 날짜 목록에 들어있는 날짜도 선택된 것처럼 변수에 값이 들어가고 있었다.|
|😭 시도|애초에 예약되어 있는 날짜 목록을 보낼 때, 중간에 하루만 선택 가능한 날짜가 있으면 그 날짜도 목록에 포함시켜서 비활성화 시키고자 했다.  |
|😄 해결|예약 내역 테이블에서 해당 숙소로 예약되어 있는 모든 데이터를 불러와서 체크인/체크아웃 날짜를 현재 날짜와 비교했다. 현재 날짜부터 그 이후의 날짜들 중 체크인 날짜부터 체크아웃 날짜 사이의 모든 날짜를 리스트에 추가했다. 날짜 리스트를 정렬한 뒤, 연속되지 않는 날짜가 나올 때 그 간격을 식별해서 간격이 하루인 경우 그 날짜도 리스트에 추가했다. 이렇게 예약 기능에서 체크인 날짜와 체크아웃 날짜가 동일할 수 없다는 것을 고려하여 예약이 불가능한 날짜 사이에 선택이 가능한 날짜가 하루만 들어있는 경우가 생기는 것을 배제했더니 날짜 선택 로직 구현이 간단해졌다.|


### 스웨거 API 테스트 쿠키 생성 문제

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| 스웨거 환경을 구성하면서 Refresh Token이 필요한 API 테스트 도중 쿠키가 안넘어가는 문제가 생김|
|🤔 원인 | Spring boot 에서 보내는 Cookie의 도메인 설정(127.0.0.1)이 스웨거 URL(localhost)의 도메인과 맞지 않아 스웨거 URL에서 쿠키 생성이 되지 않음|
|😭 시도| - 스웨거에서 로그인 API를 통해 쿠키를 받아옴, 하지만 결국 Spring boot 에서 쿠키에 설정된 도메인이 스웨거 URL과 다르기때문에 쿠키 생성이 안됨<br> - 스웨거에서 직접 쿠키값을 헤더에 설정해서 API 요청함, 이 경우 스웨거 공식 문서에서 스웨거에서 쿠키를 직접 헤더에 포함시키는건 지원이 안된다고 확인|
|😄 해결| Spring boot의 설정 파일에서 Cookie의 도메인을 localhost로 변경하여 해결  |

### 잘못된 provider 응답

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| 클라이언트가 OAuthLogin 엔드포인트를 통해 잘못된 provider 를 제공하는 경우에도 200으로 응답|
|🤔 원인|OAuthLogin 메소드에서는 제공된 provider에 따라 다른 동작을 수행하도록 구현되어 있지만, 잘못된 provider에 대한 처리가 없어서 잘못된 provider가 제공될 경우에도 기본적으로 성공 상태 코드(200)가 반환됨|
|😭 시도|잘못된 provider에 대해서는 HTTP 응답 코드를 400으로 변경  |
|😄 해결|OAuthLogin 메소드에서는 provider가 유효한지 먼저 확인하고 잘못된 provider를 사용할 경우 400 Bad Request를 클라이언트에게 반환하여 오류 해결 가능 |



### WebClient 비동기 환경 내 Transactional 작업 이슈

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| WebClient 를 사용할 경우 CRUD 중 Create 와 Read 에는 문제가 없지만 Update 와 Delete 가 처리되지 않았다.|
|🤔 원인| WebClient 는 비동기를 지원한다. JPA 의 더티체킹은 스레드에 트랜잭션을 할당하고 해당 트랜잭션이 끝나는 시저멩 변화가 있는 모든 엔티티 객체를 데이터베이스에 자동으로 반영해주는데 비동기를 사용할 경우 별도의 스레드를 사용하기 때문에 영속성 컨텍스트의 더티체킹이 적용되지 않는다|
|😭 시도| 생성이나 조회 작업의 경우 WebClient 의 비동기 작업 흐름에 두고 수정과 삭제 작업은 따로 분리하여 수행시키도록 코드를 분리 <br> 하지만 WebClient 흐름 내에서 생성된 값을 사용하지 못할 경우 수행할 수 없는 작업들에서 문제가 잔재|
|😄 해결| block() 메소드를 사용하여 비동기 흐름을 끊고 응답값을 가져와 직접 활용|


### Review 엔티티 필드에 값이 저장되지 않는 문제

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|리뷰 등록 시 rating 값이 항상 null로 저장되는 문제|
|🤔 원인|ReviewService에서 계산된 값이 rating 필드에 저장되지 않음 |
|😭 시도| getRating 메서드를 수정해서 ReviewRequestDTO 클래스에서 계산된 값이 rating 필드에 저장되도록 변경 |
|😄 해결|ReviewService의 addReview 메서드에서 ReviewRequestDTO에서 rating 값을 가져와서 review 객체 생성 시에 설정하도록 수정 |

### 숙소 등록 Swagger 테스트 시 문제 발생 

|진행 순서| 내용|
|:---|:---|
| 😱 문제 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;| 숙소 등록 시 숙소의 정보와 사진을 함께 받아야 해서 서버에 http 요청 헤더에 mutipart/form-data를 붙이고 숙소 정보는 application/json 그리고 사진을  mutipart/form-data  헤더로 요청을 보내 인자에 값을 넣을 때 각각의 스프링 컨버터로 역직렬화 하여 서비스 로직을 통해 저장하는 형태였다. 하지만 스웨거에서 인자들에 붙어있는 어노테이션이나 메서드 레벨에 붙어있는 어노테이션으로 자동화를 해주면 테스트 시   역직렬화가 되지 않는 오류가 발생.|
|🤔 원인|스웨거가 메서드 인자들에 붙어있는 어노테이션이나 메서드 레벨에 있는 어노테이션을 보고 자동으로 api 문서를 만들어주지만 추가 설정을 해주지 않으면 테스트에 번번히 실패. |
|😭 시도| 스웨거에서도 http요청을 mutipart/form-data로 보내고 숙소 정보, 사진 파일을 각각 application/json, mutipart/form-data로 요청해 역직렬화가 가능하게 만드는 설정들을 계속 찾아보았다.   |
|😄 해결|@Schema(type = "string", format = "binary"))를 숙소 정보 DTO 앞에 붙여주면 숙소 정보 또한 json 형식의 파일을 첨부할 수 있게 되고 사진, 숙소 정보가 각각 역직렬화 되어 테스트를 실행할 수 있었다. |

</details>

<br/>





## 멤버
|<img src="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/c601a2f4-db66-40d3-8d7d-079ee1f9bee1" width="100" height="100">|<img src ="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/ea331acd-c248-45a3-844d-289e2ce3b0d3" width="100" height="100">|<img src ="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/21b246bc-6147-4eb8-bd9a-e3843608fb00" width="100" height="100">|<img src="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/fce2229f-8811-4bfa-80a2-a6f3423f2739" width="100" height="100">|<img src ="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/b0e7f298-8889-4287-b009-268546f0f24d" width="100" height="100">|<img src ="https://github.com/BES-HOTSIX/HOTSIX_BE/assets/96820952/41fd57fc-abc5-4fdc-ba3b-b0606269da15" width="100" height="100">
|:-:|:-:|:-:|:-:|:-:|:-:|
|[김겸호](https://github.com/js030)|[금시연](https://github.com/jkeum-dev)|[김경환](https://github.com/hagd0520)|[정주영](https://github.com/git990412)|[이유현](https://github.com/leeyuhyun0104)|[배현준](https://github.com/bhj2bb)|

<br>
<br>




