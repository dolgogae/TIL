# try-finally 보다는 try-with-resources를 사용하라.  

자바는 InputStream, OutputStream, java.sql.Connection 등의 close를 통해 직접 닫아줘야하는 자원이 있다.  

finally 키워드는 안전하지 못하다. 이유로는 프로그래머의 실수가 빈번하게 일어날 수 있기 때문이다.  

```java
static void copy(String src, String dst) throws IOException { InputStream in = new FileInputStream(src);
    try { 
        OutputStream out = new FileOutputStream(dst);
        try { 
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        } finally { 
            out.close();
        }
    } finally {
        in.close();
    }
}
```

다음과 같이 예외가 2개 이상일 경우는 코드가 너무 복잡해진다.  
또한, try에서 예외를 던지고나면 같은 이유로 close도 실패를 할 수 있을 것이다.(예를 들어 파일이 없다면)  
이 경우에는 두번째 예외(close)가 첫번째 예외(try문)를 삼켜 디버깅을 몹시 어려울 것이다.  

이러한 문제점을 try-with-resources를 통해서 해결할 수 있다.  

```java
static void copy(String src, String dst) throws IOException { 
    try (InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst)) { 
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = in.read(buf)) >= 0)
            out.write(buf, 0, n);
    } 
}
```

다음과 같이 간결하게 작성이 가능하다.  
이 구조를 사용하려면 해당 자원이 AutoCloseable 인터페이스를 구현해야 한다.  
> **AutoCloseable**  
> 단순히 void를 반환하는 close 메서드 하나만 덩그러니 정의한 인터페이스

자바 라이브러리와 서드파티 라이브러리들의 수많은 클래스와 인터페이스가 이미 AutoCloseable을 구현하거나 확장해뒀다.  

또한, try-with-resources도 catch문과 함께 사용이 가능하다.

```java
static String firstLineOfFile(String path, String defaultVal) { 
    try (BufferedReader br = new BufferedReader( new FileReader(path))) { 
        return br.readLine();
    } catch (IOException e) { 
        return defaultVal;
    } 
}
```