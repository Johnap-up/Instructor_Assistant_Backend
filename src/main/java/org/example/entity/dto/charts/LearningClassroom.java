package org.example.entity.dto.charts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningClassroom {
    public String classroom;
    public Integer done;
    public Integer total;
}
