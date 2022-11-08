[블록체인 스터디] Simple Blockchain implementation with Java 
==========================================================
아래 레퍼런스 포스팅의 예제와 설명을 참고하여 자바로 블록체인을 구현해봅니다. 
블록체인의 기본적인 개념을 구현하고 간단한 작업 증명 시스템을 구현해봅니다.


다음과 같은 기능이 구현되었습니다.
----------------------------------------------------
 - 블록에 데이터를 저장할 수 있음.
 - 블록의 연관관계를 해쉬를 사용하여 연결함. 
 - 새로운 블록을 만들기 위해 설정된 difficulty에 따른 작업 증명이 진행됨. 
 - 블록이 변경되었거나 유효한지 판별할 수 있음.
 - new Wallet(); 을 통해 유저들이 새로운 지갑을 생성할 수 있음.
 - Elliptic-Curve(타원곡선) 암호를 통한 공개키를 사용해 지갑을 제공함. 
 - 전자서명을 사용해 소유권을 증명하고 안전한 송금을 구현함. 
 - 유저들이 트랜잭션을 만들 수 있는 기능을 제공함. 

개선할 점:
-----------------------------------------------
- 실제 동작이 Local이 아니라 네트워크 상에서 이루어질 수 있도록 개선가능 

Reference:
-----------------------------------------
> https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
> https://medium.com/programmers-blockchain/creating-your-first-blockchain-with-java-part-2-transactions-2cdac335e0ce


