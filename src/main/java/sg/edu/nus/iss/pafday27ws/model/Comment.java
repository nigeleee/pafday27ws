package sg.edu.nus.iss.pafday27ws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Comment {
    private String user;
    private Integer rating;
    private String comment;
    private Integer gid;

}
