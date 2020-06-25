FROM gradle:6.5.0-jdk11 AS build
COPY . /build
WORKDIR /build
RUN gradle build --no-daemon
RUN mkdir unpacked && ls -al && cd unpacked && jar -xf ../build/libs/*.jar

FROM adoptopenjdk:11-jre-hotspot

EXPOSE 8080

RUN addgroup --system spring && adduser --system spring
USER spring:spring

COPY --from=build /build/unpacked/BOOT-INF/lib /app/lib
COPY --from=build /build/unpacked/META-INF /app/META-INF
COPY --from=build /build/unpacked/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.lsq.interview.LsqInterviewApplication"]
