package re.yuugu.hzx.types.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ author anon
 * @ description Response
 * @ create 2025/12/31 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response <T> implements Serializable {
    String info;
    String code;
    private T data;
}
