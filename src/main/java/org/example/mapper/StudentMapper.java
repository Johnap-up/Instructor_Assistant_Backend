package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.dto.Student;
import org.example.entity.vo.response.StudentTaskRate;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    //学生信息部分
    @Select("select * from student;")
    List<Student> selectAllStudent();
    @Select("select * from student where name = #{name}")
    List<Student> selectStudentByName(@Param("name") String name);
    @Select("select * from student where classroom = #{classroom}")
    List<Student> selectStudentByClassroom(@Param("classroom") int classroom);
    @Select("select * from student where name = #{name} and classroom = #{classroom}")
    List<Student> selectStudentByNameAndClassroom(@Param("name") String name, @Param("classroom") int classroom);

    //考勤部分
    @Select("select str.sid, t.type, sum(str.isDone) as doneNum, MAX(t.sequence) as 'totalNum'" +
            "from student_task_record str, task t " +
            "where t.taskId = str.taskId and t.year = #{year} and t.semester = #{semester} and t.tid = #{tid} and t.type = #{type} group by str.sid")
    List<StudentTaskRate> getFinishRate(@Param("year") int year, @Param("semester") int semester, @Param("tid") String tid, @Param("type") int type);

    @Select("select COUNT(*) from task_type;")
    int selectTaskCount();

    @Select("select name from student where sid = #{sid}")
    String selectNameBySid(@Param("sid") String sid);
    @Select("select * from student where id = #{id}")
    Student selectStudentById(@Param("id") int id);
}








