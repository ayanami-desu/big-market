package re.yuugu.hzx.types.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ author anon
 * @ description BaseEvent
 * @ create 2026/1/9 22:41
 */
public abstract class BaseEvent <T>{
    public abstract EventMessage<T> buildEventMessage(T data);
    public abstract String topic();


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EventMessage<T>{
        private String id;
        private Date timeStamp;
        private T data;
    }
}
