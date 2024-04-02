package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.dto.Student;
import org.example.entity.vo.response.StudentActivityRate;
import org.example.entity.vo.response.StudentWithActivityVO;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
    @Select("select s.*, ad.type, sum(sar.isDone) as doneNum, MAX(ad.sequence) as 'totalNum'" +
            "from student s, student_activity_record sar, activity_detail ad " +
            "where s.sid = sar.sid and ad.aid = sar.aid and YEAR(issue_time) = #{year} and ad.tid = #{tid} and ad.type = #{type} group by s.sid")
    List<StudentWithActivityVO> selectStudentWithActivityListByYearAndInstructorAndType(@Param("year") int year, @Param("tid") String tid, @Param("type") int type);

    @Select("select s.*, ad.type, sum(sar.isDone) as doneNum, MAX(ad.sequence) as 'totalNum'" +
            "from student s, student_activity_record sar, activity_detail ad " +
            "where s.sid = sar.sid and ad.aid = sar.aid and YEAR(issue_time) = #{year} and ad.tid = #{tid} group by s.sid, ad.type order by s.sid")
    List<StudentWithActivityVO> selectStudentWithActivityListAllTypeByYearAndInstructor(@Param("year") int year, @Param("tid") String tid);


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
    @Select("select sar.sid, ad.type, sum(sar.isDone) as doneNum, MAX(ad.sequence) as 'totalNum'" +
            "from student_activity_record sar, activity_detail ad " +
            "where ad.aid = sar.aid and YEAR(issue_time) = #{year} and ad.tid = #{tid} and ad.type = #{type} group by sar.sid")
    List<StudentActivityRate> getFinishRate(@Param("year") int year, @Param("tid") String tid, @Param("type") int type);

    @Select("select COUNT(distinct type) from activity_detail;")
    int selectActivityTypeCount();

}








