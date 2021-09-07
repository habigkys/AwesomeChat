package com.awesome;

import com.awesome.configurations.AwesomeDatasourceConfiguration;
import com.awesome.configurations.LogDatasourceConfiguration;
import com.awesome.configurations.RootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching
public class WebApplication {
    public static void main(String[] args) {
        // 계층구조를 가진 멀티 모듈일 경우 빌더를 사용하여 부모와 자식관계를 만든다.
        // 웹 애플리케이션은 반드시 하위에 위치해야한다. child 는 N개를 설정할수 있다.
        // parent 는 각 child 에서 컨텍스트를 공유하지만, child 간에는 독립된 컨텍스트가 사용된다.
        // new SpringApplicationBuilder(ParentConfig.class).child(ChildConfig.class).run(args);
        // [주의] 계층구조를 만들때는 각 계층의 스프링 기본 application.yml 은 로드하나 autoConfig 객체가 임포트 되어 로딩되지 않는다.
        // @EnableAutoConfiguration 를 각 컨피규레이션 객체에 넣어주면 된다.
        // @EnableAutoConfiguration 선언된 객체의 패키지를 ROOT 로 해서 스캔하면서 설정이 올라온다.
        new SpringApplicationBuilder()
                .parent(RootApplication.class, RootConfiguration.class)
                .listeners(new ApplicationPidFileWriter())
                .build()
                .run(WebApplication.class, args);
    }
}
