# 서버통신 오류 해결
```
onFailure() called/ t: javax.net.ssl.SSLHandshakeException: Chain validation failed
```
위와 같은 오류가 발생했다..  
처음보는 오류라 찾아봤더니, 에뮬레이터의 시간과 서버통신되는 시점이 비슷하지 않으면 오류가 발생한다고 한다..  
- [참고문헌](https://geekcarrot.net/ko/android-%EC%8A%A4%EB%A7%88%ED%8A%B8%ED%8F%B0%EC%97%90%EC%84%9C-https-ssl-tsl-%EC%97%B0%EA%B2%B0-%EC%98%A4%EB%A5%98-%EC%88%98%EC%A0%95)
```
SSL/TSL 인증서는 브라우저 시스템과 웹 서버의 시계가 거의 같은 시간으로 설정되어 있지 않은 경우
 연결이 잘못된 것으로 간주
```