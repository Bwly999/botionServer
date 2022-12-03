package cn.edu.xmu.botionserver.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组成文档的小块
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Block {
    private String id;
    private String type;
    private BlockDetail details;
}
