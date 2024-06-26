package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.entity.dto.Student;
import org.example.entity.dto.charts.DoUndo;
import org.example.entity.dto.charts.Gender;
import org.example.entity.dto.charts.LearningClassroom;
import org.example.entity.vo.response.StudentTaskRate;
import org.example.entity.dto.charts.Dormitory;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student>{
    //学生信息部分
    @Select("select * from student;")
    List<Student> selectAllStudent();
    @Select("select * from student where name = #{name}")
    List<Student> selectStudentByName(@Param("name") String name);
    @Select("select * from student where classroom = #{classroom}")
    List<Student> selectStudentByClassroom(@Param("classroom") int classroom);
    @Select("select * from student where name = #{name} and classroom = #{classroom}")
    List<Student> selectStudentByNameAndClassroom(@Param("name") String name, @Param("classroom") Integer classroom);

    //考勤部分
    @Select("select str.sid, t.type, sum(str.isDone) as doneNum, MAX(t.sequence) as 'totalNum'" +
            "from student_task_record str, task t " +
            "where t.taskId = str.taskId and t.year = #{year} and t.semester = #{semester} and t.tid = #{tid} and t.type = #{type} group by str.sid")
    List<StudentTaskRate> getFinishRate(@Param("year") int year, @Param("semester") int semester, @Param("tid") String tid, @Param("type") int type);

    @Select("select s.sid, sum(crr.isDone) doneNum, max(t.sequence) totalNum from check_room_record crr, task t, student s where crr.dormitory = s.dormitory and crr.room = s.room and crr.taskId = t.taskId and t.tid = #{tid}  and t.year = #{year} and t.semester = #{semester} group by s.sid")
//    @Results({
//            @Result(column = "sid", property = "sid"),
//            @Result(column = "done", property = "doneNum"),
//            @Result(column = "total", property = "totalNum")
//    })
    List<StudentTaskRate> getType2Rate(@Param("year") int year, @Param("semester") int semester, @Param("tid") String tid);
    @Select("select MAX(t.sequence) as 'totalNum'" +
            "from task t " +
            "where t.year = #{year} and t.semester = #{semester} and t.tid = #{tid} and t.type = #{type}")
    int getTotalNum(@Param("year") int year, @Param("semester") int semester, @Param("tid") String tid, @Param("type") int type);

    @Select("select COUNT(*) from task_type;")
    int selectTaskCount();

    @Select("select sid from student")
    List<String> selectAllSid();

    @Select("select name from student where sid = #{sid}")
    String selectNameBySid(@Param("sid") String sid);
    @Select("select * from student where id = #{id}")
    Student selectStudentById(@Param("id") int id);

    @Select("select name from student where dormitory = #{dormitory} and room = #{room}")
    List<String> selectStudentByDormitoryAndRoom(@Param("dormitory") String dormitory, @Param("room") String room);

    @Select("select s.id, s.sid, s.name, s.qq from student_task_record str left join student s on str.sid = s.sid where str.isDone = #{isDone} and str.taskId = #{taskId}")
    List<DoUndo> selectDoUndo(@Param("taskId") String taskId, @Param("isDone") boolean isDone);


    /*图表使用*/
    @Select("select dormitory as name, count(sid) as value from student group by dormitory")
    List<Dormitory> getDormitory();
    @Select("select classroom, count(gender) as man, sum(gender) as woman from student group by classroom;")
    List<Gender> getGender();
    @Select("select s.classroom, sum(str.isDone) as done, count(str.isDone) as total from student s " +
            "left join student_task_record str on s.sid = str.sid where str.taskId in (${taskId}) group by classroom")
    List<LearningClassroom> getLearningClassroom(@Param("taskId") String taskId);
    @Select("select s.classroom, sum(str.isDone) as done, count(str.isDone) as total from student s " +
            "left join student_task_record str on s.sid = str.sid where str.taskId = #{taskId} group by classroom")
    List<LearningClassroom> getLatestRate(@Param("taskId") String taskId);
}








