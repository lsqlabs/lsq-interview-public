FROM gradle:6.5.0-jdk11 AS build
COPY . /build
WORKDIR /build
RUN gradle build --no-daemon
RUN mkdir unpacked && ls -al && cd unpacked && jar -xf ../build/libs/*.jar

FROM adoptopenjdk:11-jre-hotspot

EXPOSE 8080

WORKDIR /app

COPY --from=build /build/unpacked/BOOT-INF/lib lib
COPY --from=build /build/unpacked/META-INF META-INF
COPY --from=build /build/unpacked/BOOT-INF/classes .

ENTRYPOINT ["java", "-cp", ".:lib/*", "com.lsq.interview.LsqInterviewApplication"]
