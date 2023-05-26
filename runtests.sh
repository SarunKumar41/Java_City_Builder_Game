javac -cp ".:./tests/lib/junit-4.12.jar:./tests/lib/hamcrest-core-1.3.jar" tests/Tests.java
java -javaagent:"./tests/lib/jacocoagent.jar" -cp ".:./tests/lib/junit-4.12.jar:./tests/lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore tests.Tests

java -jar ./tests/lib/jacococli.jar report ./jacoco.exec --classfiles ./settings/Storage.class --sourcefiles ./settings/ --html ./tests/coverage